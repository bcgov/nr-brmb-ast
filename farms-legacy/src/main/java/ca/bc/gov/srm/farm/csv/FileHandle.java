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

import au.com.bytecode.opencsv.CSVReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * CSV File Handle -- takes care of one iteration through the file rows.
 *
 * @author  dzwiers
 */
public abstract class FileHandle implements Iterator<Object> {

  /** Logger. */
  protected static Logger logger = LoggerFactory.getLogger(FileHandle.class);

  /** CSVReader. */
  private CSVReader reader = null;

  /** FileReader. */
  private FileReader fr = null;

  /** Object. */
  private Object current = null;

  /** Exception. */
  protected CSVParserException excep = null;

  /** header. */
  protected String[] header = null;

  /**
   * @param   f  CSV to read
   *
   * @throws  CSVParserException  IOException IO problem exists
   */
  protected FileHandle(final File f) throws CSVParserException {
    logger.info("Starting parse: " + f);

    try {
      fr = new FileReader(f);
    } catch (FileNotFoundException e) {
      throw new CSVParserException(0, getFileNumber().intValue(), e);
    }

    reader = new CSVReader(fr);
  }

  /**
   * @see  java.util.Iterator#remove()
   */
  @Override
  public final void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * @return  boolean
   *
   * @see     java.util.Iterator#hasNext()
   */
  @Override
  public final boolean hasNext() {

    if ((current == null) && (excep == null)) {

      try {
        load();
      } catch (IOException e) {
      	logger.error("Unexpected error: ", e);
        excep = new CSVParserException(row, getFileNumber().intValue(), e);
      } catch (ParseException e) {
      	logger.error("Unexpected error: ", e);
        excep = new CSVParserException(row, getFileNumber().intValue(), e);
      }
    }

    return (current != null) || (excep != null);
  }

  /**
   * @return  Next Fipd Object
   *
   * @see     java.util.Iterator#next()
   */
  @Override
  public final Object next() {

    if ((current == null) && (excep == null)) {

      try {
        load();
      } catch (IOException e) {
      	logger.error("Unexpected error: ", e);
        excep = new CSVParserException(row, getFileNumber().intValue(), e);
      } catch (ParseException e) {
      	logger.error("Unexpected error: ", e);
        excep = new CSVParserException(row, getFileNumber().intValue(), e);
      }
    }

    if (excep != null) {
      CSVParserException e = excep;
      excep = null;
      throw new RuntimeException(e);
    }

    if (current == null) {
      throw new NoSuchElementException();
    } else {
      Object o = current;
      current = null;

      return o;
    }
  }

  /**
   * Closes the reader stream.
   *
   * @throws  IOException  IO problem exists
   */
  public final void close() throws IOException {
    reader = null;
    fr.close();
    fr = null;
  }

  /**
   * @return  The current row number being parsed. Intended for error handling.
   */
  public final int getRowsRead() {
    return row;
  }

  /** current parse row. */
  protected int row = 0;
  
  /**
   * @return  true if the CSV file has a header row.
   */
  protected boolean csvFileHasHeader() {
  	return true;
  }

  /**
   * @throws  IOException     IO problem exists
   * @throws  ParseException  Invalid data condition
   */
  protected void load() throws IOException, ParseException {

    if (reader != null) {
      String[] nextLine = reader.readNext();
      row++;

      if (csvFileHasHeader() && (header == null)) {
        header = nextLine;
        nextLine = reader.readNext();
        row++;
      }

      if (nextLine != null) {
        current = parse(nextLine);
      }
    }
  }

  /**
   * @param   line  The CSV values for the line to parse
   *
   * @return  The parsed FIPD Object
   *
   * @throws  ParseException  Invalid data condition
   */
  protected abstract Object parse(String[] line) throws ParseException;

  /**
   * This method validates the file header.
   *
   * @return  Set of errors when the header in the file does not match the
   *          header in the spec.
   */
  public String[] validate() {
    ArrayList<String> results = new ArrayList<>();
    ArrayList<String> headers = new ArrayList<>(getExpectedHeader());

    // load header
    try {
      load();
    } catch (IOException | ParseException e) {
      logger.error("Unexpected error: ", e);
      excep = new CSVParserException(row, getFileNumber().intValue(), e);
    }

    // compare header
    for (int i = 0; i < header.length; i++) {

      if ((header[i] != null) && !"".equals(header[i].trim())
          && !headers.remove(header[i].trim())) {
        results.add("Extra Header found: " + header[i]);
      }
    }

    for (String curHeader : headers) {
      results.add("Header not found: " + curHeader);
    }

    if (results.size() <= 0) {
      return null;
    }

    return results.toArray(new String[results.size()]);
  }

  /**
   * @return  Ordered list of the expected header names
   */
  protected abstract ArrayList<String> getExpectedHeader();

  /** MISSING_ROW. */
  protected static final int MISSING_ROW = 1;

  /** INVALID_ROW. */
  protected static final int INVALID_ROW = 2;

  /**
   * @param   code    type of data issue
   * @param   offset  offset of error within the line
   *
   * @throws  ParseException  Data issue within the file
   */
  protected final void error(final int code, final int offset)
    throws ParseException {

    switch (code) {

    case MISSING_ROW:
      throw new ParseException("Row " + row
        + " does not contain any data",
        offset);

    case INVALID_ROW:
      throw new ParseException(
        "Row " + row
        + " does not contain the correct number of columns",
        offset);

    default:
      throw new ParseException("Row " + row
        + " has an unknown error.",
        offset);
    }
  }

  /**
   * Gets current.
   *
   * @return  the current
   */
  protected final Object getCurrent() {
    return current;
  }

  /**
   * Sets current.
   *
   * @param  pCurrent  the current to set
   */
  protected final void setCurrent(final Object pCurrent) {
    current = pCurrent;
  }

  /**
   * @return  The File Number
   */
  public abstract Integer getFileNumber();

  /**
   * @return  The File Name
   */
  public abstract String getFileName();
}
