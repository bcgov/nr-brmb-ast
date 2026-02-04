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

import ca.bc.gov.srm.farm.util.ProgramYearUtils;


/**
 * Displays a select box allowing the user to select the program year
 * that they wish to work with. When the year is selected, the page
 * will be refreshed with data for that year.
 * 
 * @author awilkinson
 * @created Nov 26, 2010
 */
public class ProgramYearSelectTag extends MenuSelectTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 4406852018697415092L;

  private String year;
  private String yearEval;

  private Logger logger = LoggerFactory.getLogger(getClass());

  /** Constructor */
  public ProgramYearSelectTag() {
    super();
  }

  /**
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {
    yearEval = EvalHelper.evalString("year", getYear(), this, pageContext);
  
    if (StringUtils.isBlank(yearEval)) {
      logger.debug("yearEval=[" + yearEval + "]");
      throw new JspException("year must be non-blank");
    }
  
    return super.doStartTag();
  }

  /**
   * @return String
   */
  @Override
  protected String getMenuName() {
    return "yearPicker";
  }

  /**
   * @param buff StringBuffer
   */
  @Override
  protected void appendOptions(StringBuffer buff) {
    int[] adminYears = ProgramYearUtils.getAdminYears();
    
    int selectedYear = Integer.parseInt(getYearEval());
    for(int ii = 0; ii < adminYears.length; ii++) {
      int curYear = adminYears[ii];
      buff.append("<option value=\"");
      buff.append(Integer.toString(curYear));
      buff.append("\"");
  
      if(curYear == selectedYear) {
      	buff.append(" selected=\"selected\"");
      }
      buff.append(">");
      buff.append(Integer.toString(curYear));
      buff.append("</option>\n");
    }
  }

  /**
   * @param buff StringBuffer
   */
  @Override
  protected void appendUrlParams(StringBuffer buff) {
    buff.append("?year=");
    buff.append(getSelectValueJavascript());
    String additionalParams = getUrlParamsEval();
    if(!StringUtils.isBlank(additionalParams)) {
      buff.append("&");
      buff.append(additionalParams);
    }
  }

  /**
   * @return String
   */
  @Override
  protected String getToolTip() {
    return "Click here to open a different Program Year.";
  }

  /**
   * @return String
   */
  @Override
  protected String getDisplayValue() {
    logger.debug("displayValue=[" + getYearEval() + "]");
    return getYearEval();
  }

  /**
   * @return String
   */
  protected String getYearParam() {
    return getSelectValueJavascript();
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
   * @return the yearEval
   */
  public String getYearEval() {
    return yearEval;
  }

}
