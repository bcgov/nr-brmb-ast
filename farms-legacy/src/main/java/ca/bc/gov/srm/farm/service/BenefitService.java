/**
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service;

import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;



/**
 * 
 */
public interface BenefitService {
  /**
   * Validate that the benefit can be calculated
   *
   * @param   scenario  scenario
   * @throws  Exception on any expection 
   * @return  ActionMessages
   */
  ActionMessages getAllValidationErrors(final Scenario scenario, final String userId) 
  throws Exception;
  
  /**
   * Validate that the benefit can be calculated,
   * try to calculate it, save it to the DB.
   *
   * @param   scenario  scenario
   * @param   userId  userId
   * @throws  Exception on any expection 
   * @return  ActionMessages
   */
  ActionMessages calculateBenefit(final Scenario scenario, final String userId) 
  throws Exception;
  
  
  /**
   * validate, calculate, save
   * 
   * @param scenario scenario
   * @param userId userId
   * @param saveBenefit true if you want the calculations saved
   * @param validate true if you want to validate the scenario before calculation
   * @param recalculateOverridables true if you want to recalculate fields
   *                                that can be overridden on the benefit screen
   * @throws Exception on error
   * 
   * @return ActionMessages messages
   */
  ActionMessages calculateBenefit(
      final Scenario scenario,
      final String userId,
      final boolean saveBenefit,
      final boolean validate,
      final boolean recalculateOverridables) 
  throws Exception;

}
