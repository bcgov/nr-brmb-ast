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
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.service.ImportController;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  dzwiers
 */
public class RunImportController {
  
  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.commit();
      conn.close();
    }
  }

  @Disabled
  @Test
  public final void testCreateVersion() throws SQLException {
    String fn = "xxxxxxxxxxx.zip";

    VersionDAO dao = new VersionDAO(conn);
    Integer i = dao.createVersion("UNITTEST", "Description", fn);
    conn.commit();
    
    ImportController ic = ImportControllerFactory.getInstance(conn, null);

//  File f = new File("M:\\MAL - CAIS Analysis\\Project Documentation\\Quality Assurance\\Test Data\\AARM\\"+fn);
  File f = new File("D:\\CVS_REPO\\MALBCFARM\\java\\test\\data\\"+fn);
//    File f = new File(".\\java\\test\\data\\"+fn);
    ic.importCSV(i,f, "TEST");
    assertNotNull(i);
    
    conn.commit();
    
    ic.processImport(i, "TEST");
    
    conn.commit();
    
    
//    i = dao.createVersion("User", "Description", fn);
//    conn.commit();
//
//    ic.importCSV(i,f,null, "TEST");
//    assertNotNull(i);
//    
//    conn.commit();
//    
//    ic.processImport(i, "TEST");
//    
//    conn.commit();
  }
}
