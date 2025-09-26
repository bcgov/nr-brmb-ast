/**
 *
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.service.TipReportService;

/**
 * @author awilkinson
 */
public final class TipBenchmarkExtractDAO extends OracleDAO {

  private Logger logger = LoggerFactory.getLogger(TipBenchmarkExtractDAO.class);

  private static final String PACKAGE_NAME = "FARM_TIP_USER_PKG";
  
  private static final String READ_TIP_BENCHMARK_YEARS_PROC = "Read_Tip_Benchmark_Years";
  private static final String READ_TIP_BENCHMARK_EXPENSES_PROC = "Read_Tip_Benchmark_Expenses";
  private static final String READ_TIP_INDIVIDUAL_DATA_PROC = "Read_Tip_Individual_Data";
  private static final String READ_TIP_INDIVIDUAL_EXPENSES_PROC = "Read_Tip_Individual_Expenses";

  private static final DecimalFormat INTEGER_FORMAT = new DecimalFormat("#");
  private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#0.00##");
  private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#0.000#");
  private static final DecimalFormat RATIO_FORMAT = new DecimalFormat("#0.000#");
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm a");
  
  public Path exportDataToFile(Connection conn,
      String name,
      Integer programYear,
      String farmType3Name,
      String farmType2Name,
      String farmType1Name,
      Double incomeRangeLow,
      Double incomeRangeHigh) throws IOException, SQLException {
    LoggingUtils.logMethodStart(logger);
    
    Path outFilePath = Files.createTempFile(name, ".csv");

    String procName;
    final int paramCount = 6;
    Format[] columnFormats;
    if(name.equals(TipReportService.BENCHMARK_YEARS)) {
      procName = READ_TIP_BENCHMARK_YEARS_PROC;
      columnFormats = getBenchmarkYearsFormats();
    } else if(name.equals(TipReportService.BENCHMARK_EXPENSES)) {
      procName = READ_TIP_BENCHMARK_EXPENSES_PROC;
      columnFormats = getBenchmarkExpensesFormats();
    } else if(name.equals(TipReportService.INDIVIDUAL_DATA)) {
      procName = READ_TIP_INDIVIDUAL_DATA_PROC;
      columnFormats = getIndividualDataFormats();
    } else { // if(name.equals(TipReportService.INDIVIDUAL_EXPENSES)) {
      procName = READ_TIP_INDIVIDUAL_EXPENSES_PROC;
      columnFormats = getIndividualExpensesFormats();
    }
    
    try(DAOStoredProcedure proc = new DAOStoredProcedure(conn, PACKAGE_NAME + "." + procName, paramCount, true);
        FileWriter osw = new FileWriter(outFilePath.toFile());) {
      
      CSVWriter writer = new CSVWriter(osw, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
      
      // All procs have the same parameters
      int index = 1;
      proc.setInt(index++, programYear);
      proc.setString(index++, farmType3Name);
      proc.setString(index++, farmType2Name);
      proc.setString(index++, farmType1Name);
      proc.setDouble(index++, incomeRangeLow);
      proc.setDouble(index++, incomeRangeHigh);
      proc.execute();

      try(ResultSet rs = proc.getResultSet(); ) {
        
        int recordCount = writer.writeAll(rs, true, columnFormats);
        logger.debug(">exportDataToFile " + name + " - recordCount: " + recordCount);
        
        writer.flush();
        osw.flush();
      }
      
    }
    
    LoggingUtils.logMethodEnd(logger);
    return outFilePath;
  }
  
  private Format[] getBenchmarkYearsFormats() {
    
    return new Format[] {
        INTEGER_FORMAT,  // Program_Year
        INTEGER_FORMAT,  // Reference_Year,
        null,            // Farm_Type_3_Name,
        null,            // Farm_Type_2_Name,
        null,            // Farm_Type_1_Name,
        INTEGER_FORMAT,  // Income_Range_Low,
        INTEGER_FORMAT,  // Income_Range_High,
        INTEGER_FORMAT,  // Tip_Benchmark_Year_Id,
        INTEGER_FORMAT,  // Minimum_Group_Count,
        INTEGER_FORMAT,  // Farming_Operation_Count,
        INTEGER_FORMAT,  // Reference_Year_Count,
        CURRENCY_FORMAT, // Total_Income,
        CURRENCY_FORMAT, // Allowable_Income,
        CURRENCY_FORMAT, // Allowable_Expenses,
        CURRENCY_FORMAT, // Allowable_Expenses_Per_100,
        CURRENCY_FORMAT, // Allowable_Expenses_Per_100_5yr,
        CURRENCY_FORMAT, // Non_Allowable_Income,
        CURRENCY_FORMAT, // Non_Allowable_Expenses,
        CURRENCY_FORMAT, // Non_Allowable_Expenses_Per_100,
        CURRENCY_FORMAT, // Non_Allowable_Expn_Per_100_5yr,
        CURRENCY_FORMAT, // Allowable_Income_Per_100,
        CURRENCY_FORMAT, // Allowable_Income_Per_100_5year,
        CURRENCY_FORMAT, // Other_Farm_Income_Per100,
        CURRENCY_FORMAT, // Other_Farm_Income_Per100_5year,
        CURRENCY_FORMAT, // Total_Expenses_Per_100,
        CURRENCY_FORMAT, // Total_Expenses_Per_100_5year,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Rpay_Bnft_Per_100,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Rpay_Per_100_5yr,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Per100_High_25pct,
        CURRENCY_FORMAT, // Non_Allowbl_Repay_Pgm_Bnft_100,
        CURRENCY_FORMAT, // Non_Allw_Rpay_Pgm_Bnft_100_5yr,
        CURRENCY_FORMAT, // Non_Allw_Rpay_Pgm_Bnf_100_H25p,
        CURRENCY_FORMAT, // Net_Margin,
        CURRENCY_FORMAT, // Net_Margin_Per_100,
        CURRENCY_FORMAT, // Net_Margin_Per_100_5year,
        CURRENCY_FORMAT, // Production_Margin,
        CURRENCY_FORMAT, // Production_Margin_Per_100,
        CURRENCY_FORMAT, // Production_Margin_Per_100_5yr,
        RATIO_FORMAT,    // Production_Margin_Ratio,
        RATIO_FORMAT,    // Prodtn_Margin_Ratio_Low_25pct,
        CURRENCY_FORMAT, // Operating_Cost,
        RATIO_FORMAT,    // Operating_Cost_Ratio,
        RATIO_FORMAT,    // Operatng_Cost_Ratio_High_25pct,
        CURRENCY_FORMAT, // Direct_Expenses,
        RATIO_FORMAT,    // Direct_Expenses_Ratio,
        RATIO_FORMAT,    // Direct_Expens_Ratio_High_25pct,
        CURRENCY_FORMAT, // Machinery_Expenses,
        RATIO_FORMAT,    // Machinery_Expenses_Ratio,
        RATIO_FORMAT,    // Machnry_Expns_Ratio_High_25pct,
        CURRENCY_FORMAT, // Land_Build_Expenses,
        RATIO_FORMAT,    // Land_Build_Expenses_Ratio,
        RATIO_FORMAT,    // Land_Bld_Expn_Ratio_High_25pct,
        CURRENCY_FORMAT, // Overhead_Expenses,
        RATIO_FORMAT,    // Overhead_Expenses_Ratio,
        RATIO_FORMAT,    // Overhead_Expn_Ratio_High_25pct,
        DATE_FORMAT,     // Generated_Date
    };
  }
  
  private Format[] getBenchmarkExpensesFormats() {
    
    return new Format[] {
        INTEGER_FORMAT,  // Program_Year,
        INTEGER_FORMAT,  // Reference_Year,
        null,            // Farm_Type_3_Name,
        null,            // Farm_Type_2_Name,
        null,            // Farm_Type_1_Name,
        INTEGER_FORMAT,  // Income_Range_Low,
        INTEGER_FORMAT,  // Income_Range_High,
        INTEGER_FORMAT,  // Tip_Benchmark_Year_Id,
        DATE_FORMAT,     // Generated_Date,
        INTEGER_FORMAT,  // Line_Item,
        null,            // Description,
        null,            // Eligibility_Ind,
        CURRENCY_FORMAT, // Amount,
        CURRENCY_FORMAT, // Amount_Per_100,
        CURRENCY_FORMAT, // Amount_Per_100_High_25_Pct,
        CURRENCY_FORMAT, // Amount_Per_100_5_Year_Average
        INTEGER_FORMAT,  // Reference_Year_Count
    };
  }
  
  private Format[] getIndividualDataFormats() {
    
    return new Format[] {
        INTEGER_FORMAT,  // Participant_Pin,
        null,            // Client_Name,
        INTEGER_FORMAT,  // Tip_Report_Result_Id,
        null,            // Is_Reference_Year,
        INTEGER_FORMAT,  // Parent_Tip_Report_Result_Id Parent_Id,
        INTEGER_FORMAT,  // Year,
        null,            // Alignment_Key,
        INTEGER_FORMAT,  // Operation_Number,
        INTEGER_FORMAT,  // Partnership_Pin,
        null,            // Partnership_Name,
        new DecimalFormat("#0.000#"), // Partnership_Percent,
        null,            // Farm_Type_3_Name,
        null,            // Farm_Type_2_Name,
        null,            // Farm_Type_1_Name,
        PERCENT_FORMAT,  // Farm_Type_3_Percent,
        PERCENT_FORMAT,  // Farm_Type_2_Percent,
        PERCENT_FORMAT,  // Farm_Type_1_Percent,
        null,            // Farm_Type_3_Threshold_Met_Ind,
        null,            // Farm_Type_2_Threshold_Met_Ind,
        null,            // Farm_Type_1_Threshold_Met_Ind,
        INTEGER_FORMAT,  // Farm_Type_Level,
        INTEGER_FORMAT,  // Income_Range_Low,
        INTEGER_FORMAT,  // Income_Range_High,
        INTEGER_FORMAT,  // Group_Farming_Operation_Count,
        INTEGER_FORMAT,  // Type_3_Tip_Benchmark_Year_Id,
        INTEGER_FORMAT,  // Type_2_Tip_Benchmark_Year_Id,
        INTEGER_FORMAT,  // Type_1_Tip_Benchmark_Year_Id,
        INTEGER_FORMAT,  // Type_3_Income_Range_Low,
        INTEGER_FORMAT,  // Type_3_Income_Range_High,
        INTEGER_FORMAT,  // Type_2_Income_Range_Low,
        INTEGER_FORMAT,  // Type_2_Income_Range_High,
        INTEGER_FORMAT,  // Type_1_Income_Range_Low,
        INTEGER_FORMAT,  // Type_1_Income_Range_High,
        INTEGER_FORMAT,  // Type_3_Minimum_Group_Count,
        INTEGER_FORMAT,  // Type_2_Minimum_Group_Count,
        INTEGER_FORMAT,  // Type_1_Minimum_Group_Count,
        INTEGER_FORMAT,  // Reference_Year_Count,
        CURRENCY_FORMAT, // Total_Income,
        CURRENCY_FORMAT, // Total_Expenses,
        CURRENCY_FORMAT, // Allowable_Income,
        CURRENCY_FORMAT, // Allowable_Expenses,
        CURRENCY_FORMAT, // Non_Allowable_Income,
        CURRENCY_FORMAT, // Non_Allowable_Expenses,
        CURRENCY_FORMAT, // Commodity_Income,
        CURRENCY_FORMAT, // Commodity_Purchases,
        CURRENCY_FORMAT, // Allowable_Repaymnt_Prgm_Benfts,
        CURRENCY_FORMAT, // Non_Allowable_Repay_Prgm_Bnfts,
        CURRENCY_FORMAT, // Commdity_Purchases_Repay_Bnfts,
        CURRENCY_FORMAT, // Total_Income_Benchmark,
        CURRENCY_FORMAT, // Allowable_Income_Per_100,
        CURRENCY_FORMAT, // Allowable_Income_Per_100_5year,
        CURRENCY_FORMAT, // Allowable_Income_Per_100_Bench,
        CURRENCY_FORMAT, // Other_Farm_Income_Per100,
        CURRENCY_FORMAT, // Other_Farm_Income_Per100_5year,
        CURRENCY_FORMAT, // Other_Farm_Income_Per100_Bench,
        CURRENCY_FORMAT, // Allowable_Expenses_Per_100,
        CURRENCY_FORMAT, // Allowable_Expens_Per_100_5year,
        CURRENCY_FORMAT, // Allowable_Expens_Per_100_Bench,
        CURRENCY_FORMAT, // Non_Allowable_Expenses_Per_100,
        CURRENCY_FORMAT, // Non_Allowabl_Expns_Per_100_5yr,
        CURRENCY_FORMAT, // Non_Allowabl_Expns_Per_100_Bch,
        CURRENCY_FORMAT, // Production_Margin_Per_100,
        CURRENCY_FORMAT, // Production_Margin_Per_100_5yr,
        CURRENCY_FORMAT, // Production_Margin_Per_100_Bch,
        CURRENCY_FORMAT, // Total_Expenses_Per_100,
        CURRENCY_FORMAT, // Total_Expenses_Per_100_5year,
        CURRENCY_FORMAT, // Total_Expenses_Per_100_Bench,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Rpay_Bnft_100,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Rpay_Bnft_100_5yr,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Rpay_Bnft_100_Bch,
        CURRENCY_FORMAT, // Cmmdty_Prchs_Per100_High_25pct,
        CURRENCY_FORMAT, // Non_Allowbl_Repay_Pgm_Bnft_100,
        CURRENCY_FORMAT, // Non_Allw_Rpay_Pgm_Bnft_100_5yr,
        CURRENCY_FORMAT, // Non_Allw_Rpay_Pgm_Bnft_100_Bch,
        CURRENCY_FORMAT, // Non_Allw_Rpay_Pgm_Bnf_100_H25p,
        CURRENCY_FORMAT, // Production_Margin,
        RATIO_FORMAT,    // Production_Margin_Ratio,
        CURRENCY_FORMAT, // Production_Margin_Ratio_5year,
        CURRENCY_FORMAT, // Production_Margin_Ratio_Bench,
        CURRENCY_FORMAT, // Prodtn_Margin_Ratio_Low_25pct,
        CURRENCY_FORMAT, // Net_Margin,
        CURRENCY_FORMAT, // Net_Margin_Per_100,
        CURRENCY_FORMAT, // Net_Margin_Per_100_5yr,
        CURRENCY_FORMAT, // Net_Margin_Per_100_Bench,
        CURRENCY_FORMAT, // Operating_Cost,
        RATIO_FORMAT,    // Operating_Cost_Ratio,
        RATIO_FORMAT,    // Operating_Cost_Ratio_5year,
        RATIO_FORMAT,    // Operating_Cost_Ratio_Bench,
        RATIO_FORMAT,    // Operatng_Cost_Ratio_High_25pct,
        CURRENCY_FORMAT, // Direct_Expenses,
        RATIO_FORMAT,    // Direct_Expenses_Ratio,
        RATIO_FORMAT,    // Direct_Expenses_Ratio_5year,
        RATIO_FORMAT,    // Direct_Expenses_Ratio_Bench,
        RATIO_FORMAT,    // Direct_Expens_Ratio_High_25pct,
        CURRENCY_FORMAT, // Machinery_Expenses,
        RATIO_FORMAT,    // Machinery_Expenses_Ratio,
        RATIO_FORMAT,    // Machinery_Expenses_Ratio_5year,
        RATIO_FORMAT,    // Machinery_Expenses_Ratio_Bench,
        RATIO_FORMAT,    // Machnry_Expns_Ratio_High_25pct,
        CURRENCY_FORMAT, // Land_Build_Expenses,
        RATIO_FORMAT,    // Land_Build_Expenses_Ratio,
        RATIO_FORMAT,    // Land_Build_Expens_Ratio_5year,
        RATIO_FORMAT,    // Land_Build_Expens_Ratio_Bench,
        RATIO_FORMAT,    // Land_Bld_Expn_Ratio_High_25pct,
        CURRENCY_FORMAT, // Overhead_Expenses,
        RATIO_FORMAT,    // Overhead_Expenses_Ratio,
        RATIO_FORMAT,    // Overhead_Expenses_Ratio_5year,
        RATIO_FORMAT,    // Overhead_Expenses_Ratio_Bench,
        RATIO_FORMAT,    // Overhead_Expn_Ratio_High_25pct,
        null,            // Use_For_Benchmarks_Ind,
        null,            // Cmmdty_Prchs_Rpay_b_100_Indctr,
        null,            // Production_Margin_Ratio_Indctr,
        null,            // Operating_Cost_Ratio_Indicator,
        null,            // Direct_Expenses_Ratio_Indicatr,
        null,            // Machinery_Expenses_Ratio_Ratng,
        null,            // Land_Build_Expens_Ratio_Indctr,
        null,            // Overhead_Expenses_Ratio_Indctr,
        DATE_FORMAT,     // Generated_Date
    };
  }
  
  private Format[] getIndividualExpensesFormats() {
    
    return new Format[] {
        INTEGER_FORMAT,  // Participant_Pin,
        null,            // Client_Name,
        INTEGER_FORMAT,  // Year,
        null,            // Is_Reference_Year,
        null,            // Alignment_Key,
        INTEGER_FORMAT,  // Partnership_Pin,
        null,            // Farm_Type_3_Name,
        null,            // Farm_Type_2_Name,
        null,            // Farm_Type_1_Name,
        null,            // Farm_Type_2_Threshold_Met_Ind,
        null,            // Farm_Type_1_Threshold_Met_Ind,
        INTEGER_FORMAT,  // Farm_Type_Level,
        INTEGER_FORMAT,  // Income_Range_Low,
        INTEGER_FORMAT,  // Income_Range_High,
        INTEGER_FORMAT,  // Line_Item,
        null,            // Description,
        null,            // Eligibility_Ind,
        CURRENCY_FORMAT, // Amount,
        CURRENCY_FORMAT, // Amount_Per_100,
        CURRENCY_FORMAT, // Amount_Per_100_5_Year,
        CURRENCY_FORMAT, // Amount_Per_100_Benchmark,
        CURRENCY_FORMAT, // Amount_Per_100_High_25_Pct,
        null,            // Expense_Indicator,
        null,            // Use_For_Benchmarks_Ind,
        INTEGER_FORMAT,  // Tip_Report_Result_Id,
        INTEGER_FORMAT,  // Parent_Tip_Report_Result_Id Parent_Id,
        INTEGER_FORMAT,  // Tip_Report_Expense_Id
        DATE_FORMAT,     // Generated_Date
        INTEGER_FORMAT,  // Reference_Year_Count
    };
  }

}
