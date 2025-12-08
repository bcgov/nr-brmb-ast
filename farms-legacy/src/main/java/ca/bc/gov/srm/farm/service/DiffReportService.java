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

import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.diff.ProgramYearVersionDiff;
import ca.bc.gov.srm.farm.exception.ServiceException;

/**
 * For a given year, DiffReportService compares the CRA
 * data (PYV) for a scenario or reference scenario to the
 * latest Program Year Version and produces an object
 * representing the differences.
 * 
 * @author awilkinson
 * @created Mar 22, 2011
 */
public interface DiffReportService {

  /**
   * @param oldFarmingYear FarmingYear
   * @param newFarmingYear the new FarmingYear to compare to
   * @return ProgramYearVersionDiff representing the differences
   * @throws ServiceException On Exception
   */
  ProgramYearVersionDiff generateDiffReport(
      FarmingYear oldFarmingYear,
      FarmingYear newFarmingYear)
  throws ServiceException, InstantiationException, IllegalAccessException;

}
