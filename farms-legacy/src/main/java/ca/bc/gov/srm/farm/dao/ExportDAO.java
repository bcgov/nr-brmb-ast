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
package ca.bc.gov.srm.farm.dao;

import static ca.bc.gov.srm.farm.service.ReportService.REPORT_STA;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.service.ExportService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.impl.ExportServiceFactory;
import ca.bc.gov.webade.dbpool.WrapperConnection;
import oracle.jdbc.driver.OracleDriver;

/**
 * @author dzwiers
 */
public final class ExportDAO {

  /** Connection. */
  private Connection conn = null;
  
  @SuppressWarnings("unused")
  private Connection neverUse = null;

  private Logger logger = LoggerFactory.getLogger(ExportDAO.class);

  /**
   * @param pConn
   *          Connection
   */
  public ExportDAO(final Connection pConn) {
    neverUse = pConn;
    if (pConn instanceof WrapperConnection) {
      WrapperConnection wc = (WrapperConnection) pConn;
      this.conn = wc.getWrappedConnection();
    } else {
      this.conn = pConn;
    }

    OracleDriver od = new OracleDriver();
    logger.info("Oracle Driver: " + od.getMajorVersion() + "."
        + od.getMinorVersion());
  }

  
  private static final String PACKAGE_NAME = "FARM_EXPORT_PKG";
  
  private static final String ANALYTICAL_DATA_EXTRACT_PROC_SUFFIX = "_ADE";
  
  private static final String STATEMENT_A_PROC_SUFFIX = "_STATEMENT_A";

  private static final String F01_PROC = "F01";
  private static final int F01_PARAM = 1;

  private static final String F02_PROC = "F02";
  private static final int F02_PARAM = 1;

  private static final String F03_PROC = "F03";
  private static final int F03_PARAM = 1;

  private static final String F04_PROC = "F04";
  private static final int F04_PARAM = 1;

  private static final String F05_PROC = "F05";
  private static final int F05_PARAM = 1;

  private static final String F07_PROC = "F07";
  private static final int F07_PARAM = 1;

  private static final String F08_PROC = "F08";
  private static final int F08_PARAM = 1;

  private static final String F09_PROC = "F09";
  private static final int F09_PARAM = 1;

  private static final String F20_PROC = "F20";
  private static final int F20_PARAM = 1;

  private static final String F21_PROC = "F21";
  private static final int F21_PARAM = 1;

  private static final String F25_PROC = "F25";
  private static final int F25_PARAM = 0;

  private static final String F26_PROC = "F26";
  private static final int F26_PARAM = 0;

  private static final String F30_PROC = "F30";
  private static final int F30_PARAM = 1;

  private static final String F31_PROC = "F31";
  private static final int F31_PARAM = 1;

  private static final String F40_PROC = "F40";
  private static final int F40_PARAM = 1;

  private static final String F60_PROC = "F60";
  private static final int F60_PARAM = 1;
  
  private static final String DETAILED_SCENARIO_EXTRACT_PROC = "DETAILED_SCENARIO_EXTRACT";
  private static final int DETAILED_SCENARIO_EXTRACT_PARAM = 1;

  /**
   * @param zos ZipOutputStream
   * @param name String
   * @param pProgramYear pProgramYear
   * @param exportType exportType
   * @param file99Records file99Records
   * @param userAccountName userAccountName
   * @param exportDate 
   * @throws IOException IOException
   * @throws SQLException SQLException
   */
  @SuppressWarnings({ "resource", "null" })
  public void addZipEntry(ZipOutputStream zos,
      String name,
      Integer pProgramYear,
      String exportType,
      List<String[]> file99Records,
      String filePrefix,
      String csvSuffix,
      String userAccountName,
      String exportDate) throws IOException, SQLException {
    OutputStreamWriter osw = new OutputStreamWriter(zos);
    CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
    if(exportType.equals(ReportService.REPORT_AAFMA)) {
      setADEFormats(writer);
    }

    String fileName = filePrefix + name + csvSuffix;
    ZipEntry entry = new ZipEntry(fileName);
    zos.putNextEntry(entry);

    boolean includeColumnNames = !REPORT_STA.equals(exportType);
    String[] columnHeadings = null;

    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    int c = 1;
    
    try {
      if(name.equals(ExportService.FILE_01)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F01_PROC, exportType), F01_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_01)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F01_PROC, exportType), F01_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_02)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F02_PROC, exportType), F02_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_03)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F03_PROC, exportType), F03_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_04)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F04_PROC, exportType), F04_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_05)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F05_PROC, exportType), F05_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_07)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F07_PROC, exportType), F07_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_08)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F08_PROC, exportType), F08_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_09)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F09_PROC, exportType), F09_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_20)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F20_PROC, exportType), F20_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_21)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F21_PROC, exportType), F21_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_25)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F25_PROC, exportType), F25_PARAM, true);
      } else if(name.equals(ExportService.FILE_26)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F26_PROC, exportType), F26_PARAM, true);
      } else if(name.equals(ExportService.FILE_30)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F30_PROC, exportType), F30_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_31)) {
        columnHeadings = getFile31ColumnHeadings();
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F31_PROC, exportType), F31_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_40)) {
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F40_PROC, exportType), F40_PARAM, true);
        proc.setInt(c++, pProgramYear);
      } else if(name.equals(ExportService.FILE_60)) {
        columnHeadings = getFile60ColumnHeadings();
        proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + getProcName(F60_PROC, exportType), F60_PARAM, true);
        proc.setInt(c++, pProgramYear);
      }

      proc.execute();

      rs = proc.getResultSet();

      if(columnHeadings != null) {
        includeColumnNames = false;
        writer.writeNext(columnHeadings);
      }

      int recordCount = writer.writeAll(rs, includeColumnNames, null);

      writer.flush();
      osw.flush();
      
      zos.closeEntry();

      file99Records.add(new String[]{
          exportDate,
          name,
          Integer.toString(recordCount),
          userAccountName,
          ExportServiceFactory.STATEMENT_A_EXTRACT_SENT_TO
          });

    } finally {
      if (rs != null) {
        rs.close();
      }
      if (proc != null) {
        proc.close();
      }
    }
    
    logger.debug("addEntry: " + fileName);
  }

  /**
   * @param writer writer
   */
  private void setADEFormats(CSVWriter writer) {
    HashMap<Integer, Format> dataTypeFormats = new HashMap<>(CSVWriter.DEFAULT_FORMATS);
    DecimalFormat df = new DecimalFormat("#0.##");
    dataTypeFormats.put(new Integer(Types.DECIMAL), df);
    dataTypeFormats.put(new Integer(Types.DOUBLE), df);
    dataTypeFormats.put(new Integer(Types.FLOAT), df);
    dataTypeFormats.put(new Integer(Types.REAL), df);
    dataTypeFormats.put(new Integer(Types.NUMERIC), df);
    writer.setDataTypeFormats(dataTypeFormats);
    
    HashMap<String, Format> columnFormatsByName = new HashMap<>();
    columnFormatsByName.put("PRODUCTIVE_CAPACITY_AMOUNT", new DecimalFormat("#0.##"));
    writer.setColumnFormatsByName(columnFormatsByName);
  }

  /**
   * @param procBaseName procBaseName
   * @param exportType
   * @return String
   */
  private String getProcName(String procBaseName, String exportType) {
    if(exportType.equals(ReportService.REPORT_AAFM)) {
      return procBaseName;
    } else if(exportType.equals(ReportService.REPORT_AAFMA)) {
      return procBaseName + ANALYTICAL_DATA_EXTRACT_PROC_SUFFIX;
    } else if(exportType.equals(ReportService.REPORT_STA)) {
      return procBaseName + STATEMENT_A_PROC_SUFFIX;
    }
    return null;
  }
  
  /**
   * @return column headings
   */
  private String[] getFile31ColumnHeadings() {
    return new String[]{
        "PARTICIPANT_PIN",
        "PROGRAM_YEAR",
        "SCENARIO_NUMBER",
        "PRIOR_YEAR",
        "UNADJUSTED_PRODUCTION_MARGIN",
        "STRUCTURAL_CHANGE_ADJS",
        "PRODUCTION_MARG_AFT_STR_CHANGS",
        "PRODUCTION_MARG_ACCR_ADJS",
        "Accrued PY Allowable Expenses",
        "Ratio/Additive Method Expense Structural Adjustment",
        "RML_Indicator"
    };
  }
  
  /**
   * @return column headings
   */
  private String[] getFile60ColumnHeadings() {
    return new String[]{
        "PARTICIPANT_PIN",
        "PROGRAM_YEAR",
        "PRODUCTION_MARGINS",
        "ACCRUAL_ADJS_CROP_INVENTORY",
        "ACCRUAL_ADJS_LVSTCK_INVENTORY",
        "OUTSTANDING_FEES",
        "PROGRAM_YEAR_PAYMENTS_RECEIVED",
        "PRODUCER_SHARE",
        "STRUCTURAL_CHANGE_ADJS",
        "UNADJUSTED_REFERENCE_MARGIN",
        "Reference Margin for Benefit Calculation",
        "TIER2_BENEFIT",
        "TIER3_BENEFIT",
        "TIER4_BENEFIT",
        "ACCRUAL_ADJS_PAYABLES",
        "ACCRUAL_ADJS_RECEIVABLES",
        "ACCRUAL_ADJS_PURCHASED_INPUTS",
        "PROD_INSUR_DEEMED_BENEFIT",
        "COMBINED_FARM_PERCENTAGE"
    };
  }

  /**
   * @param fos ZipOutputStream
   * @param pProgramYear pProgramYear
   * @throws IOException IOException
   * @throws SQLException SQLException
   */
  @SuppressWarnings({ "resource" })
  public void detailedScenarioExtract(FileOutputStream fos,
      Integer pProgramYear) throws IOException, SQLException {
    logger.debug("<detailedScenarioExtract");
    
    OutputStreamWriter osw = new OutputStreamWriter(fos);
    CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
    setDetailedScenarioExtractFormats(writer);

    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    int c = 1;
    
    try {
      proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + DETAILED_SCENARIO_EXTRACT_PROC, DETAILED_SCENARIO_EXTRACT_PARAM, true);
      proc.setInt(c++, pProgramYear);

      proc.execute();

      rs = proc.getResultSet();

      boolean includeColumnNames = true;
      writer.writeAll(rs, includeColumnNames, null);

      writer.flush();
      osw.flush();
      
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (proc != null) {
        proc.close();
      }
    }
    
    logger.debug(">detailedScenarioExtract");
  }
  
  private void setDetailedScenarioExtractFormats(CSVWriter writer) {
    HashMap<Integer, Format> dataTypeFormats = new HashMap<>(CSVWriter.DEFAULT_FORMATS);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    dataTypeFormats.put(new Integer(Types.TIMESTAMP), sdf);
    
    writer.setDataTypeFormats(dataTypeFormats);
  }

}
