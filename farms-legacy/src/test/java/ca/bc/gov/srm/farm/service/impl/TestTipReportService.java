/**
 *
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 */
public class TestTipReportService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static Connection conn;
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    conn = TestUtils.openConnection();
  }
  
  
  @Disabled
  @Test
  public void scheduleBackgroundReportJob(){
  	ImportService service = ServiceFactory.getImportService();
  	
  	List<String> lines = new ArrayList<>();
  	lines.add("447685");
  	
  	try {
      File tipReportJobFile = IOUtils.writeTempFile("tipJobParameters", ".csv", null, lines);
  		try(FileInputStream fis = new FileInputStream(tipReportJobFile);) {
  		
    		@SuppressWarnings("unused")
        ImportVersion importVersion = service.createImportVersion(
    				"TIP_REPORT",
    				ImportStateCodes.SCHEDULED_FOR_STAGING,
        		"Test description", 
        		"Test filename",
        		fis,
        		"TEST");
  		}
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }
  
  @Disabled
  @Test
  public void runTipReports() {
  	ImportService service = ServiceFactory.getImportService();
  	
  	try {
  		
  		service.checkForScheduledImport(conn);
  		conn.commit();
      
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }
  
  
  @Disabled
  @Test
  public void testScheduleAndRun(){
    scheduleBackgroundReportJob();
    runTipReports();
  }
  
  @Test
  public void runTipReportsForOperationIds() {
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    
    try {
      
      String farmingOperationIdsString = "515477,515238,515390,512513,521761,508653,507303";
      int importVersionId = 123456789;
      tipReportService.generateReports(farmingOperationIdsString , importVersionId, "UNIT_TEST", conn);
      conn.commit();
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  @Test
  public void downloadReports() {
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    
    try {
      
      String farmingOperationIdsString = "527053,527095,527120,";
      Path zipPath = tipReportService.downloadReports(farmingOperationIdsString);
      logger.debug("zip path: " + zipPath);
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  @Test
  public void runBenchmarkSummaryReport() {
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    
    try {
        
      String reportCsv = tipReportService.getBenchmarkSummaryReport(2019, "Fruits", null, null, 100001.00);
      
      logger.debug("reportCsv:\n" + reportCsv);
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  @Test
  public void runBenchmarkExtractReport() {
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    
    try {
      
      tipReportService.generateBenchmarkExtract(2019, null, null, null, null, null);
      Path benchmarkExtractZipFile = tipReportService.getBenchmarkExtractZipFile();
      
      logger.debug("benchmarkExtractZipFile:\n" + benchmarkExtractZipFile);
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

}
