/**
 * Copyright (c) 2009, B.C..
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


/**
 * GetFirstProvAdminYearTag.
 */
public class GetFirstProvAdminYearTag extends VarTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 6135131369226798445L;

  /** Creates a new GetFirstProvAdminYearTag object. */
  public GetFirstProvAdminYearTag() {
    super();
  }
  
  
  /**
   * doStartTag.
   *
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   */
  @Override
  public int doStartTag() throws JspException {
    String varName = EvalHelper.evalString("var", getVar(), this, pageContext);

    if (StringUtils.isBlank(varName)) {
      throw new JspException("var must be non-blank");
    }
    
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    String key = ConfigurationKeys.FIRST_BC_ADMIN_YEAR;
    String result = config.getValue(key);

    store(result);

    return SKIP_BODY;
  }

}
