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
package ca.bc.gov.srm.farm.domain.codes;

public final class ImportClassCodes {

  /**  */
  private ImportClassCodes() {
  }

  /** Canada Revenue Agency */
  public static final String CRA = "CRA";
  public static final String CRA_DESCRIPTION = "Canada Revenue Agency";

  /** Fair Market Value */
  public static final String FMV = "FMV";
  public static final String FMV_DESCRIPTION = "Fair Market Value";

  /** Benchmark Per Unit */
  public static final String BPU = "BPU";
  public static final String BPU_DESCRIPTION = "Benchmark Per Unit";

  /** Insurable Values & Premium Rates */
  public static final String IVPR = "IVPR";
  public static final String IVPR_DESCRIPTION = "Insurable Values & Premium Rates";

  /** Accrual Adjusted Reference Margin */
  public static final String AARM = "AARM";
  public static final String AARM_DESCRIPTION = "Accrual Adjusted Reference Margin";
  
  /** BC Generated CRA */
  public static final String BCCRA = "BCCRA";
  public static final String BCCRA_DESCRIPTION = "BC Generated CRA";
  
  /** Enrolment Generation */
  public static final String ENROL = "ENROL";
  
  /** Enrolment Data Transfer to CRM */
  public static final String XENROL = "XENROL";
  
  /** Contact Data Transfer to CRM */
  public static final String XCONTACT = "XCONTACT";
  
  /** Benefit Data Transfer to CRM */
  public static final String XSTATE = "XSTATE";
  
  public static final String TIP_REPORT = "TIP_REPORT";
  
  public static final String TRIAGE = "TRIAGE";
  

  public static boolean isCra(String importClassCode) {
    boolean result = false;
    
    if(CRA.equals(importClassCode)) {
      result = true;
    } else if(BCCRA.equals(importClassCode)) {
      result = true;
    }
    return result;
  }
  
  public static boolean isBpu(String importClassCode) {
    return BPU.equals(importClassCode);
  }

  public static boolean isIvpr(String importClassCode) {
    return IVPR.equals(importClassCode);
  }

  public static boolean isFmv(String importClassCode) {
    return FMV.equals(importClassCode);
  }
  
  public static boolean isEnrolment(String importClassCode) {
    return ENROL.equals(importClassCode);
  }
  
  public static boolean isTransfer(String importClassCode) {
    boolean result = false;
    
    if(XSTATE.equals(importClassCode)) {
      result = true;
    } else if(XCONTACT.equals(importClassCode)) {
      result = true;
    } else if(XENROL.equals(importClassCode)) {
      result = true;
    }
    return result;
  }
  
  public static boolean isTipReport(String importClassCode) {
    return TIP_REPORT.equals(importClassCode);
  }
}
