package ca.bc.gov.srm.farm.ui.tag;

import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * IdentifierTag.
 */
public class IdentifierTag extends VarTagSupport {

  /** Serializable. */
  private static final long serialVersionUID = 1394551190269681386L;

  /** DOCUMENT ME! */
  private String prefix;

  /** constructor. */
  public IdentifierTag() {

    // default to request scope so that ids don't collide across tiles.
    setScope(ScopeTagSupport.REQUEST_SCOPE_STRING);
  }

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

      Set identifiers = (Set) retrieve();

      if (identifiers == null) {
        identifiers = new HashSet();
      }

      String thePrefix = EvalHelper.evalString("prefix", getPrefix(), this,
          pageContext);
      String identifier = thePrefix + identifiers.size();
      identifiers.add(identifier);
      store(identifiers);
      out.print(identifier);
    } catch (IOException e) {
      throw new JspException("Failed to create tag output:  " + e);
    }

    return TagSupport.SKIP_BODY;
  }

  /**
   * @return  prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @param  prefix  the prefix to set
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

}
