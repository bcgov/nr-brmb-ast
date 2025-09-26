package ca.bc.gov.srm.farm.service;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;


/**
 * @author   awilkinson
 */
public interface ReasonabilityTestService {

  ReasonabilityTestResults test(Scenario scenario) throws ReasonabilityTestException;
 
  boolean missingRequiredData(Scenario scenario);
}
