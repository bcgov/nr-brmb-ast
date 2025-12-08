/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * TextTagSupport.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public abstract class TextTagSupport extends VarTagSupport {

  private static final long serialVersionUID = -7659814069407389271L;

  /** Creates a new TextTagSupport object. */
  public TextTagSupport() {
    super();
  }

  /**
   * text.
   *
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   */
  public abstract String text() throws JspException;

  /**
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {
    JspWriter out = null;

    try {
      out = pageContext.getOut();

      String text = EvalHelper.evalString("text", text(), this, pageContext);

      if ((text != null) && !text.equals("")) {
        out.print(text);
        store(text);
      }
    } catch (IOException e) {
      throw new JspException("Failed to create tag output:  " + e);
    }

    return TagSupport.SKIP_BODY;
  }

}
