/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.fmv;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.TestUtils;

/**
 * @author awilkinson
 * @created Aug 21, 2019
 */
public class TestFmvRead {

  private static Connection conn = null;
  
  @BeforeAll
  protected static void setUp() throws Exception {
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  @Disabled
  @Test
  public final void loadFmvsWithNoApples() throws Exception {
    int pin = 23331481;
    int year = 2017;
    Integer scenarioNumber = Integer.valueOf(2);
    
    String expectedScenarioString = TestUtils.loadFileAsString("data/fmv/Scenario1WithoutApples.json");
    assertNotNull(expectedScenarioString);
    
    TestUtils.useTestTransaction();

    CalculatorService service = ServiceFactory.getCalculatorService();
    Scenario scenario = service.loadScenario(pin, year, scenarioNumber);
    assertNotNull(scenario);

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    String scenarioLoadedFromDatabaseString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scenario);
    assertNotNull(scenarioLoadedFromDatabaseString);

    assertEquals(expectedScenarioString, scenarioLoadedFromDatabaseString);
  }
  
  @Disabled
  @Test
  public final void loadFmvsWithApples() throws Exception {
    int pin = 25823717;
    int year = 2018;
    Integer scenarioNumber = Integer.valueOf(2);
    
    String expectedScenarioString = TestUtils.loadFileAsString("data/fmv/Scenario1WithApples.json");
    assertNotNull(expectedScenarioString);
    
    TestUtils.useTestTransaction();
    
    CalculatorService service = ServiceFactory.getCalculatorService();
    Scenario scenario = service.loadScenario(pin, year, scenarioNumber);
    assertNotNull(scenario);
    
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    String scenarioLoadedFromDatabaseString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scenario);
    assertNotNull(scenarioLoadedFromDatabaseString);
    
    assertEquals(expectedScenarioString, scenarioLoadedFromDatabaseString);
  }

}
