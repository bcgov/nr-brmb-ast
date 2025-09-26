/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * ScopeTagSupport.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class ScopeTagSupport extends TagSupport {

  private static final long serialVersionUID = 4885129991980975393L;

  /** PAGE_SCOPE_STRING. */
  public static final String PAGE_SCOPE_STRING = "page";

  /** REQUEST_SCOPE_STRING. */
  public static final String REQUEST_SCOPE_STRING = "request";

  /** SESSION_SCOPE_STRING. */
  public static final String SESSION_SCOPE_STRING = "session";

  /** APPLICATION_SCOPE_STRING. */
  public static final String APPLICATION_SCOPE_STRING = "application";

  /** defaultScope. */
  private int defaultScope;

  /** scope. */
  private String scope;

  /** Creates a new ScopeTagSupport object. */
  public ScopeTagSupport() {
    super();
    defaultScope = PageContext.PAGE_SCOPE;
  }

  /**
   * Creates a new ScopeTagSupport object.
   *
   * @param  value  value Input parameter to initialize class.
   */
  public ScopeTagSupport(final int value) {
    super();
    defaultScope = value;
  }

  /**
   * getScope.
   *
   * @return  The return value.
   */
  public String getScope() {
    return scope;
  }

  /**
   * Sets the value for scope.
   *
   * @param  value  Input parameter.
   */
  public void setScope(final String value) {
    this.scope = value;
  }

  /**
   * getScopeValue.
   *
   * @return  The return value.
   */
  protected int getScopeValue() {
    int result = defaultScope;

    String trimScope;

    if (scope == null) {
      trimScope = null;
    } else {
      trimScope = scope.trim();
    }

    if (PAGE_SCOPE_STRING.equalsIgnoreCase(trimScope)) {
      result = PageContext.PAGE_SCOPE;
    } else if (REQUEST_SCOPE_STRING.equalsIgnoreCase(trimScope)) {
      result = PageContext.REQUEST_SCOPE;
    } else if (SESSION_SCOPE_STRING.equalsIgnoreCase(trimScope)) {
      result = PageContext.SESSION_SCOPE;
    } else if (APPLICATION_SCOPE_STRING.equalsIgnoreCase(trimScope)) {
      result = PageContext.APPLICATION_SCOPE;
    }

    return result;
  }
}
