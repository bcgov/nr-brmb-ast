/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.io;

import ca.bc.gov.srm.farm.exception.UtilityException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * IOStream.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class IOStream {

  /** file. */
  private File file = null;

  /** inputStream. */
  private FileInputStream inputStream = null;

  /** outputStream. */
  private FileOutputStream outputStream = null;

  /**
   * Creates a new IOStream object.
   *
   * @param  aFile         Input parameter to initialize class.
   * @param  aInputStream  Input parameter to initialize class.
   */
  IOStream(final File aFile, final FileInputStream aInputStream) {
    this.file = aFile;
    this.inputStream = aInputStream;
  }

  /**
   * Creates a new IOStream object.
   *
   * @param  aFile          Input parameter to initialize class.
   * @param  aOutputStream  Input parameter to initialize class.
   */
  IOStream(final File aFile, final FileOutputStream aOutputStream) {
    this.file = aFile;
    this.outputStream = aOutputStream;
  }

  /**
   * Creates a new IOStream object.
   *
   * @param  aFile          Input parameter to initialize class.
   * @param  aInputStream   Input parameter to initialize class.
   * @param  aOutputStream  Input parameter to initialize class.
   */
  IOStream(final File aFile, final FileInputStream aInputStream,
    final FileOutputStream aOutputStream) {
    this.file = aFile;
    this.inputStream = aInputStream;
    this.outputStream = aOutputStream;
  }

  /**
   * closeInputStream.
   *
   * @throws  UtilityException  On exception.
   */
  public void closeInputStream() throws UtilityException {

    try {

      if (inputStream != null) {
        inputStream.close();
      }
    } catch (IOException e) {
      throw new UtilityException("Error closing input stream: "
        + e.getMessage(), e);
    }
  }

  /**
   * closeOutputStream.
   *
   * @throws  UtilityException  On exception.
   */
  public void closeOutputStream() throws UtilityException {

    try {

      if (outputStream != null) {
        outputStream.close();
      }
    } catch (IOException e) {
      throw new UtilityException("Error closing output stream: "
        + e.getMessage(), e);
    }

  }

  /**
   * getExtension.
   *
   * @return  The return value.
   */
  public String getExtension() {
    String result = null;

    if (file != null) {
      String fs = file.toString();
      int place = fs.lastIndexOf('.');

      if (place >= 0) {
        result = fs.substring(place + 1);
      }
    }

    return result;
  }

  /**
   * getFile.
   *
   * @return  The return value.
   */
  public File getFile() {
    return file;
  }

  /**
   * getInputStream.
   *
   * @return  The return value.
   */
  public InputStream getInputStream() {
    return inputStream;
  }

  /**
   * getOutputStream.
   *
   * @return  The return value.
   */
  public OutputStream getOutputStream() {
    return outputStream;
  }
}
