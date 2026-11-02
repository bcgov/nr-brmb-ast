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

import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;

import java.sql.Connection;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.StatementAFormConstants;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;

public class StatementACorporateSubmissionProcessor extends StatementASubmissionProcessor {

  public StatementACorporateSubmissionProcessor(Connection connection, String formUserType) {
    super(connection, formUserType, ChefsFormTypeCodes.STA,
        StatementAFormConstants.FORM_SHORT_NAME, StatementAFormConstants.FORM_LONG_NAME);
  }

  @Override
  protected boolean isIndividual(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_STATUS_INDIAN.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_INDIVIDUAL.equals(farmType);
  }

  @Override
  protected boolean isBusinessNumberRequired(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_CORPORATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_COOPERATIVE.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_MEMBER_OF_A_PARTNERSHIP.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_LIMITED_PARTNERSHIP.equals(farmType);
  }

  @Override
  protected boolean isTrustNumberRequired(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_FARM_TYPE_COMMUNAL_ORGANIZATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_TRUST.equals(farmType);
  }
}
