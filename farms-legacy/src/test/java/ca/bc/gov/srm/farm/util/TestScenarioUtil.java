/**
 * Copyright (c) 2011,
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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 * @created Feb 23, 2013
 */
public class TestScenarioUtil {


  @Test
  public final void isInPartnershipFalse() throws Exception {

    Scenario scenario = buildScenario(1);
    
    // null partner list, null partnership pin, name
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertFalse(inPartnership);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    List<FarmingOperationPartner> partners = new ArrayList<>();
    op1.setFarmingOperationPartners(partners);
    
    // empty partner list
    inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertFalse(inPartnership);
    
    // partnershipPin zero
    op1.setPartnershipPin(0);
    inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertFalse(inPartnership);
    
    // partnershipName empty string
    op1.setPartnershipName("");
    inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertFalse(inPartnership);
    
    // partnershipPercent 100%
    op1.setPartnershipPercent(1.0);
    inPartnership = ScenarioUtils.isInPartnership(scenario);
  }
  
  
  @Test
  public final void isInPartnershipTrueOnePartner() throws Exception {
    
    Scenario scenario = buildScenario(1);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    List<FarmingOperationPartner> partners = new ArrayList<>();
    op1.setFarmingOperationPartners(partners);
    
    FarmingOperationPartner op1Partner1 = new FarmingOperationPartner();
    partners.add(op1Partner1);
    op1.setFarmingOperationPartners(partners);
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertTrue(inPartnership);
  }
  
  
  @Test
  public final void isInPartnershipTrueTwoPartners() throws Exception {
    
    Scenario scenario = buildScenario(1);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    List<FarmingOperationPartner> partners = new ArrayList<>();
    op1.setFarmingOperationPartners(partners);
    
    FarmingOperationPartner op1Partner1 = new FarmingOperationPartner();
    partners.add(op1Partner1);
    op1.setFarmingOperationPartners(partners);
    
    FarmingOperationPartner op1Partner2 = new FarmingOperationPartner();
    partners.add(op1Partner2);
    op1.setFarmingOperationPartners(partners);
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertTrue(inPartnership);
  }
  
  
  @Test
  public final void isInPartnershipTrueHasPartnershipPin() throws Exception {
    
    Scenario scenario = buildScenario(1);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    op1.setPartnershipPin(12345678);
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertTrue(inPartnership);
  }
  
  
  @Test
  public final void isInPartnershipTrueHasPartnershipName() throws Exception {
    
    Scenario scenario = buildScenario(1);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    op1.setPartnershipName("asdf");
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertTrue(inPartnership);
  }
  
  
  @Test
  public final void isInPartnershipTrueHasPartnershipPercent() throws Exception {
    
    Scenario scenario = buildScenario(1);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    FarmingOperation op1 = farmingOperations.get(0);
    
    op1.setPartnershipPercent(0.5);
    
    boolean inPartnership = ScenarioUtils.isInPartnership(scenario);
    assertTrue(inPartnership);
  }


  private Scenario buildScenario(int numberOfOperations) {
    Scenario scenario = new Scenario();
    FarmingYear farmingYear = new FarmingYear();
    scenario.setFarmingYear(farmingYear);
    List<FarmingOperation> farmingOperations = new ArrayList<>();
    
    for(int i = 0; i < numberOfOperations; i++) {
      FarmingOperation op = new FarmingOperation();
      farmingOperations.add(op);
    }
    
    farmingYear.setFarmingOperations(farmingOperations);
    
    return scenario;
  }

}
