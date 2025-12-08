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
package ca.bc.gov.srm.farm.chefs.resource.common;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class CustomFeedGrid extends ChefsResource {

  private LabelValue typeOfAnimalCustomFed;
  private Double numberOfAnimalsCustomFed;
  private Double averageCustomFeedDaysPerAnimal;

  public LabelValue getTypeOfAnimalCustomFed() {
    return typeOfAnimalCustomFed;
  }

  public void setTypeOfAnimalCustomFed(LabelValue typeOfAnimalCustomFed) {
    this.typeOfAnimalCustomFed = typeOfAnimalCustomFed;
  }

  public Double getNumberOfAnimalsCustomFed() {
    return numberOfAnimalsCustomFed;
  }

  public void setNumberOfAnimalsCustomFed(Double numberOfAnimalsCustomFed) {
    this.numberOfAnimalsCustomFed = numberOfAnimalsCustomFed;
  }

  public Double getAverageCustomFeedDaysPerAnimal() {
    return averageCustomFeedDaysPerAnimal;
  }

  public void setAverageCustomFeedDaysPerAnimal(Double averageCustomFeedDaysPerAnimal) {
    this.averageCustomFeedDaysPerAnimal = averageCustomFeedDaysPerAnimal;
  }

}
