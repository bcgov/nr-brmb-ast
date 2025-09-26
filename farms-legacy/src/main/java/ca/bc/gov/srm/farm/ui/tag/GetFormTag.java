/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import org.apache.commons.lang.StringUtils;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;


/**
 * GetFormTag.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class GetFormTag extends VarTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = -4990072691819477278L;

  /** Creates a new GetFormTag object. */
  public GetFormTag() {
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

    // Get the current form, if any
    ActionForm form = getForm(pageContext);
    store(form);

    return SKIP_BODY;
  }

  /**
   * getForm.
   *
   * @param   pc  Input parameter.
   *
   * @return  The return value.
   */
  protected ActionForm getForm(final PageContext pc) {
    String formName = getFormName(pc);
    ActionForm result;

    if (formName == null) {
      result = null;
    } else {
      result = (ActionForm) (pc.findAttribute(formName));
    }

    return result;
  }

  /**
   * getFormName.
   *
   * @param   pc  Input parameter.
   *
   * @return  The return value.
   */
  private String getFormName(final PageContext pc) {
    String result = null;
    ActionMapping mapping = getMapping(pc);

    if (mapping != null) {
      result = mapping.getName();
    }

    return result;
  }

  /**
   * getMapping.
   *
   * @param   pc  Input parameter.
   *
   * @return  The return value.
   */
  private ActionMapping getMapping(final PageContext pc) {
    ActionMapping result = null;
    result = (ActionMapping) (pc.getAttribute(Globals.MAPPING_KEY,
          PageContext.REQUEST_SCOPE));

    return result;
  }

}
