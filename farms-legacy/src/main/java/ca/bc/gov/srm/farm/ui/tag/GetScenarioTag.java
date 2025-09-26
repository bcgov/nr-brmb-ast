/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheKeys;

/**
 * @author awilkinson
 * @created Nov 25, 2010
 */
public class GetScenarioTag extends GetUserCacheTag {

  /** serialVersionUID. */
  private static final long serialVersionUID = 2261821241481744654L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String pin;
  private String pinEval;
  
  private String year;
  private String yearEval;
  
  private String scenarioNumber;
  private String scenarioNumberEval;
  

  /**
   * doStartTag.
   *
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   */
  @Override
  public int doStartTag() throws JspException {
    pinEval = EvalHelper.evalString("pin", getPin(), this, pageContext);
    yearEval = EvalHelper.evalString("year", getYear(), this, pageContext);
    scenarioNumberEval = EvalHelper.evalString("scenarioNumber", getScenarioNumber(), this, pageContext);
    if(StringUtils.isBlank(scenarioNumberEval)) {
      scenarioNumberEval = null;
    }

    if (StringUtils.isBlank(pinEval)
        || StringUtils.isBlank(yearEval)) {
      logger.debug("pinEval=[" + pinEval + "]");
      logger.debug("yearEval=[" + yearEval + "]");
      throw new JspException("pin, year, and scenarioNumber must be non-blank");
    }

    return super.doStartTag();
  }

  /**
   * @return  CacheKeys key
   */
  @Override
  protected String getKey() {
    return CacheKeys.SCENARIO_PREFIX + pinEval + "." + yearEval + "." + scenarioNumberEval;
  }

  /**
   * @return the pin
   */
  public String getPin() {
    return pin;
  }

  /**
   * @param pin the pin to set
   */
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   * @return the year
   */
  public String getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(String year) {
    this.year = year;
  }

  /**
   * @return the scenarioNumber
   */
  public String getScenarioNumber() {
    return scenarioNumber;
  }

  /**
   * @param scenarioNumber the scenarioNumber to set
   */
  public void setScenarioNumber(String scenarioNumber) {
    this.scenarioNumber = scenarioNumber;
  }

}
