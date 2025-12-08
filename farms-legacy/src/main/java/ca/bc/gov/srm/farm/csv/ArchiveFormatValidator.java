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

import ca.bc.gov.srm.farm.domain.staging.Z99ExtractFileCtl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Checks a FileCacheManager instance to ensure the basic integrity of the
 * archive.
 *
 * @author  dzwiers
 */
public class ArchiveFormatValidator {

  /** FileCacheManager. */
  private FileCacheManager cache;

  /**
   * Creates a new ArchiveFormatValidator object.
   *
   * @param  fcm  FileCacheManager
   */
  public ArchiveFormatValidator(final FileCacheManager fcm) {
    this.cache = fcm;
  }

  /** errors. */
  private ArrayList<CSVParserException> errors = null;

  /** warnings. */
  private ArrayList<CSVParserException> warnings = null;

  /**
   * @return  the number of errors, or -1 if the validation has not been
   *          executed.
   */
  public final ArrayList<CSVParserException> getErrors() {
    return errors;
  }

  /**
   * @return  ArrayList
   */
  public final ArrayList<CSVParserException> getWarnings() {
    return warnings;
  }

  /**
   * @param  e  Error to log
   */
  private void addError(final CSVParserException e) {

    if (errors == null) {
      errors = new ArrayList<>();
    }

    errors.add(e);
  }

  /**
   * @param  e  Error to log
   */
  private void addWarning(final CSVParserException e) {

    if (warnings == null) {
      warnings = new ArrayList<>();
    }

    warnings.add(e);
  }

  /**
   * Performs three stages of basic validation; if one stage fails validation
   * terminates. The validation results are accessable from the getters.
   *
   * @return  True when the file validated.
   */
  public final boolean validate() {
    validateFileNames();

    if ((errors != null) && (errors.size() > 0)) {
      return false;
    }

    ArrayList<Z99ExtractFileCtl> z99 = checkAndLoad99();

    if (z99 != null) {
      validateF99FileNumbers(z99);
    }

    if ((errors != null) && (errors.size() > 0)) {
      return false;
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_01_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_02_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_03_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_04_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_05_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_21_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_22_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_23_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_28_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_29_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_40_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_42_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_50_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    try {
      FileHandle fh = cache.read(FileCacheManager.FIPD_51_NAME_PREFIX);
      validate(fh, z99);
    } catch (CSVParserException e) {
      addError(e);
    }

    return (errors == null) || (errors.size() == 0);
  }

  /**
   * @param  fh   FileHandle to check
   * @param  z99  count Expected row count
   */
  private void validate(final FileHandle fh, final ArrayList<Z99ExtractFileCtl> z99) {
    int count = findCount(fh.getFileNumber(), z99);

    try {
      String[] errs = fh.validate();

      if (errs != null) {

        for (int i = 0; i < errs.length; i++) {
          addError(new CSVParserException(fh.getRowsRead(),
              fh.getFileNumber().intValue(), errs[i]));
        }
      }

      int c = 0;

      while (fh.hasNext()) {

        if (fh.next() != null) {
          c++;
        }
      }

      if (c != count) {
        addError(new CSVParserException(0, fh.getFileNumber().intValue(),
            "Row count in file does not match declared count."));
      }
    } catch (RuntimeException re) {
      addError(new CSVParserException(0, fh.getFileNumber().intValue(), re));
    } finally {

      try {
        fh.close();
      } catch (IOException e) {
        addError(new CSVParserException(0, fh.getFileNumber().intValue(), e));
      }
    }
  }

  /**
   * @param   fileNumber  File Number
   * @param   pZ99        Set of File counts by file number as provided.
   *
   * @return  expected count as provided
   */
  private int findCount(final Integer fileNumber, final ArrayList<Z99ExtractFileCtl> pZ99) {

    if (pZ99 == null) {
      return -1;
    }

    for (Z99ExtractFileCtl z : pZ99) {

      if ((z.getExtractFileNumber() != null)
          && z.getExtractFileNumber().equals(fileNumber)) {

        if (z.getRowCount() != null) {
          return z.getRowCount().intValue();
        }
      }
    }

    return 0;
  }

  /**
   * @param  pZ99  Z99ExtractFileCtl
   */
  private void validateF99FileNumbers(final ArrayList<Z99ExtractFileCtl> pZ99) {
    ArrayList<Integer> l = new ArrayList<>();

    for (Z99ExtractFileCtl p : pZ99) {
      l.add(p.getExtractFileNumber());
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_01_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_01_NUMBER,
          "File 01 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_02_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_02_NUMBER,
          "File 02 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_03_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_03_NUMBER,
          "File 03 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_04_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_04_NUMBER,
          "File 04 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_05_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_05_NUMBER,
          "File 05 not found in File 99"));
    }


    if (!l.remove(new Integer(FileCacheManager.FIPD_21_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_21_NUMBER,
          "File 21 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_22_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_22_NUMBER,
          "File 22 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_23_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_23_NUMBER,
          "File 23 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_28_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_28_NUMBER,
          "File 28 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_29_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_29_NUMBER,
          "File 29 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_40_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_40_NUMBER,
          "File 40 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_42_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_42_NUMBER,
          "File 42 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_50_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_50_NUMBER,
          "File 50 not found in File 99"));
    }

    if (!l.remove(new Integer(FileCacheManager.FIPD_51_NUMBER))) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_51_NUMBER,
          "File 51 not found in File 99"));
    }

    for (Iterator<Integer> i = l.iterator(); i.hasNext();) {
      addWarning(new CSVParserException(0, 0,
          "File " + i.next() + " found in File 99"));
    }
  }

  /**  */
  private void validateFileNames() {
    File f = null;

    f = cache.find(FileCacheManager.FIPD_01_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_01_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_01_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_02_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_02_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_02_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_03_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_03_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_03_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_04_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_04_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_04_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_05_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_05_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_05_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_21_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_21_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_21_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_22_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_22_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_22_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_23_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_23_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_23_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_28_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_28_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_28_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_29_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_29_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_29_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_40_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_40_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_40_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_42_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_42_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_42_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_50_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_50_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_50_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_51_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_51_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_51_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }

    f = cache.find(FileCacheManager.FIPD_99_NAME_PREFIX);

    if (f == null) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_99_NUMBER,
          "Missing File: " + FileCacheManager.FIPD_99_NAME_PREFIX
          + "YYYYMMDD.csv"));
    }
  }

  /**
   * @return  ArrayList of Z99ExtractFileCtl
   */
  private ArrayList<Z99ExtractFileCtl> checkAndLoad99() {
    ArrayList<Z99ExtractFileCtl> z99 = new ArrayList<>();
    FileHandle fh = null;

    try {

      try {
        fh = Fipd99.read(cache.find(FileCacheManager.FIPD_99_NAME_PREFIX));
      } catch (CSVParserException e) {
        addError(e);

        return z99;
      }

      String[] errs = fh.validate();

      if (errs != null) {

        for (int i = 0; i < errs.length; i++) {
          addError(new CSVParserException(fh.getRowsRead(),
              fh.getFileNumber().intValue(), errs[i]));
        }
      }

      while (fh.hasNext()) {
        z99.add((Z99ExtractFileCtl)fh.next());
      }

    } catch (RuntimeException re) {
      addError(new CSVParserException(0, FileCacheManager.FIPD_99_NUMBER, re));
    } finally {

      if (fh != null) {

        try {
          fh.close();
        } catch (IOException e) {
          addError(new CSVParserException(0, FileCacheManager.FIPD_99_NUMBER,
              e));
        }
      }
    }

    return z99;
  }
}
