/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;

import org.apache.taglibs.standard.tag.common.core.NullAttributeException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;


/**
 * IfUserCanPerformActionTag.
 *
 * <p>Logic tag to prevent execution of a block of code if user does not have //
 * permissions to the given action.</p>
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
public class IfUserCanPerformActionTag extends ConditionalTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 9124836017939555452L;

  /** action. */
  private String action;

  /**
   * Sets the value for action.
   *
   * @param  value  Input parameter.
   */
  public void setAction(final String value) {
    this.action = value;
  }

  /**
   * condition.
   *
   * @return  The return value.
   *
   * @throws  JspTagException  On exception.
   */
  @Override
  protected boolean condition() throws JspTagException {

    try {
      boolean canPerformAction = false;

      if (action == null) {
        throw new NullAttributeException("ifUserCanPerformAction", "action");
      }

      SecurityUtility security = SecurityUtility.getInstance();
      User currentUser = CurrentUser.getUser();
      canPerformAction = security.canPerformAction(action, currentUser);

      return canPerformAction;
    } catch (UtilityException e) {
      throw new JspTagException("Error checking security: " + e.getMessage());
    } catch (JspException ex) {
      throw new JspTagException(ex.toString());
    }
  }

}
