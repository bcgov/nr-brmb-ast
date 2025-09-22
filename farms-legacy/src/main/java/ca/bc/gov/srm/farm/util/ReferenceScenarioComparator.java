/**
 *
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import java.util.Comparator;

import ca.bc.gov.srm.farm.domain.ReferenceScenario;

/**
 *
 *
 * @author dzwiers
 */
public final class ReferenceScenarioComparator implements Comparator<ReferenceScenario> {

  /**
   */
  private ReferenceScenarioComparator(){}
  
  /**
   * @param pO1 Object
   * @param pO2 Object
   * @return int
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  @Override
  public int compare(ReferenceScenario pO1, ReferenceScenario pO2) {
    ReferenceScenario r1 = pO1;
    ReferenceScenario r2 = pO2;
    
    if(r1 == null){
      if(r2 == null) {
        return 0;
      } else {
        return -1;
      }
    }
    
    if(r2 == null){
      return 1;
    }

    Integer i1 = r1.getYear();
    Integer i2 = r2.getYear();

    if(i1 == null){
      if(i2 == null) {
        return 0;
      } else {
        return -1;
      }
    }
    
    if(i2 == null){
      return 1;
    }
    
    return i1.compareTo(i2);
  }

  private static ReferenceScenarioComparator instance = null;
  /**
   * @return Comparator
   */
  public static Comparator<ReferenceScenario> getInstance() {
    if(instance == null){
      instance = new ReferenceScenarioComparator();
    }
    return instance;
  }

}
