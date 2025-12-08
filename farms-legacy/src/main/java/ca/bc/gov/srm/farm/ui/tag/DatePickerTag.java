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

/**
 * DatePickerTag is helper tag for inserting a YUI calendar button.
 * The attribute "fieldId" is required. It is the name of the textfield
 * that the calendar operates on.
 * The button will pop up a calendar. If there is a date in the textfield
 * then that date will be selected, otherwise the current date will be selected.
 * When a date is selected from the calendar, the textfield is updated.
 * This tag assumes a date format of yyyy-MM-dd. 
 * 
 * @author awilkinson
 * @created Nov 29, 2010
 */
public class DatePickerTag extends TextTagSupport {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /** serialVersionUID. */
  private static final long serialVersionUID = -4990072696549477278L;
  
  private String fieldId;
  private String onUpdateFunc;
  private String initialDate;
  
  /**
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {

    if (StringUtils.isBlank(fieldId)) {
      logger.error("fieldId=[" + fieldId + "]");
      throw new JspException("fieldId must be non-blank");
    }

    return super.doStartTag();
  }

  /**
   * @return String
   */
  @Override
  public String text() {
    StringBuffer buff = new StringBuffer();
    
    String buttonName = "cal" + fieldId + "Button";
    String containerName = "cal" + fieldId + "Container";
    String calendarName = "cal" + fieldId;
    String handleFunctionName = "handle" + fieldId + "Select";
    String updateFunctionName = "update" + fieldId;

    buff.append("<div id=\"");
    buff.append(fieldId);
    buff.append("Container\" style=\"float:right;\">\n");
    buff.append("  <img id=\"");
    buff.append(buttonName);
    buff.append("\" src=\"images/calendar_view_day.jpg\"");
    buff.append("   alt=\"\" width=\"16\" height=\"16\" />\n");
    buff.append("  <div id=\"");
    buff.append(containerName);
    buff.append("\" style=\"display:none; position:absolute;");
    buff.append("    z-index:2\"></div>\n");
    buff.append("</div>\n");

    buff.append("<script type=\"text/javascript\">\n");
    buff.append("//<![CDATA[\n");
    buff.append("  var ");
    buff.append(calendarName);
    buff.append("CalendarNavConfig = {\n");
    buff.append("      strings: {\n");
    buff.append("          month: \"Month\",\n");
    buff.append("          year: \"Year\",\n");
    buff.append("          submit: \"OK\",\n");
    buff.append("          cancel: \"Cancel\",\n");
    buff.append("          invalidYear: \"Year needs to be a number\"\n");
    buff.append("      },\n");
    buff.append("      monthFormat: YAHOO.widget.Calendar.LONG,\n");
    buff.append("      initialFocus: \"year\"\n");
    buff.append("  };\n");
    buff.append("  YAHOO.farm.");
    buff.append(calendarName);
    buff.append(" = new YAHOO.widget.Calendar(\"");
    buff.append(    calendarName);
    buff.append(    "\",\"");
    buff.append(    containerName);
    buff.append(    "\",");
    buff.append("   { title:\"Choose a date:\", close:true,");
    if(initialDate != null) {
      buff.append("pagedate:\"");
      buff.append(initialDate);
      buff.append("\",");
    }
    buff.append(" navigator:");
    buff.append(calendarName);
    buff.append("CalendarNavConfig } );\n");
    buff.append("  YAHOO.farm.");
    buff.append(    calendarName);
    buff.append(    ".selectEvent.subscribe(");
    buff.append(    handleFunctionName);
    buff.append(    ", YAHOO.farm.");
    buff.append(    calendarName);
    buff.append(    ",");
    buff.append("   true);\n");
    buff.append("  ");
    buff.append(updateFunctionName + "();\n");
    buff.append("  YAHOO.farm.");
    buff.append(    calendarName);
    buff.append(    ".render();\n");
    //  Listener to show the single page Calendar when the button is clicked
    buff.append("  YAHOO.util.Event.addListener(\"");
    buff.append(    buttonName);
    buff.append(    "\", \"click\",");
    buff.append(" YAHOO.farm.");
    buff.append(    calendarName);
    buff.append(    ".show, YAHOO.farm.");
    buff.append(    calendarName);
    buff.append(    ", true);\n");
    buff.append("  function ");
    buff.append(    handleFunctionName);
    buff.append(    "(type,args,obj) {\n");
    buff.append("    var dates = args[0];\n");
    buff.append("    var date = dates[0];\n");
    buff.append("    var year = date[0], month = date[1], day = date[2];\n");
    buff.append("    var dateText =  year + \"-\" + month + \"-\" + day;\n");
    buff.append("    var dateTextfield = document.getElementById(\"" + fieldId + "\");\n");
    buff.append("    dateTextfield.value =  dateText;\n");
    if (onUpdateFunc != null) {
      buff.append("    ");
      buff.append(onUpdateFunc);
      buff.append("();\n");
    }
    buff.append("    YAHOO.farm.");
    buff.append(      calendarName);
    buff.append(      ".hide();\n");
    buff.append("}\n");

    buff.append("function ");
    buff.append(    updateFunctionName);
    buff.append(    "() {\n");
    buff.append("  var dateTextfield = document.getElementById(\"" + fieldId + "\");\n");
    buff.append("  if (dateTextfield.value != \"\") {\n");
    buff.append("    var dateSplit = dateTextfield.value.split(\"-\", 3);\n");
    buff.append("    var year = dateSplit[0];\n");
    buff.append("    var month = dateSplit[1];\n");
    buff.append("    var day = dateSplit[2];\n");
    buff.append("    if(!isNaN(year) && !isNaN(month) && !isNaN(day)) {\n");
    buff.append("      var selectedDate = new Date();\n");
    buff.append("      selectedDate.setFullYear(year, month - 1, day);\n");
    buff.append("      YAHOO.farm.");
    buff.append(          calendarName);
    buff.append(          ".select(selectedDate);\n");
    buff.append("      YAHOO.farm.");
    buff.append(          calendarName);
    buff.append(          ".cfg.setProperty(\"pagedate\",");
    buff.append("        (selectedDate.getMonth()+1) + \"/\" + selectedDate.getFullYear());\n");
    buff.append("      var selectedDates = YAHOO.farm.");
    buff.append(          calendarName);
    buff.append(          ".getSelectedDates();\n");
    buff.append("    }\n");
    buff.append("  }\n");
    buff.append("}\n");
    buff.append("//]]>\n");
    buff.append("</script>\n");
  
    return buff.toString();
  }

  /**
   * @return the fieldId
   */
  public String getFieldId() {
    return fieldId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @return the onUpdateFunc
   */
  public String getOnUpdateFunc() {
    return onUpdateFunc;
  }

  /**
   * @param onUpdateFunc the onUpdateFunc to set
   */
  public void setOnUpdateFunc(String onUpdateFunc) {
    this.onUpdateFunc = onUpdateFunc;
  }

  /**
   * @return the initialDate
   */
  public String getInitialDate() {
    return initialDate;
  }

  /**
   * @param initialDate the initialDate to set
   */
  public void setInitialDate(String initialDate) {
    this.initialDate = initialDate;
  }

}
