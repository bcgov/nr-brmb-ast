/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;

import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.transaction.Transaction;

/**
 * @author awilkinson
 * @created Mar 28, 2011
 */
public class ReportDAO extends OracleDAO {

  private static final String PACKAGE_NAME = "FARM_REPORT_PKG";
  
  private static final String NATIONAL_SURVEILLANCE_STRATEGY_PROC = "NATIONAL_SURVEILLANCE_STRATEGY";
  private static final int NATIONAL_SURVEILLANCE_STRATEGY_PARAM = 1;
  
  private static final String ANALYTICAL_SURVEILLANCE_STRATEGY_PROC = "ANALYTICAL_SURVEILLANCE_STRATEGY";
  private static final int ANALYTICAL_SURVEILLANCE_STRATEGY_PARAM = 1;
  

  public void surveillanceStrategy(Transaction transaction,
      String reportType,
      Integer year,
      String[] headerLines,
      String[] columnHeadings,
      Format[] columnFormats,
      File outputFile)
      throws IOException, DataAccessException {
    @SuppressWarnings("resource")
    Connection connection = getOracleConnection(transaction);
    
    String surveillanceStrategyProc = null;
    int surveillanceStrategyParam = 0;
    if(reportType.equals(ReportService.REPORT_NATIONAL_SURVEILLANCE_STRATEGY)) {
      surveillanceStrategyProc = NATIONAL_SURVEILLANCE_STRATEGY_PROC;
      surveillanceStrategyParam = NATIONAL_SURVEILLANCE_STRATEGY_PARAM;
    } else {
      surveillanceStrategyProc = ANALYTICAL_SURVEILLANCE_STRATEGY_PROC;
      surveillanceStrategyParam = ANALYTICAL_SURVEILLANCE_STRATEGY_PARAM;
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(connection,
        PACKAGE_NAME + "." + surveillanceStrategyProc, surveillanceStrategyParam, true);) {
        
      int param = 1;
      proc.setInt(param++, year);
      proc.execute();
      
      try(ResultSet rs = proc.getResultSet();) {
        CSVWriter.writeOutputFile(headerLines, columnHeadings, columnFormats, outputFile, rs);
      }
        
    } catch (SQLException e) {
      getLog().error("Unexpected error: ", e);
      handleException(e);
    }
  }

}
