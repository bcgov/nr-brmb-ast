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
package ca.bc.gov.srm.farm.service;

import java.util.List;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.domain.calculator.StructuralChangeRow;



/**
 * 
 */
public interface StructuralChangeService {
 /**
  *
  * @param   scenario  scenario
  *
  * @return  List<StructuralChangeRow>
  */
  List<StructuralChangeRow> getStructuralChanges(final Scenario scenario);
}
