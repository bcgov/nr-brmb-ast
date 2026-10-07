/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.enrolment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.service.ConfigurationService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

public class VerificationEnrolmentCalculatorTest {

  private Field configurationServiceField;
  private ConfigurationService previousConfigurationService;


  @BeforeEach
  public void useEnhancedBenefitsForFixture() throws Exception {
    configurationServiceField = ServiceFactory.class.getDeclaredField("configurationService");
    configurationServiceField.setAccessible(true);
    previousConfigurationService = (ConfigurationService) configurationServiceField.get(null);

    ConfigurationService configurationService = (ConfigurationService) Proxy.newProxyInstance(
        ConfigurationService.class.getClassLoader(),
        new Class<?>[] {ConfigurationService.class},
        (proxy, method, args) -> {
          if("getValue".equals(method.getName())) {
            return "Y";
          }
          throw new UnsupportedOperationException(method.getName());
        });
    configurationServiceField.set(null, configurationService);
  }


  @AfterEach
  public void restoreConfigurationService() throws Exception {
    configurationServiceField.set(null, previousConfigurationService);
  }


  @Test
  public void shouldCalculatePyPlusTwoEnrolmentFromScenarioReferenceMargin() throws Exception {
    Scenario scenario = createScenario();
    VerificationEnrolmentCalculator calculator =
        EnrolmentCalculatorFactory.getVerificationEnrolmentCalculator();

    Enrolment sameYearEnrolment = calculator.calculateEnrolment(scenario);
    Enrolment enrolment = calculator.calculateEnrolment(scenario, scenario.getYear() + 2);

    assertEquals(Integer.valueOf(2023), enrolment.getEnrolmentYear());
    assertEquals(
        scenario.getFarmingYear().getBenefit().getEnhancedReferenceMarginForBenefitCalculation(),
        enrolment.getContributionMarginAverage());
    assertEquals(186.39, enrolment.getEnrolmentFee(), 0.001);
    assertEquals(scenario.getScenarioId(), enrolment.getMarginScenarioId());
    assertEquals(sameYearEnrolment.getEnrolmentFee(), enrolment.getEnrolmentFee());
    assertEquals(sameYearEnrolment.getMarginYearMinus2(), enrolment.getMarginYearMinus2());
    assertEquals(sameYearEnrolment.getMarginYearMinus3(), enrolment.getMarginYearMinus3());
    assertEquals(sameYearEnrolment.getMarginYearMinus4(), enrolment.getMarginYearMinus4());
    assertEquals(sameYearEnrolment.getMarginYearMinus5(), enrolment.getMarginYearMinus5());
    assertEquals(sameYearEnrolment.getMarginYearMinus6(), enrolment.getMarginYearMinus6());
  }


  @Test
  public void shouldPreserveSameYearLateParticipantBehaviour() throws Exception {
    Scenario scenario = createScenario();
    VerificationEnrolmentCalculator calculator =
        EnrolmentCalculatorFactory.getVerificationEnrolmentCalculator();

    Enrolment enrolment = calculator.calculateEnrolment(scenario);

    assertEquals(Integer.valueOf(2021), enrolment.getEnrolmentYear());
  }


  private Scenario createScenario() {
    Scenario scenario = new Scenario();
    scenario.setYear(2021);
    scenario.setScenarioId(816100);
    scenario.setIsInCombinedFarmInd(false);

    Client client = new Client();
    client.setClientId(7592);
    client.setParticipantPin(23179765);
    scenario.setClient(client);

    Benefit benefit = new Benefit();
    benefit.setAllocatedReferenceMargin(50000.0);
    benefit.setEnhancedReferenceMarginForBenefitCalculation(59170.86);

    FarmingYear farmingYear = new FarmingYear();
    farmingYear.setBenefit(benefit);
    scenario.setFarmingYear(farmingYear);

    List<ReferenceScenario> referenceScenarios = new ArrayList<>();
    for(int year = 2020; year >= 2016; year--) {
      ReferenceScenario referenceScenario = new ReferenceScenario();
      referenceScenario.setYear(year);
      referenceScenario.setUsedInCalc(year >= 2018);

      MarginTotal marginTotal = new MarginTotal();
      marginTotal.setProductionMargAftStrChangs(Double.valueOf(year * 10));
      FarmingYear referenceFarmingYear = new FarmingYear();
      referenceFarmingYear.setMarginTotal(marginTotal);
      referenceScenario.setFarmingYear(referenceFarmingYear);

      referenceScenarios.add(referenceScenario);
    }
    scenario.setReferenceScenarios(referenceScenarios);

    return scenario;
  }
}
