/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.list.CodeListView;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;


/**
 * GetCodeDescriptionTag.
 */
public class GetCodeDescriptionTag extends TextTagSupport {

  /** DOCUMENT ME! */
  private static final long serialVersionUID = -5128369727193574521L;

  /** DOCUMENT ME! */
  private String code;

  /** DOCUMENT ME! */
  private String list;

  /** Creates a new GetCodeDescriptionTag object. */
  public GetCodeDescriptionTag() {
    super();
  }

  /**
   * @return  the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param  code  the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return  the list
   */
  public String getList() {
    return list;
  }

  /**
   * @param  list  the list to set
   */
  public void setList(String list) {
    this.list = list;
  }

  /**
   * doStartTag.
   *
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   */
  @Override
  public String text() throws JspException {

    if (StringUtils.isBlank(getCode())) {
      throw new JspException("The code attribute must be specified");
    }

    if (StringUtils.isBlank(getList())) {
      throw new JspException("The list attribute must be specified");
    }

    Cache cache = CacheFactory.getApplicationCache();
    CodeListView[] codeList = (CodeListView[]) cache.getItem(getList());
    String codeDescription = null;
    String codeValue = EvalHelper.evalString("code", code, this, pageContext);

    for (int ii = 0; ii < codeList.length; ii++) {

      if (codeList[ii].getValue().equals(codeValue)) {
        codeDescription = codeList[ii].getLabel();

        break;
      }
    }

    return codeDescription;
  }

}
