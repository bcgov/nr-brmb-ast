/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.cache.CacheFactory;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;


/**
 * GetUserCacheTag.
 */
public abstract class GetUserCacheTag extends VarTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 2261821241481744643L;


  /** Creates a new GetUserCacheTag object. */
  public GetUserCacheTag() {
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

    store(CacheFactory.getUserCache().getItem(getKey()));

    return SKIP_BODY;
  }


  /**
   * It seemed cleaner to put the keys into subclasses, rather than having the
   * keys harded coded into N JSPS.
   *
   * @return  CacheKeys key
   */
  protected abstract String getKey();
}
