/**
 *
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.craimport;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * Utility test that downloads the CRA import file (FARM_IMPORT_VERSIONS.IMPORT_FILE)
 * behind the most recent CRA scenario of each of the given participants, and writes
 * each one to disk under {@link #OUTPUT_DIRECTORY} using its IMPORT_FILE_NAME.
 *
 * Read-only against the database. Edit {@link #PINS} and {@link #OUTPUT_DIRECTORY}
 * before running; requires VPN access to the environment TestUtils connects to.
 *
 * @author awilkinson
 */
public class CraImportFileDownloadTest {

  private static Logger logger = LoggerFactory.getLogger(CraImportFileDownloadTest.class);

  /** Where the downloaded files are written. Created if it does not exist. */
  private static final String OUTPUT_DIRECTORY = "C:\\temp\\FARM\\cra_import_file_exports";

  /** Participants whose latest CRA scenario's import file we want. */
  private static final int[] PINS = {
      98765829, 98765826, 98765823
  };

  /**
   * Finds, per client, the newest CRA scenario (by year, then program year version,
   * then scenario number), and returns the non-empty import file behind it. The %s is
   * replaced with the comma separated {@link #PINS}.
   */
  private static final String SELECT_IMPORT_FILES =
      "WITH sc_ids AS ("
      + " SELECT Sv.Agristability_Scenario_Id,"
      + "        Sv.Participant_Pin,"
      + "        Iv.Import_Version_Id,"
      + "        First_Value(Sv.Agristability_Scenario_Id) Over("
      + "                    PARTITION BY Sv.Agristability_Client_Id"
      + "                    ORDER BY Sv.Year DESC,"
      + "                             Sv.Program_Year_Version_Id DESC,"
      + "                             Sv.Scenario_Number DESC"
      + "                ) sc_Id"
      + " FROM Farm_Scenarios_Vw Sv"
      + " JOIN Farm_Program_Year_Versions Pyv ON Pyv.Program_Year_Version_Id = Sv.Program_Year_Version_Id"
      + " JOIN Farm_Import_Versions Iv ON Iv.Import_Version_Id = Pyv.Import_Version_Id"
      + " WHERE Sv.Participant_Pin IN (%s)"
      + " AND Sv.Scenario_Class_Code = 'CRA'"
      + " AND dbms_lob.getlength(Iv.Import_File) > 0"
      + "), imp_ids AS ("
      + " SELECT UNIQUE Iv.Import_Version_Id"
      + " FROM sc_ids Sci"
      + " JOIN Farm_Import_Versions Iv ON Iv.Import_Version_Id = Sci.Import_Version_Id"
      + " WHERE Sci.Agristability_Scenario_Id = Sci.sc_Id"
      + ")"
      + " SELECT Iv.Import_Version_Id, Iv.Import_File_Name, Iv.Import_File"
      + " FROM imp_ids Ii"
      + " JOIN Farm_Import_Versions Iv ON Iv.Import_Version_Id = Ii.Import_Version_Id";

  private static final int BUFFER_SIZE = 8192;

  private static Connection conn = null;

  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openProdConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }

  @Disabled
  @Test
  public final void downloadLatestFileForPins() throws Exception {
    File outputDirectory = new File(OUTPUT_DIRECTORY);
    if (!outputDirectory.exists() && !outputDirectory.mkdirs()) {
      throw new Exception("Unable to create output directory " + outputDirectory.getAbsolutePath());
    }

    String sql = String.format(SELECT_IMPORT_FILES, pinList());
    int fileCount = 0;

    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement(sql);
      rs = stmt.executeQuery();

      while (rs.next()) {
        long importVersionId = rs.getLong("Import_Version_Id");
        String importFileName = rs.getString("Import_File_Name");
        Blob importFile = rs.getBlob("Import_File");

        if (importFile == null) {
          logger.warn("Import version {} has a null import file, skipped.", Long.valueOf(importVersionId));
          continue;
        }

        File file = outputFile(outputDirectory, importVersionId, importFileName);
        long bytes = writeBlobToFile(importFile, file);
        fileCount++;

        logger.info("Import version {} -> {} ({} bytes)",
            new Object[] { Long.valueOf(importVersionId), file.getAbsolutePath(), Long.valueOf(bytes) });
      }
    } finally {
      close(rs);
      close(stmt);
    }

    logger.info("Wrote {} file(s) to {}", Integer.valueOf(fileCount), outputDirectory.getAbsolutePath());
    assertTrue(fileCount > 0, "The query returned no import files.");
  }

  /** Copies the blob to the given file, returning the number of bytes written. */
  private long writeBlobToFile(Blob blob, File file) throws Exception {
    long total = 0;
    InputStream in = null;
    OutputStream out = null;
    try {
      in = blob.getBinaryStream();
      assertNotNull(in);
      out = new FileOutputStream(file);

      byte[] buffer = new byte[BUFFER_SIZE];
      int read = in.read(buffer);
      while (read > -1) {
        out.write(buffer, 0, read);
        total += read;
        read = in.read(buffer);
      }
      out.flush();
    } finally {
      close(in);
      close(out);
    }
    return total;
  }

  /**
   * Uses the IMPORT_FILE_NAME as-is where possible, falling back to the import version
   * id when the name is missing, and appending it when two versions share a name so
   * that nothing is silently overwritten.
   */
  private File outputFile(File directory, long importVersionId, String importFileName) {
    String name = importFileName == null ? "" : importFileName.trim();

    // strip any path and the characters Windows will not accept in a file name
    name = name.replace('\\', '/');
    int lastSlash = name.lastIndexOf('/');
    if (lastSlash > -1) {
      name = name.substring(lastSlash + 1);
    }
    name = name.replaceAll("[<>:\"/\\\\|?*]", "_").trim();

    if (name.length() == 0) {
      name = "import_" + importVersionId + ".dat";
    }

    File file = new File(directory, name);
    if (file.exists()) {
      int dot = name.lastIndexOf('.');
      String stem = dot > 0 ? name.substring(0, dot) : name;
      String extension = dot > 0 ? name.substring(dot) : "";
      file = new File(directory, stem + "_" + importVersionId + extension);
    }
    return file;
  }

  /** The PINS as a comma separated list for the IN clause. */
  private String pinList() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < PINS.length; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(PINS[i]);
    }
    return sb.toString();
  }

  private void close(AutoCloseable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (Exception ex) {
      // ignore
      ex.toString();
    }
  }

}
