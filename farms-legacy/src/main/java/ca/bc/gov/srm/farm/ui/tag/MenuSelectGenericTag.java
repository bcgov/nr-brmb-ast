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

import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import ca.bc.gov.srm.farm.list.ListView;

/**
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class MenuSelectGenericTag extends MenuSelectTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 4406852018697415092L;
  
  private String name;

  private String toolTip;
  
  private String selectedValue;
  private String selectedValueEval;

  private String options;
  private List<ListView> optionsEval;

  private Logger logger = LoggerFactory.getLogger(getClass());

  /** Constructor */
  public MenuSelectGenericTag() {
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
    selectedValueEval = EvalHelper.evalString("selectedValue", getSelectedValue(), this, pageContext);
    optionsEval = (List<ListView>) ExpressionEvaluatorManager.evaluate("options", getOptions(),
        Object.class, this,
        pageContext);
  
    if (StringUtils.isBlank(name) || optionsEval == null || optionsEval.isEmpty()) {
      logger.debug("optionsEval=[" + optionsEval + "]");
      throw new JspException("options must be non-blank");
    }
  
    return super.doStartTag();
  }

  /**
   * @return String
   */
  @Override
  protected String getMenuName() {
    return getName();
  }

  /**
   * @param buff StringBuffer
   */
  @Override
  protected void appendOptions(StringBuffer buff) {

    for(ListView item : getOptionsEval()) {
      String label = item.getLabel();
      String value = item.getValue();

      buff.append("<option value=\"");
      buff.append(value);
      buff.append("\"");
  
      if(value.equals(getSelectedValueEval())) {
        buff.append(" selected=\"selected\"");
      }
      buff.append(">");
      buff.append(label);
      buff.append("</option>\n");
    }
  }

  /**
   * @return String
   */
  @Override
  protected String getDisplayValue() {
    String displayValue = "";

    for(ListView item : getOptionsEval()) {
      String label = item.getLabel();
      String value = item.getValue();

      if(getSelectedValueEval() != null && value.equals(getSelectedValueEval().toString())) {
        displayValue = label;
      }
    }
    logger.debug("displayValue=[" + displayValue + "]");
    return displayValue;
  }

  /**
   * @param buff StringBuffer
   */
  @Override
  protected void appendUrlParams(StringBuffer buff) {
    buff.append("?");
    buff.append(getParamName());
    buff.append("=");
    buff.append(getSelectValueJavascript());
    String params = getUrlParamsEval();
    if(!StringUtils.isBlank(params)) {
      buff.append("&");
      buff.append(params);
    }
  }


  /**
   * @return the toolTip
   */
  @Override
  public String getToolTip() {
    return toolTip;
  }

  /**
   * @param toolTip the toolTip to set
   */
  public void setToolTip(String toolTip) {
    this.toolTip = toolTip;
  }

  /**
   * @return the options
   */
  public String getOptions() {
    return options;
  }

  /**
   * @param options the options to set
   */
  public void setOptions(String options) {
    this.options = options;
  }

  /**
   * @return the optionsEval
   */
  public List<ListView> getOptionsEval() {
    return optionsEval;
  }

  /**
   * @return the selectedValue
   */
  public String getSelectedValue() {
    return selectedValue;
  }

  /**
   * @param selectedValue the selectedValue to set
   */
  public void setSelectedValue(String selectedValue) {
    this.selectedValue = selectedValue;
  }

  /**
   * @return the selectedValueEval
   */
  public String getSelectedValueEval() {
    return selectedValueEval;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

}
