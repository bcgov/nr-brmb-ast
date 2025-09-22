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


import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * MenuSelectTagSupport is used to insert a drop-down menu control
 * that will update the screen based on the value selected.
 * 
 * This tag relies on a Scenario being cached in the user cache.
 * pin, year, and scenarioNumber are required attributes.
 * These fields are required to retrieve the current Scenario. 
 * 
 * @author awilkinson
 * @created Nov 26, 2010
 */
public abstract class MenuSelectTagSupport extends TextTagSupport {

  private static final long serialVersionUID = 7809745923213198582L;

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String action;
  private String paramName;
  private String additionalFieldIds;

  /** Constructor */
  public MenuSelectTagSupport() {
    super();
  }
  
  private String urlParams;
  private String urlParamsEval;

  /**
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {
    urlParamsEval = EvalHelper.evalString("urlParams",
        getUrlParams(), this, pageContext);

    return super.doStartTag();
  }

  /**
   * text.
   *
   * @return  The return value.
   */
  @Override
  public String text() {
    logger.debug("building menu select");
    StringBuffer buff = new StringBuffer();
    
    String menuName = getMenuName();

    buff.append("<label id=\"" + menuName + "-container\">\n");
    buff.append("<select id=\"" + menuName + "\" name=\"" + menuName + "\">\n");
    
    appendOptions(buff);

    buff.append("</select>\n");
    buff.append("</label>\n");

    buff.append("<script type=\"text/javascript\">\n");
    buff.append("//<![CDATA[\n");

    buff.append("function findFieldValue(field) {\n");
    buff.append("  var valueVar = '';\n");
    buff.append("  if(field) {\n");
    buff.append("    switch (field.type) {\n");
    buff.append("      case 'text' :\n");
    buff.append("      case 'textarea' :\n");
    buff.append("      case 'hidden' :\n");
    buff.append("        valueVar = field.value;\n");
    buff.append("        break;\n");
    buff.append("      case 'checkbox' :\n");
    buff.append("      case 'radio' :\n");
    buff.append("        if(field.checked==true) {\n");
    buff.append("          valueVar = field.value;\n");
    buff.append("        }\n");
    buff.append("        break;\n");
    buff.append("      case 'select-one' :\n");
    buff.append("        j = 0;\n");
    buff.append("        while (opt = field.options[j++]) {\n");
    buff.append("          if (opt.selected == true) {\n");
    buff.append("            valueVar = opt.value;\n");
    buff.append("          }\n");
    buff.append("        }\n");
    buff.append("        break;\n");
    buff.append("    }\n");
    buff.append("  }\n");
    buff.append("    return valueVar;\n");
    buff.append("}\n");

    buff.append("new YAHOO.widget.Tooltip(\"" + menuName + "ButtonTT\",\n");
    buff.append("    { context:\"" + menuName + "Button\",\n");
    buff.append("      text:\"" + getToolTip() + "\", \n");
    buff.append("      autodismissdelay: 7500,\n");
    buff.append("      effect:{effect:YAHOO.widget.ContainerEffect.FADE,duration:0.5} \n});\n");
    buff.append("var " + menuName + "MenuItemChange = function (event) {\n");
    buff.append("  var oMenuItem = event.newValue;\n");
    buff.append("  this.set(\"label\", ( oMenuItem.cfg.getProperty(\"text\") ));\n");
    buff.append("  var url = \"");
    buff.append(action);
    appendUrlParams(buff);
    buff.append("\";\n");
    appendJavascriptParams(buff);
    buff.append("  document.location.href = url;\n");
    buff.append("  showProcessing();\n"); 
    buff.append("};\n"); 

    buff.append("var " + menuName + "Button = new YAHOO.widget.Button({ \n"); 
    buff.append("    id: \"" + menuName + "Button\", \n"); 
    buff.append("    name: \"" + menuName + "Button\",\n"); 
    buff.append("    label: \""); 
    buff.append(getDisplayValue()); 
    buff.append("\",\n"); 
    buff.append("    type: \"menu\",\n"); 
    buff.append("    lazyloadmenu: false,\n"); 
    buff.append("    menu: \"" + menuName + "\", \n"); 
    buff.append("    container: \"" + menuName + "-container\"\n"); 
    buff.append("});\n"); 
    //  Register a "selectedMenuItemChange" event handler that will sync the 
    //  Button's "label" attribute to the MenuItem that was clicked.
    buff.append("" + menuName + "Button.on(\"selectedMenuItemChange\", " + menuName + "MenuItemChange);\n"); 
    buff.append("//]]>\n");
    buff.append("</script>\n");
  
    return buff.toString();
  }

  /**
   * @return String
   */
  protected String getSelectValueJavascript() {
    return "\" + oMenuItem.value + \"";
  }

  /**
   * @param buff StringBuffer
   */
  protected void appendJavascriptParams(StringBuffer buff) {
    if( ! StringUtils.isBlank(additionalFieldIds) ) {
      StringTokenizer st = new StringTokenizer(additionalFieldIds, ",");
      while(st.hasMoreTokens()) {
        String fieldId = st.nextToken();
        String fieldVar = fieldId + "Field";
        String valueVar = fieldId + "Value";
        
        // find the field
        buff.append("  var ");
        buff.append(fieldVar);
        buff.append(" = document.getElementById('");
        buff.append(fieldId);
        buff.append("');\n");
        
        // find the value
        buff.append("  var ");
        buff.append(valueVar);
        buff.append(" = findFieldValue(");
        buff.append(fieldVar);
        buff.append(");\n");
        
        // now we've figured out the value, so append it to the URL
        buff.append("  if(");
        buff.append(valueVar);
        buff.append(") {\n");
        buff.append("    url = url + '&' + ");
        buff.append(fieldVar);
        buff.append(".name");
        buff.append(" + '=' + ");
        buff.append(valueVar);
        buff.append(";\n");
        buff.append("  }\n");
      }
    }
  }

  /**
   * @return String
   */
  protected abstract String getDisplayValue();

  /**
   * @return String
   */
  protected abstract String getToolTip();

  /**
   * @param buff StringBuffer
   */
  protected abstract void appendUrlParams(StringBuffer buff);

  /**
   * @return String
   */
  protected abstract String getMenuName();

  /**
   * @param buff StringBuffer
   */
  protected abstract void appendOptions(StringBuffer buff);

  
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
   * @return the urlParamsEval
   */
  public String getUrlParamsEval() {
    return urlParamsEval;
  }

  /**
   * @return the additionalFieldIds
   */
  public String getAdditionalFieldIds() {
    return additionalFieldIds;
  }

  /**
   * @param additionalFieldIds the additionalFieldIds to set the value to
   */
  public void setAdditionalFieldIds(String additionalFieldIds) {
    this.additionalFieldIds = additionalFieldIds;
  }

  /**
   * @return the paramName
   */
  public String getParamName() {
    return paramName;
  }

  /**
   * @param paramName the paramName to set the value to
   */
  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

}
