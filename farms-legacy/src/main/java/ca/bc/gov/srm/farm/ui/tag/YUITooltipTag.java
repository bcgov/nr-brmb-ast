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

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.taglib.TagUtils;

import ca.bc.gov.srm.farm.util.StringUtils;



/**
 * @author awilkinson
 */
public class YUITooltipTag extends TextTagSupport {

  private static final long serialVersionUID = -6863625463141279178L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String targetId;
  private String targetIdEval;
  private String messageKey;
  private String messageKeyEval;
  private String messageText;

  /** Creates a new  object. */
  public YUITooltipTag() {
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
    targetIdEval = EvalHelper.evalString("targetId", targetId, this, pageContext);
    messageKeyEval = EvalHelper.evalString("message", messageKey, this, pageContext);
    
    if (StringUtils.isBlank(targetIdEval)) {
      logger.error("targetIdEval=[" + targetIdEval + "]");
      throw new JspException("targetId must be non-blank");
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
    
    String tooltipMessage;
    if(!StringUtils.isBlank(messageKeyEval)) {
      TagUtils tagUtils = TagUtils.getInstance();
      tooltipMessage = tagUtils.message(pageContext, null, null, messageKeyEval);
    } else {
      tooltipMessage = messageText;
    }
    
    html.append("<script type=\"text/javascript\">\n");
    html.append("  //<![CDATA[\n");
    html.append("  new YAHOO.widget.Tooltip(\"");
    html.append(targetIdEval);
    html.append("TT\",\n");
    html.append("    { context:\"");
    html.append(targetIdEval);
    html.append("\",\n");
    html.append("      text:\"");
    html.append(tooltipMessage);
    html.append("\",\n");
    html.append("      autodismissdelay: 7500,\n");
    html.append("      effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} });\n");
    
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
   * @return the messageKey
   */
  public String getMessageKey() {
    return messageKey;
  }

  /**
   * @param messageKey the messageKey to set
   */
  public void setMessageKey(String messageKey) {
    this.messageKey = messageKey;
  }

  /**
   * @return the messageKeyEval
   */
  public String getMessageKeyEval() {
    return messageKeyEval;
  }

  /**
   * @return the messageText
   */
  public String getMessageText() {
    return messageText;
  }

  /**
   * @param messageText the messageText to set
   */
  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  /**
   * @return the targetId
   */
  public String getTargetId() {
    return targetId;
  }

  /**
   * @param targetId the targetId to set
   */
  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  /**
   * @return the targetIdEval
   */
  public String getTargetIdEval() {
    return targetIdEval;
  }

}
