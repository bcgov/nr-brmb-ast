/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.io;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.exception.UtilityException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * FileUtility.
 */
public final class FileUtility {

  /** FILE_PREFIX. */
  private static final String FILE_PREFIX = "temp-";

  /** instance. */
  private static FileUtility instance = null;

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /** virusScanDir. */
  private File virusScanDir = null;

  /** Creates a new FileUtility object. */
  private FileUtility() {

  }

  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static FileUtility getInstance() {

    if (instance == null) {
      instance = new FileUtility();
    }

    return instance;
  }

  /**
   * cleanDirectory.
   *
   * @throws  UtilityException  On exception.
   */
  public void cleanDirectory() throws UtilityException {

    if (log.isDebugEnabled()) {
      log.debug("clean fsw files from virus scan/temp directory...");
    }

    initializeDir();

    String[] info = virusScanDir.list();

    for (int i = 0; i < info.length; i++) {
      File n = new File(virusScanDir + File.separator + info[i]);

      if (!n.isFile()) { // skip ., .., other directories, etc.
        continue;
      }

      if (info[i].indexOf(FILE_PREFIX) == -1) { // name doesn't match
        continue;
      }

      delete(n);
    }
  }

  /**
   * close.
   *
   * @param  iostream  The parameter value.
   */
  public void close(final IOStream iostream) {

    try {

      if (iostream != null) {

        if (iostream.getOutputStream() != null) {
          iostream.getOutputStream().flush();
          iostream.getOutputStream().close();
        }

        if (iostream.getInputStream() != null) {
          iostream.getInputStream().close();
        }
      }
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException during close: " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("IOException during close: " + e.getMessage(), e);
    }
  }

  /**
   * createIOStream.
   *
   * @param   filename  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public IOStream createIOStream(final String filename)
    throws UtilityException {
    IOStream result = null;
    File tempFile = null;

    try {

      tempFile = new File(filename);

      if (tempFile.exists()) {
        FileOutputStream out = new FileOutputStream(tempFile);
        FileInputStream in = new FileInputStream(tempFile);
        result = new IOStream(tempFile, in, out);
      }
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException during create: " + e.getMessage(), e);
      throw new UtilityException("Error creating temp file: " + e.getMessage(),
        e);
    }

    return result;
  }

  /**
   * createTemp.
   *
   * @param   extension  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public IOStream createTemp(final String extension) throws UtilityException {
    IOStream result = null;
    File tempFile = null;

    try {

      tempFile = createTempFile(extension);
      result = createIOStream(tempFile.getAbsolutePath());
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException during create: " + e.getMessage(), e);
      throw new UtilityException("Error creating temp file: " + e.getMessage(),
        e);
    } catch (IOException e) {
      log.error("IOException during create: " + e.getMessage(), e);
      throw new UtilityException("Error creating temp file: " + e.getMessage(),
        e);
    }

    return result;
  }

  /**
   * delete.
   *
   * @param   iostream  The parameter value.
   *
   * @return  The return value.
   */
  public boolean delete(final IOStream iostream) {
    boolean result = false;

    close(iostream);

    if (iostream != null) {

      if (iostream.getFile() != null) {
        result = delete(iostream.getFile());
      }
    }

    return result;
  }

  /**
   * delete.
   *
   * @param   file  The parameter value.
   *
   * @return  The return value.
   */
  public boolean delete(final File file) {
    boolean result = false;

    if (file != null) {

      if (log.isDebugEnabled()) {
        log.debug("deleting file: " + file.getAbsolutePath());
      }

      result = file.delete();

      if (!result && log.isWarnEnabled()) {
        log.warn("delete failed: " + file.getAbsolutePath());
      }
    }

    return result;
  }

  /**
   * read.
   *
   * @param   fileToRead    The parameter value.
   * @param   outputStream  The parameter value.
   *
   * @throws  UtilityException  On exception.
   */
  public void read(final File fileToRead, final OutputStream outputStream)
    throws UtilityException {

    if (fileToRead == null) {
      throw new IllegalArgumentException("fileToRead cannot be null.");
    }

    if (!fileToRead.exists()) {
      throw new IllegalArgumentException("fileToRead does not exist.");
    }

    try {
      FileInputStream inputStream = new FileInputStream(fileToRead);
      final int buffersize = 256;
      byte[] buffer = new byte[buffersize];
      int bytesRead = 0;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      inputStream.close();
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException during read: " + e.getMessage(), e);
      throw new UtilityException("Error reading file: " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("IOException during read: " + e.getMessage(), e);
      throw new UtilityException("Error reading file: " + e.getMessage(), e);
    }
  }

  /**
   * write.
   *
   * @param   inputStream  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public File write(final InputStream inputStream) throws UtilityException {
    File result = write(inputStream, ".tmp");

    return result;
  }

  /**
   * writeToFile.
   *
   * @param   inputStream  The parameter value.
   * @param   extension    The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public File write(final InputStream inputStream, final String extension)
    throws UtilityException {
    File tempFile = null;

    try {
      tempFile = createTempFile(extension);

      final int buffersize = 256;
      byte[] buffer = new byte[buffersize];
      int bytesRead = 0;
      OutputStream out = new FileOutputStream(tempFile);

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }

      inputStream.close();
      out.close();
    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException during write: " + e.getMessage(), e);
      throw new UtilityException("Error writing file: " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("IOException during write: " + e.getMessage(), e);
      throw new UtilityException("Error writing file: " + e.getMessage(), e);
    }

    return tempFile;
  }

  /**
   * createTempFile.
   *
   * @param   extension  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  IOException       On exception.
   * @throws  UtilityException  On exception.
   */
  File createTempFile(final String extension) throws IOException,
    UtilityException {
    File result = null;

    initializeDir();

    String ext = extension;

    if (StringUtils.isBlank(ext)) {
      ext = ".tmp";
    } else {

      if (!ext.startsWith(".")) {
        ext = "." + ext;
      }
    }

    result = File.createTempFile(FILE_PREFIX, ext, virusScanDir);

    if (log.isDebugEnabled()) {
      log.debug("created temporary file: " + result.getAbsolutePath());
    }

    return result;
  }

  /**
   * initializeDir.
   *
   * @throws  UtilityException  On exception.
   */
  void initializeDir() throws UtilityException {
    String key = ConfigurationKeys.IMPORT_VIRUS_SCAN_DIR;
    String virusDir = ConfigurationUtility.getInstance().getValue(key);

    if (log.isDebugEnabled()) {
      log.debug("Virus Scan Dir = " + virusDir);
    }

    virusScanDir = new File(virusDir);

    if (!virusScanDir.exists()) {

      if (log.isInfoEnabled()) {
        log.info("Virus Scan Dir does not exist, attempting to create it.");
      }

      virusScanDir.mkdir();
    }

    if (!virusScanDir.exists()) {
      String msg = "Virus scan directory (" + virusDir
        + ") does not exist and cannot be created.";
      throw new UtilityException(msg);
    } else if (!virusScanDir.canWrite()) {
      String msg = "Virus scan directory (" + virusDir
        + ") does not allow write.";
      throw new UtilityException(msg);
    }
  }

  /**
   * @return  webadeConnection
   *
   * @throws  UtilityException  On exception
   */
  public File getTempDirectory() throws UtilityException {
    initializeDir();

    return virusScanDir;
  }

}
