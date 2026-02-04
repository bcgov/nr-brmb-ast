/**
 * Copyright (c) 2011,
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

import ca.bc.gov.srm.farm.cache.CacheFactory;

/**
 * @author awilkinson
 * @created Mar 29, 2011
 */
public class GetApplicationCacheTag extends VarTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 2261648541481744643L;
  
  /** application cache key */
  private String key;


  /** Creates a new GetUserCacheTag object. */
  public GetApplicationCacheTag() {
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

    store(CacheFactory.getApplicationCache().getItem(getKey()));

    return SKIP_BODY;
  }

  /**
   * @return the key
   */
  public String getKey() {
    return key;
  }

  /**
   * @param key the key to set the value to
   */
  public void setKey(String key) {
    this.key = key;
  }


}
