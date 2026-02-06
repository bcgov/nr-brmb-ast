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
package ca.bc.gov.srm.farm.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.domain.dataimport.StagingResults;
import ca.bc.gov.srm.farm.util.TestUtils;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEApplicationUtils;

public class TestImportService {
  
  public static final String IMPORT_FILE_DIRECTORY = "E:\\FARM\\Import\\CRA\\";
  public static final String IMPORT_FILE_NAME = "cra_import_98765739_2024.zip";
  public static final String IMPORT_FILE_PATH = IMPORT_FILE_DIRECTORY + IMPORT_FILE_NAME;


  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.useTestTransaction();
    TestUtils.setBusinessActionToSystem();
  }
  
  
  @Test
  public void runQueuedImport() {
    ImportService service = ServiceFactory.getImportService();
    ConfigurationService configService = ServiceFactory.getConfigurationService();
    
    try {
      configService.loadConfigurationParameters();
      configService.loadYearConfigurationParameters();
      
      Application app = WebADEApplicationUtils.createApplication("FARM");
      WebADERequest.getInstance().setApplication(app);
      
      try(Connection conn = TestUtils.openConnection();) {
      
        service.checkForScheduledImport(conn);
        conn.commit();
      }
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }

  @Disabled
  @Test
  public void insertImportVersion(){
    ImportService service = ServiceFactory.getImportService();
    
    try {
      File zipFile = new File(IMPORT_FILE_PATH);
      try(FileInputStream fis = new FileInputStream(zipFile);) {
      
        @SuppressWarnings("unused")
        ImportVersion importVersion = service.createImportVersion(
            "CRA",
            ImportStateCodes.SCHEDULED_FOR_STAGING,
            IMPORT_FILE_NAME, 
            IMPORT_FILE_PATH,
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
  public void stagingDetails(){
  	Integer importVersionId = new Integer(7);
  	ImportService service = ServiceFactory.getImportService();
  	
  	try {
	  	StagingResults sd = service.getStagingResults(importVersionId);
	  	
	  	assertEquals(22, sd.getNumberOfItems());
	  	assertEquals(3, sd.getErrors().size());
	  	assertEquals(0, sd.getWarnings().size());
	  	assertEquals("SF", sd.getImportVersion().getImportStateCode());
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }

  @Disabled
  @Test
  public void startupCheck(){
  	ImportService service = ServiceFactory.getImportService();
  	
  	try {

      try(Connection conn = TestUtils.openConnection();) {
        
    		service.checkForInProgessImports(conn);
    		
    		conn.commit();
      }
      
  	} catch(Exception ex) {
  		ex.printStackTrace();
  		fail(ex.getMessage());
  	}
  }
  
  
  @Disabled
  @Test
  public void enrolment() throws ServiceException {
    ImportService service = ServiceFactory.getImportService();
    TestUtils.overrideServices();
    
    try {
      Application app = WebADEApplicationUtils.createApplication("FARM");
      WebADERequest.getInstance().setApplication(app);
      
      try(Connection conn = TestUtils.openConnection();) {
        
        service.checkForScheduledImport(conn);
        conn.commit();
      }
      
    } catch(Exception ex) {
      ex.printStackTrace();
      fail(ex.getMessage());
    }
  }
  
  @Disabled
  @Test
  public void insertAndImport(){
    insertImportVersion();
    runQueuedImport();
  }

  
  @Disabled
  @Test
  public void run(){
    ImportService service = ServiceFactory.getImportService();
    
    try {
    	  // need this for preferences etc...
    	  Application app = WebADEApplicationUtils.createApplication("FARM");
  		  WebADERequest.getInstance().setApplication(app);
  		
        File zipFile = new File(IMPORT_FILE_PATH);
        try(FileInputStream fis = new FileInputStream(zipFile);) {
        
          ImportVersion importVersion = service.createImportVersion(
        		"CRA",
        		ImportStateCodes.SCHEDULED_FOR_STAGING,
              "Test description", 
              "Test filename",
              fis,
              "TEST");
          
          try(Connection conn = TestUtils.openConnection();) {
            service.checkForScheduledImport(conn);
            conn.commit();
          }
          
          service.confirmImport(importVersion.getImportVersionId());
          
          try(Connection conn = TestUtils.openConnection();) {
            service.checkForScheduledImport(conn);
            conn.commit();
          }
        }
        
    } catch(Exception ex) {
        ex.printStackTrace();
        fail(ex.getMessage());
    }
  }

}
