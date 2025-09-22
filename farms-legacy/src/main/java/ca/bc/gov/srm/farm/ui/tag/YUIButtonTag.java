/**
 * Copyright (c) 2012, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.tag;

import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.taglib.TagUtils;

import ca.bc.gov.srm.farm.util.StringUtils;



/**
 * @author awilkinson
 */
public class YUIButtonTag extends TextTagSupport {

  private static final long serialVersionUID = -6863625463141279178L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String buttonId;
  private String buttonLabel;
  private String buttonLabelEval;
  private String formId;
  private String action;
  private String actionEval;
  private String function;
  private String disabled;
  private String disabledEval;
  private String urlParams;
  private String urlParamsEval;
  private String urlParamsMap;
  private Map<String, String> urlParamsMapEval;
  private String tooltip;
  private String tooltipEval;
  private String confirmMessage;
  private String confirmMessageEval;

  /** Creates a new  object. */
  public YUIButtonTag() {
    super();
  }

  /**
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @SuppressWarnings("unchecked")
  @Override
  public int doStartTag() throws JspException {
    urlParamsEval = EvalHelper.evalString("urlParams", urlParams, this, pageContext);
    urlParamsMapEval = (Map<String, String>) EvalHelper.eval("urlParamsMap", urlParamsMap, this, pageContext);
    buttonLabelEval = EvalHelper.evalString("buttonLabel", buttonLabel, this, pageContext);
    actionEval = EvalHelper.evalString("action", action, this, pageContext);
    disabledEval = EvalHelper.evalString("disabled", disabled, this, pageContext);
    tooltipEval = EvalHelper.evalString("tooltip", tooltip, this, pageContext);
    confirmMessageEval = EvalHelper.evalString("confirmMessage", confirmMessage, this, pageContext);
    
    if (StringUtils.isBlank(buttonLabelEval)) {
      logger.error("buttonLabelEval=[" + buttonLabelEval + "]");
      throw new JspException("buttonLabel must be non-blank");
    }

    return super.doStartTag();
  }

  /**
   * 
   * @return  The return value.
   * @throws JspException JspException
   */
  @Override
  public String text() throws JspException {
    StringBuffer html = new StringBuffer();
    
    boolean formSpecified = !StringUtils.isBlank(formId);
    boolean actionSpecified = !StringUtils.isBlank(actionEval);
    boolean functionSpecified = !StringUtils.isBlank(function);
    boolean isDisabled = Boolean.valueOf(disabledEval).booleanValue();
    boolean toolTipSpecified = !StringUtils.isBlank(tooltipEval);
    boolean confirmMessageSpecified = !StringUtils.isBlank(confirmMessageEval);

    boolean createFunction;
    String functionName;
    if(functionSpecified || isDisabled) {
      createFunction = false;
      functionName = function;
    } else {
      createFunction = true;
      functionName = buttonId + "Func";
    }
    
    TagUtils tagUtils = TagUtils.getInstance();
    String labelMessage = tagUtils.message(pageContext, null, null, buttonLabelEval);
    
    String computedAction = null;
    if(actionSpecified) {
      try {
        computedAction =
            tagUtils.computeURL(pageContext, null, null, null, actionEval, null,
                urlParamsMapEval, null, false);
      } catch (MalformedURLException e) {
        logger.error("Bad value specified for 'action'");
        throw new JspException(e.getMessage());
      }
    }

    html.append("<button type=\"button\" id=\"");
    html.append(buttonId);
    html.append("\">");
    html.append(labelMessage);
    html.append("</button>\n");
    
    html.append("<script type=\"text/javascript\">\n");
    html.append("  //<![CDATA[\n");
    
    if(createFunction) {
      html.append("function ");
      html.append(functionName);
      html.append("() {\n");
      if(confirmMessageSpecified) {
        String confirmString = tagUtils.message(pageContext, null, null, confirmMessageEval);
        html.append("if(confirm(\"");
        html.append(confirmString);
        html.append("\")) {\n");
      }
      if(formSpecified) {
        html.append("var form = document.getElementById('");
        html.append(formId);
        html.append("');\n");
        if(actionSpecified) {
          html.append("form.action = \"");
          html.append(computedAction);
          html.append("\";\n");
        }
        html.append("submitForm(form);\n");
      } else if(actionSpecified) {
        html.append("document.location.href = \"");
        html.append(computedAction);
        if(! StringUtils.isBlank(urlParamsEval)) {
          html.append("?");
          html.append(urlParamsEval);
        }
        html.append("\";\n");
      }
      if(confirmMessageSpecified) {
        html.append("}\n");
      }
      html.append("}\n");
    }

    html.append("  new YAHOO.widget.Button(\"");
    html.append(buttonId);
    html.append("\"");
    if(isDisabled) {
      html.append(", {disabled: true}");
    } else {
      html.append(", {onclick: {fn: ");
      html.append(functionName);
      html.append("}}");
    }
    html.append(");\n");
    
    if(toolTipSpecified) {
      String tooltipMessage = tagUtils.message(pageContext, null, null, tooltipEval);
      html.append("  new YAHOO.widget.Tooltip(\"");
      html.append(buttonId);
      html.append("ButtonTT\",\n");
      html.append("    { context:\"");
      html.append(buttonId);
      html.append("\", text:\"");
      html.append(tooltipMessage);
      html.append("\",\n");
      html.append("      autodismissdelay: 7500,\n");
      html.append("      effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });\n");
    }
    
    html.append("  //]]>\n");
    html.append("</script>");
    
    html.append("");
    html.append("");
    
    return html.toString();
  }


  /**
   * @return the logger
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * @param logger the logger to set
   */
  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @param action the action to set
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * @return the function
   */
  public String getFunction() {
    return function;
  }

  /**
   * @param function the function to set
   */
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * @return the buttonId
   */
  public String getButtonId() {
    return buttonId;
  }

  /**
   * @param buttonId the buttonId to set
   */
  public void setButtonId(String buttonId) {
    this.buttonId = buttonId;
  }

  /**
   * @return the formId
   */
  public String getFormId() {
    return formId;
  }

  /**
   * @param formId the formId to set
   */
  public void setFormId(String formId) {
    this.formId = formId;
  }

  /**
   * @return the urlParams
   */
  public String getUrlParams() {
    return urlParams;
  }

  /**
   * @param urlParams the urlParams to set
   */
  public void setUrlParams(String urlParams) {
    this.urlParams = urlParams;
  }

  /**
   * @return the buttonLabel
   */
  public String getButtonLabel() {
    return buttonLabel;
  }

  /**
   * @param buttonLabel the buttonLabel to set
   */
  public void setButtonLabel(String buttonLabel) {
    this.buttonLabel = buttonLabel;
  }

  /**
   * @return the urlParamsMap
   */
  public String getUrlParamsMap() {
    return urlParamsMap;
  }

  /**
   * @param urlParamsMap the urlParamsMap to set
   */
  public void setUrlParamsMap(String urlParamsMap) {
    this.urlParamsMap = urlParamsMap;
  }

  /**
   * @return the disabled
   */
  public String getDisabled() {
    return disabled;
  }

  /**
   * @param disabled the disabled to set
   */
  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  /**
   * @return the tooltip
   */
  public String getTooltip() {
    return tooltip;
  }

  /**
   * @param tooltip the tooltip to set
   */
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  /**
   * @return the confirmMessage
   */
  public String getConfirmMessage() {
    return confirmMessage;
  }

  /**
   * @param confirmMessage the confirmMessage to set
   */
  public void setConfirmMessage(String confirmMessage) {
    this.confirmMessage = confirmMessage;
  }

}
