package ca.bc.gov.srm.farm.service;

import java.util.List;

import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalBaseDataResource;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.exception.ServiceException;

public interface ChefsSubmissionProcessorService {

  Integer createInterimChefsScenario(InterimSubmissionDataResource data, Integer clientId, Integer programYear, String applicationVersion,
      Integer submissionId, String user) throws ServiceException;
  
  Integer createNppSupplementalData(NppSubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, boolean createdParticipant, Integer submissionId, List<FarmingOperation> farmingOperations, String user) throws ServiceException;

  Integer createAdjustmentChefsScenario(AdjustmentSubmissionDataResource data, Integer clientId, Integer programYear,
      Integer scenarioIdToCopy, String applicationVersion, String user) throws ServiceException;
  
  Integer createSupplementalChefsScenario(SupplementalBaseDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, String municipalityCode, String user, String scenarioCategoryCode) throws ServiceException;
  
  Integer createCoverageChefsScenario(CoverageSubmissionDataResource data, Integer clientId, Integer programYear,
      String applicationVersion, String municipalityCode, String user) throws ServiceException;

  Integer createCashMarginChefsScenario(CashMarginsSubmissionDataResource data, Integer clientId, Integer programYear, String applicationVersion,
      String municipalityCode, Integer submissionId, String user) throws ServiceException;

  Integer createStatementAChefsScenario(StatementASubmissionDataResource data, Integer clientId, Integer programYear, String applicationVersion,
      String municipalityCode, String user, String scenarioCategoryCode) throws ServiceException;

}
