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
package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ListIterator;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

/**
 * @author awilkiinson
 */
public class SerializeScenarioAsJson {

  private static Logger logger = LoggerFactory.getLogger(SerializeScenarioAsJson.class);

  private static Connection conn = null;
  
  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    conn = TestUtils.openConnection();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  @Test
  public void unmarshalScenarioToJson() throws Exception {
    int pin = 23260011;
    int year = 2021;
    Integer scenarioNumber = 2;
    
    TestUtils.useTestTransaction();

    CalculatorService service = ServiceFactory.getCalculatorService();
    Scenario scenario = service.loadScenario(pin, year, scenarioNumber);
    assertNotNull(scenario);
    
    if(scenario.isInCombinedFarm()) {
      for(ListIterator<Scenario> iterator = scenario.getCombinedFarm().getScenarios().listIterator(); iterator.hasNext(); ) {
        Scenario curScenario = iterator.next();
        curScenario.setCombinedFarm(null);
      }
    }

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    String scenarioString = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scenario);
    
    logger.info("scenarioString:\n" + scenarioString);
    
    Scenario scenarioFromString = jsonObjectMapper.readValue(scenarioString, Scenario.class);
    assertNotNull(scenarioFromString);

  }

}
