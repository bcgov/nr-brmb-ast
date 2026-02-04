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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.service.ExportService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  dzwiers
 */
public class TestExportService {
  
  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    TestUtils.closeConnection(conn);
  }

  @Test
  public void testExport() throws SQLException, IOException {
    
    Integer year = new Integer(2008);
    ExportService service = ExportServiceFactory.getInstance(conn);
    ExportServiceFactory.setClean(false);
    
    String user = this.getClass().getSimpleName();
    File f = service.exportNumberedFilesZip(year, ReportService.REPORT_AAFMA, user);
    
    System.out.println(f);
  }
}
