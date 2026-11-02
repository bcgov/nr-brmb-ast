/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.processor;

import java.sql.Connection;
import java.util.List;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.StatementAIndividualsFormConstants;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAIndividualsSubmissionDataResource;
import ca.bc.gov.srm.farm.domain.FarmingYear;

public class StatementAIndividualsSubmissionProcessor extends StatementASubmissionProcessor {

  public StatementAIndividualsSubmissionProcessor(Connection connection, String formUserType) {
    super(connection, formUserType, ChefsFormTypeCodes.STA_IND,
        StatementAIndividualsFormConstants.FORM_SHORT_NAME, StatementAIndividualsFormConstants.FORM_LONG_NAME);
  }

  @Override
  protected Class<? extends StatementASubmissionDataResource> getSubmissionDataClass() {
    return StatementAIndividualsSubmissionDataResource.class;
  }

  @Override
  protected void validateFarmType(StatementASubmissionDataResource data, List<String> validationErrors) {
    // The Individuals form has no farmType selector; its identity is established by STA_IND.
  }

  @Override
  protected void populateFarmType(StatementASubmissionDataResource data, FarmingYear fy) {
    // Sole proprietor / partnership is populated from businessStructure by the shared processor.
  }

  @Override
  protected String getApplicantName(StatementASubmissionDataResource data) {
    return data instanceof StatementAIndividualsSubmissionDataResource
        ? ((StatementAIndividualsSubmissionDataResource) data).getParticipantName() : data.getCorporationName();
  }

  @Override
  protected String getApplicantNameLabel() {
    return "Participant Name";
  }

  @Override
  protected boolean isIndividual(StatementASubmissionDataResource data) {
    return true;
  }

  @Override
  protected boolean isBusinessNumberRequired(StatementASubmissionDataResource data) {
    return false;
  }

  @Override
  protected boolean isTrustNumberRequired(StatementASubmissionDataResource data) {
    return false;
  }
}
