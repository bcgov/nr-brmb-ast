package ca.bc.gov.srm.farm.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import ca.bc.gov.srm.farm.domain.tips.TipBenchmarkGroup;
import ca.bc.gov.srm.farm.domain.tips.TipFarmingOperation;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface TipReportService {
  
  String REPORT_TYPE_BENCHMARK_EXTRACT = "BENCHMARK_EXTRACT";
  String REPORT_TYPE_BENCHMARK_SUMMARY = "BENCHMARK_SUMMARY";
  String REPORT_TYPE_GROUPING_CONFIG = "GROUPING_CONFIG";
  
  String BENCHMARK_YEARS = "benchmark_years";
  String BENCHMARK_EXPENSES = "benchmark_expenses";
  String INDIVIDUAL_DATA = "individual_data";
  String INDIVIDUAL_EXPENSES = "individual_expenses";

  void writeTipReportToResponse(Integer tipReportDocId, HttpServletResponse response) throws Exception;

  void processScheduledReportGeneration(Connection connection, File file, Integer importVersionId, String userId) throws ServiceException;

  Integer getTipReportDocumentId(Integer farmingOperationId) throws Exception;
  
  void scheduleTipReportGeneration(String farmingOperationIdsString, Integer year, String reportState) throws IOException, FileNotFoundException, ServiceException;

  void generateBenchmarkData(Integer year, String user) throws ServiceException;
  
  void generateReports(String farmingOperationIdsString, Integer importVersionId, String userId) throws ServiceException;

  void generateReports(String farmingOperationIdsString, Integer importVersionId, String userId, Connection connection) throws Exception;
  
  Boolean checkBenchmarkDataGenerated(Integer year) throws ServiceException;

  List<TipFarmingOperation> getTipFarmingOperations(Integer year)  throws ServiceException;
  
  Path getTipReportZipFile();
  
  Path downloadReports(String farmingOperationIdsString) throws Exception;
  
  boolean benchmarksMatchConfig(Integer year)  throws ServiceException;
  
  void updateTipParticipantFlag(
      Collection<Integer> participantPins,
      Boolean isTipParticipant,
      String userId) throws ServiceException;
  
  Map<Integer, Map<Integer, Map<String, List<TipBenchmarkGroup>>>> getBenchmarkGroups() throws ServiceException;

  String getBenchmarkSummaryReport(
      Integer programYearParam,
      String farmType3NameParam,
      String farmType2NameParam,
      String farmType1NameParam,
      Double incomeRangeLowParam) throws ServiceException;

  void generateBenchmarkExtract(
      Integer programYear,
      String farmType3Name,
      String farmType2Name,
      String farmType1Name,
      Double incomeRangeLow,
      Double incomeRangeHigh) throws ServiceException;

  Path getBenchmarkExtractZipFile();
  
  Path generateGroupingConfigReport() throws ServiceException;
}
