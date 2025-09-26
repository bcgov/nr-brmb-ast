/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Extracts the contents of a zip file to a temporary location and provides
 * access to the temporary files.
 *
 * @author  dzwiers
 */
public class FileCacheManager {

  /** archive. */
  private File archive = null;

  /** extracted. */
  private boolean extracted = false;

  /** handles. */
  private Map<String, File> handles = null;

  /** FIPD_42_NAME_PREFIX. */
  public static final String FIPD_42_NAME_PREFIX = "FIPD_AS_42_";

  /** FIPD_42_NUMBER. */
  public static final int FIPD_42_NUMBER = 42;

  /** FIPD_02_NAME_PREFIX. */
  public static final String FIPD_02_NAME_PREFIX = "FIPD_AS_02_";

  /** FIPD_02_NUMBER. */
  public static final int FIPD_02_NUMBER = 2;

  /** FIPD_04_NAME_PREFIX. */
  public static final String FIPD_04_NAME_PREFIX = "FIPD_AS_04_";

  /** FIPD_04_NUMBER. */
  public static final int FIPD_04_NUMBER = 4;

  /** FIPD_03_NAME_PREFIX. */
  public static final String FIPD_03_NAME_PREFIX = "FIPD_AS_03_";

  /** FIPD_03_NUMBER. */
  public static final int FIPD_03_NUMBER = 3;

  /** FIPD_05_NAME_PREFIX. */
  public static final String FIPD_05_NAME_PREFIX = "FIPD_AS_05_";

  /** FIPD_05_NUMBER. */
  public static final int FIPD_05_NUMBER = 5;

  /** FIPD_21_NAME_PREFIX. */
  public static final String FIPD_21_NAME_PREFIX = "FIPD_AS_21_";

  /** FIPD_21_NUMBER. */
  public static final int FIPD_21_NUMBER = 21;

  /** FIPD_22_NAME_PREFIX. */
  public static final String FIPD_22_NAME_PREFIX = "FIPD_AS_22_";

  /** FIPD_22_NUMBER. */
  public static final int FIPD_22_NUMBER = 22;

  /** FIPD_22_NAME_PREFIX. */
  public static final String FIPD_23_NAME_PREFIX = "FIPD_AS_23_";

  /** FIPD_23_NUMBER. */
  public static final int FIPD_23_NUMBER = 23;

  /** FIPD_28_NAME_PREFIX. */
  public static final String FIPD_28_NAME_PREFIX = "FIPD_AS_28_";

  /** FIPD_28_NUMBER. */
  public static final int FIPD_28_NUMBER = 28;

  /** FIPD_29_NAME_PREFIX. */
  public static final String FIPD_29_NAME_PREFIX = "FIPD_AS_29_";

  /** FIPD_29_NUMBER. */
  public static final int FIPD_29_NUMBER = 29;

  /** FIPD_40_NAME_PREFIX. */
  public static final String FIPD_40_NAME_PREFIX = "FIPD_AS_40_";

  /** FIPD_40_NUMBER. */
  public static final int FIPD_40_NUMBER = 40;

  /** FIPD_50_NAME_PREFIX. */
  public static final String FIPD_50_NAME_PREFIX = "FIPD_AS_50_";

  /** FIPD_50_NUMBER. */
  public static final int FIPD_50_NUMBER = 50;

  /** FIPD_51_NAME_PREFIX. */
  public static final String FIPD_51_NAME_PREFIX = "FIPD_AS_51_";

  /** FIPD_51_NUMBER. */
  public static final int FIPD_51_NUMBER = 51;

  /** FIPD_99_NAME_PREFIX. */
  public static final String FIPD_99_NAME_PREFIX = "FIPD_AS_99_";

  /** FIPD_99_NUMBER. */
  public static final int FIPD_99_NUMBER = 99;

  /** FIPD_01_NAME_PREFIX. */
  public static final String FIPD_01_NAME_PREFIX = "FIPD_AS_01_";

  /** FIPD_01_NUMBER. */
  public static final int FIPD_01_NUMBER = 1;

  /**
   * Creates a new FileCacheManager object.
   *
   * @param  zipFile    FIPD Zip File
   */
  public FileCacheManager(final File zipFile) {
    this.archive = zipFile;
  }

  /**
   * @param   tempDir  Temporary Directory to store the zip file contents in.
   *
   * @throws  IOException  IOException
   */
  public final void extract(final File tempDir) throws IOException {

    if (extracted) {
      return;
    }

    handles = new HashMap<>();

    if ((tempDir != null) && (!tempDir.exists() || !tempDir.isDirectory())) {
      throw new IOException(
        "Directory either does not exist or is not a directory: " + tempDir);
    }

    @SuppressWarnings("resource")
    ZipFile zip = null;

    try {
      zip = new ZipFile(archive, ZipFile.OPEN_READ);

      for (Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries(); entries.hasMoreElements();) {
        ZipEntry entry = entries.nextElement();

        if ((entry != null) && !entry.isDirectory()) {
          String name = entry.getName();
          File f = File.createTempFile(archive.getName(), ".csv", tempDir);

          @SuppressWarnings("resource")
          BufferedWriter writer = null;
          BufferedReader reader = null;

          try {
            writer = new BufferedWriter(new FileWriter(f));
            reader = new BufferedReader(new InputStreamReader(
                  zip.getInputStream(entry)));

            while (reader.ready()) {

              // can use '\n' because these are text files
              writer.write(reader.readLine() + "\n");
            }

            writer.flush();
          } finally {

            if (writer != null) {
              writer.close();
            }

            if (reader != null) {
              reader.close();
            }
          }

          handles.put(name, f);
        }
      }

      extracted = true;
    } finally {

      if (zip != null) {
        zip.close();
      }
    }
  }

  /**
   * @param   namePrefix  file prefix to search for
   *
   * @return  FileHandle if extracted and found.
   *
   * @throws  CSVParserException  IOException
   */
  public final FileHandle read(final String namePrefix)
    throws CSVParserException {

    if (!extracted || (namePrefix == null)) {
      return null;
    }

    File f = null;

    for (Iterator<String> i = handles.keySet().iterator();
        (f == null) && i.hasNext();) {
      String n = i.next();

      if ((n != null) && n.toUpperCase().startsWith(namePrefix.toUpperCase())) {
        f = handles.get(n);
      }
    }

    if (f == null) {
      return null;
    }

    // figure out which file handle to create
    if (FIPD_01_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd01.read(f);
    }

    if (FIPD_02_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd02.read(f);
    }

    if (FIPD_03_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd03.read(f);
    }

    if (FIPD_04_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd04.read(f);
    }

    if (FIPD_05_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd05.read(f);
    }

    if (FIPD_21_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd21.read(f);
    }

    if (FIPD_22_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd22.read(f);
    }

    if (FIPD_23_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd23.read(f);
    }

    if (FIPD_28_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd28.read(f);
    }

    if (FIPD_29_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd29.read(f);
    }

    if (FIPD_40_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd40.read(f);
    }

    if (FIPD_42_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd42.read(f);
    }

    if (FIPD_50_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd50.read(f);
    }

    if (FIPD_51_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd51.read(f);
    }

    if (FIPD_99_NAME_PREFIX.equalsIgnoreCase(namePrefix)) {
      return Fipd99.read(f);
    }

    return null;
  }

  /**
   * @param   namePrefix  file prefix to search for
   *
   * @return  File if extracted and found.
   */
  final File find(final String namePrefix) {

    if (!extracted || (namePrefix == null)) {
      return null;
    }

    File f = null;

    for (Iterator<String> i = handles.keySet().iterator();
        (f == null) && i.hasNext();) {
      String n = i.next();

      if ((n != null) && n.toUpperCase().startsWith(namePrefix.toUpperCase())) {
        f = handles.get(n);
      }
    }

    return f;
  }

  /**
   * @return  Clone of the file names.
   */
  final String[] list() {
    return handles.keySet().toArray(
        new String[handles.keySet().size()]);
  }
}
