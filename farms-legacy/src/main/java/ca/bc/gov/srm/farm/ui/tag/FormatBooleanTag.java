package ca.bc.gov.srm.farm.ui.tag;


import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import javax.servlet.jsp.JspException;


/**
 * FormatBooleanTag.
 */
public final class FormatBooleanTag extends VarTagSupport {

  /** DOCUMENT ME! */
  public static final String DEFAULT_TRUE_VALUE = "Yes";

  /** DOCUMENT ME! */
  public static final String DEFAULT_FALSE_VALUE = "No";

  /** DOCUMENT ME! */
  private static final long serialVersionUID = 1367671209682221114L;

  /** DOCUMENT ME! */
  private String test;

  /** DOCUMENT ME! */
  private String trueValue;

  /** DOCUMENT ME! */
  private String falseValue;

  /** Creates a new FormatBooleanTag object. */
  public FormatBooleanTag() {
    super();
  }

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getFalseValue() {
    return falseValue;
  }

  /**
   * DOCUMENT ME!
   *
   * @param  falseValue  DOCUMENT ME!
   */
  public void setFalseValue(final String falseValue) {
    this.falseValue = falseValue;
  }

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getTest() {
    return test;
  }

  /**
   * DOCUMENT ME!
   *
   * @param  test  DOCUMENT ME!
   */
  public void setTest(final String test) {
    this.test = test;
  }

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public String getTrueValue() {
    return trueValue;
  }

  /**
   * DOCUMENT ME!
   *
   * @param  trueValue  DOCUMENT ME!
   */
  public void setTrueValue(final String trueValue) {
    this.trueValue = trueValue;
  }

  /**
   * @return  DOCUMENT ME!
   *
   * @throws  JspException  DOCUMENT ME!
   *
   * @see     javax.servlet.jsp.tagext.TagSupport#doStartTag()
   */
  @Override
  public int doStartTag() throws JspException {

    if (StringUtils.isBlank(test)) {
      throw new JspException("The test attribute must be specified");
    }

    Boolean evalTest = EvalHelper.evalBoolean("test", test, this, pageContext);

    Object evalTrueValue = EvalHelper.eval("trueValue", trueValue, this,
        pageContext);

    if (evalTrueValue == null) {
      evalTrueValue = DEFAULT_TRUE_VALUE;
    }

    if (evalTrueValue instanceof String) {
      evalTrueValue = StringUtils.defaultIfEmpty((String) (evalTrueValue),
          DEFAULT_TRUE_VALUE);
    }

    Object evalFalseValue = EvalHelper.evalString("falseValue", falseValue,
        this, pageContext);

    if (evalFalseValue == null) {
      evalFalseValue = DEFAULT_FALSE_VALUE;
    }

    if (evalFalseValue instanceof String) {
      evalFalseValue = StringUtils.defaultIfEmpty((String) (evalFalseValue),
          DEFAULT_FALSE_VALUE);
    }

    Object objValue;

    if (evalTest == null) {
      objValue = ""; // never get here - evalTest is always non-null
    } else if (evalTest.booleanValue()) {
      objValue = evalTrueValue;
    } else {
      objValue = evalFalseValue;
    }

    String varName = getVar();
    int scope = getScopeValue();

    if (StringUtils.isBlank(varName)) {

      try {
        pageContext.getOut().print(objValue);
      } catch (IOException e) {
        throw new JspException(e.getMessage());
      }
    } else {
      pageContext.setAttribute(varName, objValue, scope);
    }

    return SKIP_BODY;
  }
}
