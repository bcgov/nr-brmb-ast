/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import javax.servlet.jsp.JspException;


/**
 * VarTagSupport.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class VarTagSupport extends ScopeTagSupport {

  private static final long serialVersionUID = -7856418296367639043L;
  /** var. */
  private String var;

  /** Creates a new VarTagSupport object. */
  public VarTagSupport() {
    super();
  }

  /**
   * getVar.
   *
   * @return  The return value.
   */
  public String getVar() {
    return var;
  }

  /**
   * Sets the value for var.
   *
   * @param  value  Input parameter.
   */
  public void setVar(final String value) {
    this.var = value;
  }

  /**
   * store.
   *
   * @param   o  The parameter value.
   *
   * @throws  JspException  On exception.
   */
  protected void store(final Object o) throws JspException {
    int scope = getScopeValue();

    var = EvalHelper.evalString("var", getVar(), this, pageContext);

    if ((var != null) && !var.equals("")) {

      if (o == null) {
        pageContext.removeAttribute(var, scope);
      } else {
        pageContext.setAttribute(var, o, scope);
      }
    }
  }

  /**
   * store.
   *
   * @return  the object
   *
   * @throws  JspException  On exception.
   */
  protected Object retrieve() throws JspException {
    Object o = null;
    int scope = getScopeValue();
    var = EvalHelper.evalString("var", getVar(), this, pageContext);

    if ((var != null) && !var.equals("")) {
      o = pageContext.getAttribute(var, scope);
    }

    return o;
  }

}
