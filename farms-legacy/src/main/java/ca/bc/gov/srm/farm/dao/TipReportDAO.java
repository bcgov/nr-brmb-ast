package ca.bc.gov.srm.farm.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.tips.TipBenchmarkGroup;
import ca.bc.gov.srm.farm.domain.tips.TipFarmingOperation;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.transaction.Transaction;

public class TipReportDAO extends OracleDAO {
  private static final String USER_PACKAGE_NAME = "FARMS_TIP_USER_PKG";
  
  private static final String ADMIN_PACKAGE_NAME = "FARMS_TIP_ADMIN_PKG";

  private static final String INSERT_TIP_REPORT_DOCUMENT_PROC = "UPSERT_TIP_REPORT_DOCUMENT";
  
  private static final int INSERT_TIP_REPORT_DOCUMENT_PARAM = 3;

  private static final String UPDATE_PROC = "UPDATE_TIP_REPORT";

  private static final String GET_BLOB_PROC = "GET_TIP_REPORT_BLOB";
  
  private static final String BLOB_UPDATE_PROC = "GET_TIP_REPORT_BLOB_UPD";

  private static final String GET_TIP_REPORT_DOCUMENT_ID_PROC = "GET_TIP_REPORT_DOCUMENT_ID";
  
  private static final String GENERATE_BENCHMARKS_PROC = "GENERATE_BENCHMARKS";
  
  private static final int GENERATE_BENCHMARK_PARAM = 2;
  
  private static final String CHECK_BENCHMARK_GENERATED_PROC = "CHECK_BENCHMARKS_GENERATED";
  
  private static final int CHECK_BENCHMARK_GENERATED_PARAM = 1;
  
  private static final String READ_TIP_FARM_REPORT_PINS_PROC = "READ_TIP_FARM_REPORT_PINS";
  
  private static final String BENCHMARKS_MATCH_CONFIG_PROC = "BENCHMARKS_MATCH_CONFIG";
  
  private static final int BENCHMARKS_MATCH_CONFIG_PARAM = 1;
  
  private static final String UPDATE_TIP_PARTICIPANT_FLAG_PROC = "UPDATE_TIP_PARTICIPANT_FLAG";
  
  private static final String READ_TIP_BENCHMARK_GROUPS_PROC = "READ_TIP_BENCHMARK_GROUPS";
  
  private static final String READ_TIP_BENCHMARK_SUMMARY_REPORT_PROC = "READ_TIP_BENCHMARK_SUMMARY_REPORT";
  
  private static final String READ_TIP_BENCHMARK_SUMMARY_REPORT_EXPENSES_PROC = "READ_TIP_BENCHMARK_SUMMARY_REPORT_EXPENSES";

  private static final String READ_TIP_GROUPING_CONFIG_PROC = "READ_TIP_GROUPING_CONFIG";
  
  private static final String TEMP_GROUPING_CONFIG_REPORT_FILE_NAME_PREFIX = "tipGroupingConfiguration_temp_";
  
  private Logger logger = LoggerFactory.getLogger(TipReportDAO.class);

  private static final DecimalFormat CURRENCY_NO_DECIMAL_FORMAT = new DecimalFormat("$#,##0");
  private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#");
  private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#0.00##");
  private static final DecimalFormat COUNT_FORMAT = new DecimalFormat("#,##0");
  
  public TipReportDAO() {

  }

  /**
   * @param transaction transaction
   * @param userId      userId
   * 
   * @throws DataAccessException on exception
   */
  public final Integer upsertTipReport(final Connection connection, final String userId, final Integer farmingOperationId) throws DataAccessException {
    String procName = USER_PACKAGE_NAME + "." + INSERT_TIP_REPORT_DOCUMENT_PROC;

    DAOStoredProcedure proc = null;

    Integer reportDocId = null;
    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, INSERT_TIP_REPORT_DOCUMENT_PARAM, false);
      
      int index = 1;
      proc.registerOutParameter(index, Types.BIGINT);
      
      proc.setLong(index++, (Long) null);
      proc.setLong(index++, farmingOperationId == null ? null : farmingOperationId.longValue());
      proc.setString(index++, userId);
      proc.execute();
      
      reportDocId = (int)proc.getLong(1);

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

    return reportDocId;
  }
  
  /**
   * @param   transaction    transaction
   * @param   userId  userId
   * @param   reportDocId   reportDocId
   * 
   * @throws  DataAccessException  on exception
   */
  public final void updateTipReport(final Connection connection, final String userId, final Integer reportDocId) throws DataAccessException {
    
    String procName = USER_PACKAGE_NAME + "." + UPDATE_PROC;

    DAOStoredProcedure proc = null;

    final int paramCount = 2;
    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, false);

      int index = 1;
      proc.setLong(index++, reportDocId == null ? null : reportDocId.longValue());
      proc.setString(index++, userId);
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }

  /**
   * @param connection connection
   * @param scenarioId scenarioId
   * @param update     update
   *
   * @return BLOB
   *
   * @throws DataAccessException on exception
   */
  @SuppressWarnings("resource")
  public Blob getTipReportBlob(Connection connection, Integer tipReportDocId, Integer farmingOperationId, boolean isForUpdate)
      throws DataAccessException {
    Blob blob = null;
    DAOStoredProcedure proc = null;
    ResultSet resultSet = null;
    final int paramCount = 2;
    String procName;
    
    if (isForUpdate) {
      procName = USER_PACKAGE_NAME + "." + BLOB_UPDATE_PROC;
    } else {
      procName = USER_PACKAGE_NAME + "." + GET_BLOB_PROC;
    }

    boolean originalAutoCommit = true;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, true);
      
      int index = 1;
      proc.setLong(index++, tipReportDocId == null ? null : tipReportDocId.longValue());
      proc.setLong(index++, farmingOperationId == null ? null : farmingOperationId.longValue());
      proc.execute();
      resultSet = proc.getResultSet();

      if (resultSet.next()) {
        blob = resultSet.getBlob(1);
      }

      connection.commit();
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        ex.addSuppressed(rollbackEx);
      }
      throw new DataAccessException(ex);
    } finally {
      close(resultSet, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

    return blob;
  }

  /**
   * @param transaction transaction
   * @param userId      userId
   * 
   * @throws DataAccessException on exception
   */
  public final Integer getTipReportDocumentId(Connection connection, Integer farmingOperationId) throws DataAccessException {
    String procName = USER_PACKAGE_NAME + "." + GET_TIP_REPORT_DOCUMENT_ID_PROC;
    final int paramCount = 1;

    DAOStoredProcedure proc = null;

    Integer reportDocId = null;
    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, paramCount, Types.BIGINT);
      
      int index = 1;
      
      proc.setLong(index++, farmingOperationId == null ? null : farmingOperationId.longValue());
      proc.execute();
      
      reportDocId = (int)proc.getLong(1);

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

    return reportDocId;
  }
  
  
  @SuppressWarnings("resource")
  public Boolean checkBenchmarkDataGenerated(Transaction transaction, final Integer year)
      throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;
    int paramCount = 1;
    String procName = USER_PACKAGE_NAME + "." + CHECK_BENCHMARK_GENERATED_PROC;
        
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, CHECK_BENCHMARK_GENERATED_PARAM, Types.BIGINT);
      proc.setShort(paramCount++, year == null ? null : year.shortValue());
      proc.execute();
      int benchmarkCount = (int)proc.getLong(1);

      connection.commit();
      
      return benchmarkCount > 0;
      
    } catch (SQLException ex) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        ex.addSuppressed(rollbackEx);
      }
      throw new DataAccessException(ex);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }

  
  public final void generateBenchmarks(final Transaction transaction, final Integer year, String user) throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    String procName = ADMIN_PACKAGE_NAME + "." + GENERATE_BENCHMARKS_PROC;

    DAOStoredProcedure proc = null;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, procName, GENERATE_BENCHMARK_PARAM, false);

      int index = 1;
      proc.setShort(index++, year == null ? null : year.shortValue());
      proc.setString(index++, user);
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  @SuppressWarnings("resource")
  public List<TipFarmingOperation> getTipFarmingOperations(final Transaction transaction, final Integer year) throws DataAccessException {
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    List<TipFarmingOperation> tipFarmingOps = new ArrayList<>();
    final int paramCount = 1;
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, USER_PACKAGE_NAME + "."
          + READ_TIP_FARM_REPORT_PINS_PROC, paramCount, true);
      
      int param = 1;
      proc.setShort(param++, year == null ? null : year.shortValue());
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        TipFarmingOperation tipFarmOp = new TipFarmingOperation();
        tipFarmingOps.add(tipFarmOp);
        
        tipFarmOp.setPin(getInteger(rs, "participant_pin"));
        tipFarmOp.setProducerName(getString(rs, "producer_name"));
        tipFarmOp.setFarmOpId(getInteger(rs, "farming_operation_id"));
        tipFarmOp.setPartnershipPin(getInteger(rs, "partnership_pin"));
        tipFarmOp.setTipReportStatusCode(getString(rs, "tip_report_status"));
        tipFarmOp.setTipReportDocId(getInteger(rs, "tip_report_document_id"));
        tipFarmOp.setIsTipParticipant(getIndicator(rs, "tip_participant_ind"));
        tipFarmOp.setIsAgriStabilityParticipant(getIndicator(rs, "agristability_participant_ind"));
      }

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
    return tipFarmingOps;
  }


  @SuppressWarnings("resource")
  public boolean benchmarksMatchConfig(final Transaction transaction,
      final Integer year)
  throws DataAccessException {
    
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;
    String inUseString;
    boolean result = false;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, USER_PACKAGE_NAME + "."
          + BENCHMARKS_MATCH_CONFIG_PROC, BENCHMARKS_MATCH_CONFIG_PARAM, Types.VARCHAR);

      int param = 1;
      proc.setShort(param++, year == null ? null : year.shortValue());
      proc.execute();

      inUseString = proc.getString(1);
      result = DAOStoredProcedure.YES.equals(inUseString);

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }

    return result;
  }
  
  
  public final void updateTipParticipantFlag(
      Transaction transaction,
      Integer[] participantPins,
      Boolean isTipParticipant,
      String userId)
      throws DataAccessException {
    
    @SuppressWarnings("resource")
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;
    final int paramCount = 3;

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, USER_PACKAGE_NAME + "."
          + UPDATE_TIP_PARTICIPANT_FLAG_PROC, paramCount , false);

      int index = 1;
      Array oracleArray = connection.createArrayOf("numeric", participantPins);
      proc.setArray(index++, oracleArray);
      proc.setIndicator(index++, isTipParticipant);
      proc.setString(index++, userId);
      proc.execute();

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      logSqlException(e);
      handleException(e);
    } finally {
      close(proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
  }
  
  
  @SuppressWarnings("resource")
  public Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> readBenchmarkGroups(final Transaction transaction)
      throws DataAccessException {
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    DAOStoredProcedure proc = null;
    ResultSet rs = null;
    final int paramCount = 0;
    
    Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> groupsByYear = new HashMap<>();
    
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      proc = new DAOStoredProcedure(connection, USER_PACKAGE_NAME + "."
          + READ_TIP_BENCHMARK_GROUPS_PROC, paramCount, true);
      
      proc.execute();
      
      rs = proc.getResultSet();
      
      while (rs.next()) {
        Integer programYear = getInteger(rs, "Program_Year");
        String farmType3Name = getString(rs, "Farm_Type_3_Name");
        String farmType2Name = getString(rs, "Farm_Type_2_Name");
        String farmType1Name = getString(rs, "Farm_Type_1_Name");
        Double incomeRangeLow = getDouble(rs, "Income_Range_Low");
        Double incomeRangeHigh = getDouble(rs, "Income_Range_High");
        
        Integer farmTypeLevel;
        String farmType;
        if(farmType3Name != null) {
          farmTypeLevel = 3;
          farmType = farmType3Name;
        } else if(farmType2Name != null) {
          farmTypeLevel = 2;
          farmType = farmType2Name;
        } else {
          farmTypeLevel = 1;
          farmType = farmType1Name;
        }
        
        Map<Integer, Map<String, List<TipBenchmarkGroup>>> groupsByLevel = groupsByYear.get(programYear);
        if(groupsByLevel == null) {
          groupsByLevel = new HashMap<>();
          groupsByYear.put(programYear, groupsByLevel);
        }
        
        Map<String, List<TipBenchmarkGroup>> groupsByFarmType = groupsByLevel.get(farmTypeLevel);
        if(groupsByFarmType == null) {
          groupsByFarmType = new HashMap<>();
          groupsByLevel.put(farmTypeLevel, groupsByFarmType);
        }
        
        List<TipBenchmarkGroup> farmTypeGroupList = groupsByFarmType.get(farmType);
        if(farmTypeGroupList == null) {
          farmTypeGroupList = new ArrayList<>();
          groupsByFarmType.put(farmType, farmTypeGroupList);
        }
        
        
        TipBenchmarkGroup benchmarkGroup = new TipBenchmarkGroup();
        benchmarkGroup.setProgramYear(programYear);
        benchmarkGroup.setFarmTypeLevel(farmTypeLevel);
        benchmarkGroup.setFarmType(farmType);
        benchmarkGroup.setIncomeRangeLow(incomeRangeLow);
        benchmarkGroup.setIncomeRangeHigh(incomeRangeHigh);
        farmTypeGroupList.add(benchmarkGroup);
      }

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      close(rs, proc);
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
    return groupsByYear;
  }
  
  
  @SuppressWarnings("resource")
  public String readBenchmarkSummaryReport(final Transaction transaction,
      Integer programYearParam,
      String farmType3NameParam,
      String farmType2NameParam,
      String farmType1NameParam,
      Double incomeRangeLowParam)
      throws DataAccessException {
    
    String result = null;
    Connection connection = getConnection(transaction);
    boolean originalAutoCommit = true;
    final int mainProcParamCount = 5;
    
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm a");
    

    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure proc = new DAOStoredProcedure(connection,
          USER_PACKAGE_NAME + "." + READ_TIP_BENCHMARK_SUMMARY_REPORT_PROC,
          mainProcParamCount, true);
          StringWriter osw = new StringWriter();) {
        
        int index = 1;
        
        proc.setShort(index++, programYearParam == null ? null : programYearParam.shortValue());
        proc.setString(index++, farmType3NameParam);
        proc.setString(index++, farmType2NameParam);
        proc.setString(index++, farmType1NameParam);
        proc.setBigDecimal(index++, incomeRangeLowParam == null ? null : BigDecimal.valueOf(incomeRangeLowParam));
        proc.execute();
        
        try(ResultSet mainResultSet = proc.getResultSet(); ) {
        
          if (mainResultSet.next()) {
            final int referenceYearCount = 5;
            int programYear = getInteger(mainResultSet, "Program_Year");
            int referenceYearMinimum = programYear - referenceYearCount;
            int referenceYearMaximum = programYear - 1;
            String farmType3Name = getString(mainResultSet, "Farm_Type_3_Name");
            String farmType2Name = getString(mainResultSet, "Farm_Type_2_Name");
            String farmType1Name = getString(mainResultSet, "Farm_Type_1_Name");
            Double incomeRangeLow = getDouble(mainResultSet, "Income_Range_Low");
            Double incomeRangeHigh = getDouble(mainResultSet, "Income_Range_High");
            String incomeRange = CURRENCY_NO_DECIMAL_FORMAT.format(incomeRangeLow) + " - " + CURRENCY_NO_DECIMAL_FORMAT.format(incomeRangeHigh);
            Integer numberOfFarms = getInteger(mainResultSet, "Farming_Operation_Count");
            Date generatedDate = getDate(mainResultSet, "Generated_Date");
            
            String farmType;
            if(farmType3Name != null) {
              farmType = farmType3Name;
            } else if(farmType2Name != null) {
              farmType = farmType2Name;
            } else {
              farmType = farmType1Name;
            } 
            
            
            CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
            
            // Need quotes around lines that have commas in them.
            // Quoted them all just in case they are modified later.
            
            writer.writeAll(new String[]{
                "Benchmark (BM) Report"
            });
            
            writer.writeNext(new String[]{
                "Farm Type",
                quote(farmType)
            });
            
            writer.writeNext(new String[]{
                "Sales Range",
                quote(incomeRange)
            });
            
            writer.writeNext(new String[]{
                "Number of Farms",
                quote(COUNT_FORMAT.format(numberOfFarms))
            });
            
            writer.writeNext(new String[]{
                "Generated Date",
                dateFormat.format(generatedDate)
            });
            
            writer.writeNext(new String[]{
                "",
                "AgriStability Source",
                "Industry Benchmark (" + referenceYearMinimum + " - " + referenceYearMaximum + ")",
                "Industry Benchmark (" + programYear + ")"
            });
            
            writer.writeNext(new String[]{
                "INCOME - Stated as a % of the GFI"
            });
            
            writer.writeNext(new String[]{
                "AS allowable - Commodity sales and program payments",
                "Total A",
                formatPercent(getDouble(mainResultSet, "Allowable_Income_Per_100_5year")),
                formatPercent(getDouble(mainResultSet, "Allowable_Income_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "AS Non-allowable Income - Other farming income",
                "Total B",
                formatPercent(getDouble(mainResultSet, "Other_Farm_Income_Per100_5year")),
                formatPercent(getDouble(mainResultSet, "Other_Farm_Income_Per100"))
            });
            
            writer.writeNext(new String[]{
                "Total Gross Farming Income (GFI)",
                "(Total A + B)",
                "100",
                "100"
            });
            
            writer.writeNext(new String[]{
                "" // blank line
            });
            
            writer.writeNext(new String[]{
                "EXPENSES - Stated as a % of the GFI"
            });
            
            writer.writeNext(new String[]{
                "AS Allowable Expenses"
            });
            
            // Allowable Expenses
            writeExpenseCodes(
                connection,
                programYearParam,
                writer,
                farmType3NameParam,
                farmType2NameParam,
                farmType1NameParam,
                incomeRangeLowParam,
                true,
                mainResultSet);
            
            writer.writeNext(new String[]{
                "" // blank line
            });
            
            writer.writeNext(new String[]{
                "Total AS Allowable Expenses",
                "",
                formatPercent(getDouble(mainResultSet, "Allowable_Expenses_Per_100_5yr")),
                formatPercent(getDouble(mainResultSet, "Allowable_Expenses_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "Production Margin as a % of AS allowable income (AS Allowable Income - AS Allowable Expenses)",
                "",
                formatPercent(getDouble(mainResultSet, "Production_Margin_Per_100_5yr")),
                formatPercent(getDouble(mainResultSet, "Production_Margin_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "" // blank line
            });
            
            writer.writeNext(new String[]{
                "AS Non-Allowable Expenses"
            });
            
            // Non-Allowable Expenses
            writeExpenseCodes(
                connection,
                programYearParam,
                writer,
                farmType3NameParam,
                farmType2NameParam,
                farmType1NameParam,
                incomeRangeLowParam,
                false,
                mainResultSet);
            
            writer.writeNext(new String[]{
                "" // blank line
            });
            
            writer.writeNext(new String[]{
                "Total AS Non-Allowable Expenses",
                "",
                formatPercent(getDouble(mainResultSet, "Non_Allowable_Expn_Per_100_5yr")),
                formatPercent(getDouble(mainResultSet, "Non_Allowable_Expenses_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "Total Expenses",
                "",
                formatPercent(getDouble(mainResultSet, "Total_Expenses_Per_100_5year")),
                formatPercent(getDouble(mainResultSet, "Total_Expenses_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "Net Margin (GFI - Total Expenses)",
                "",
                formatPercent(getDouble(mainResultSet, "Net_Margin_Per_100_5year")),
                formatPercent(getDouble(mainResultSet, "Net_Margin_Per_100"))
            });
            
            writer.writeNext(new String[]{
                "" // blank line
            });
            
            writer.writeNext(new String[]{
                "* For internal use by Ministry of Agriculture only"
            });
            
            writer.flush();
            osw.flush();
            result = osw.toString();
          }
        }
      }

      connection.commit();
    } catch (SQLException | IOException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      getLog().error("Unexpected error: ", e);
      handleException(e);
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        handleException(ex);
      }
    }
    
    return result;
  }

  private String formatPercent(Double amount) {
    String result = null;
    if(amount != null) {
      result = quote(CURRENCY_FORMAT.format(amount));
    }
    return result;
  }

  private void writeExpenseCodes(
      Connection connection,
      Integer programYearParam,
      CSVWriter writer,
      String farmType3NameParam,
      String farmType2NameParam,
      String farmType1NameParam,
      Double incomeRangeLowParam,
      boolean isAllowableExpense,
      ResultSet mainResultSet) throws SQLException, IOException {
    
    final int expenseProcParamCount = 6;
    
    String[] expenseCodeHeadings = {"Description", "CRA Code"};
    Format[] expenseFormats = {null, INTEGER_FORMAT, CURRENCY_FORMAT, CURRENCY_FORMAT};
    writer.writeNext(expenseCodeHeadings);
    
    // Commodity purchases and repayment of program benefits
    // are an allowable expense listed below the Description and CRA Code headings
    if(isAllowableExpense) {
      writer.writeNext(new String[]{
          "Commodity purchases and repayment of program benefits",
          "",
          formatPercent(getDouble(mainResultSet, "Cmmdty_Prchs_Rpay_Per_100_5yr")),
          formatPercent(getDouble(mainResultSet, "Cmmdty_Prchs_Rpay_Bnft_Per_100"))
      });
    } else {
      writer.writeNext(new String[]{
          "Repayment of program benefits",
          "",
          formatPercent(getDouble(mainResultSet, "Non_Allw_Rpay_Pgm_Bnft_100_5yr")),
          formatPercent(getDouble(mainResultSet, "Non_Allowbl_Repay_Pgm_Bnft_100"))
      });
    }

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try(DAOStoredProcedure expensesProc = new DAOStoredProcedure(connection,
          USER_PACKAGE_NAME + "." + READ_TIP_BENCHMARK_SUMMARY_REPORT_EXPENSES_PROC,
          expenseProcParamCount, true); ) {
        
        int index = 1;
        
        expensesProc.setShort(index++, programYearParam == null ? null : programYearParam.shortValue());
        expensesProc.setString(index++, farmType3NameParam);
        expensesProc.setString(index++, farmType2NameParam);
        expensesProc.setString(index++, farmType1NameParam);
        expensesProc.setBigDecimal(index++, incomeRangeLowParam == null ? null : BigDecimal.valueOf(incomeRangeLowParam));
        expensesProc.setIndicator(index++, isAllowableExpense);
        expensesProc.execute();
        
        try(ResultSet expensesResultSet = expensesProc.getResultSet(); ) {
        
          writer.writeAll(expensesResultSet, false, expenseFormats);
        }
      }

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        connection.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
  }
  
  public Path readGroupingConfigReport(Connection conn) throws IOException, SQLException {
    LoggingUtils.logMethodStart(logger);
    
    Path outFilePath = Files.createTempFile(TEMP_GROUPING_CONFIG_REPORT_FILE_NAME_PREFIX, ".csv");

    final int paramCount = 0;
    Format[] columnFormats = new Format[] {
        null,            // Farm_Type_3_Name,
        null,            // Farm_Type_2_Name,
        null,            // Farm_Type_1_Name,
        INTEGER_FORMAT,  // Income_Range_Low,
        INTEGER_FORMAT,  // Income_Range_High,
        INTEGER_FORMAT,  // Minimum_Group_Count,
        null,            // Inherited_From
    };

    boolean originalAutoCommit = true;
    try {
      originalAutoCommit = conn.getAutoCommit();
      conn.setAutoCommit(false);

      try(DAOStoredProcedure proc =
          new DAOStoredProcedure(conn, USER_PACKAGE_NAME + "." + READ_TIP_GROUPING_CONFIG_PROC, paramCount, true);
          FileWriter osw = new FileWriter(outFilePath.toFile());) {
        
        CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        
        proc.execute();

        try(ResultSet rs = proc.getResultSet(); ) {
          
          int recordCount = writer.writeAll(rs, true, columnFormats);
          logger.debug(">readIncomeRangesReport - recordCount: " + recordCount);
          
          writer.flush();
          osw.flush();
        }
        
      }

      conn.commit();
    } catch (SQLException e) {
      try {
        conn.rollback();
      } catch (SQLException rollbackEx) {
        e.addSuppressed(rollbackEx);
      }
      throw e;
    } finally {
      try {
        conn.setAutoCommit(originalAutoCommit);
      } catch (SQLException ex) {
        throw ex;
      }
    }
    
    LoggingUtils.logMethodEnd(logger);
    return outFilePath;
  }

  private String quote(String incomeRange) {
    return CSVWriter.DEFAULT_QUOTE_CHARACTER + incomeRange + CSVWriter.DEFAULT_QUOTE_CHARACTER;
  }
}
