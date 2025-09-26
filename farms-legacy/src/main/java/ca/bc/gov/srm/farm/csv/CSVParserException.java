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


/**
 * Iterator friendly excpetion; to be used when detected parser exceptions of
 * data insertion issues.
 *
 * @author  dzwiers
 */
public class CSVParserException extends Exception {

  private static final long serialVersionUID = 1714642732221545359L;

  /** Row on which the error occured. */
  private int row;

  /** File in which the error occured. */
  private int fileNum;

  /**
   * Creates a new CSVParserException object.
   *
   * @param  r  Row Number
   * @param  f  File Number
   * @param  e  Error
   */
  public CSVParserException(final int r, final int f, final Exception e) {
    super(e);
    this.row = r;
    this.fileNum = f;
  }

  /**
   * Creates a new CSVParserException object.
   *
   * @param  r  Row Number
   * @param  f  File Number
   * @param  s  Error
   */
  public CSVParserException(final int r, final int f, final String s) {
    super(s);
    this.row = r;
    this.fileNum = f;
  }

  /**
   * @return  Row on which the error occured.
   */
  public final int getRowNumber() {
    return row;
  }

  /**
   * @return  File in which the error occured.
   */
  public final int getFileNumber() {
    return fileNum;
  }
}
