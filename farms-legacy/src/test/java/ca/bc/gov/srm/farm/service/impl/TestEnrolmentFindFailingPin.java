/**
 *
 * Copyright (c) 2013,
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.TestUtils;


/**
 * @author  awilkinson
 */
public class TestEnrolmentFindFailingPin {
  
  private Logger logger = LoggerFactory.getLogger(TestEnrolmentFindFailingPin.class);

  /**
   * Supply a list of PINs. Put a debug point in the catch block of the
   * EnrolmentWriteDAO.updateStaging() method. If an SQLException is thrown
   * it will be caught there.
   */
  @Disabled
  @Test
  public final void findFailingPin() {

    String pinsString =
        "22295554";
    

    Integer enrolmentYear = new Integer(2013);

    String[] pins = pinsString.split(",");
    String cuPin = null;
    
    try {
    
      for(int i = 0; i < pins.length; i++) {
        cuPin = pins[i];
  
        try(Connection conn = TestUtils.openConnection();) {
          // easier to just use an existing enrolment import version
          Integer importVersionId = new Integer(5767);
          
          File enrolmentFile = createEnrolmentFile(cuPin, enrolmentYear);
          
          EnrolmentServiceImpl enrService = new EnrolmentServiceImpl();
          enrService.generateEnrolmentsStaging(conn, importVersionId, enrolmentFile, "ENROLMENT_SERVICE_TEST");
          
          enrolmentFile.delete();
          conn.rollback();
        }
      }

    } catch (Exception e) {
      logger.error("PIN failed: " + cuPin);
      logger.error("Exception message: " + e.getMessage());
      e.printStackTrace();
    }
  }


  private File createEnrolmentFile(String pinsString, Integer enrolmentYear)
      throws IOException, FileNotFoundException {
    File pTempDir = IOUtils.getTempDir(); 

    File outFile = File.createTempFile("farm_enrolment_", ".csv", pTempDir);
    try (FileOutputStream fos = new FileOutputStream(outFile);
         PrintWriter pw = new PrintWriter(fos);) {
      
      pw.println(enrolmentYear.toString());
      pw.println(pinsString);
      pw.flush();
      pw.close();
    }
    
    return outFile;
  }
}
