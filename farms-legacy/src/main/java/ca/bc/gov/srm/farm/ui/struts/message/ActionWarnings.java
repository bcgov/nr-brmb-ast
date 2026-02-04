/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.struts.message;

import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * ActionWarnings.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class ActionWarnings extends ActionMessages {

  /** serialVersionUID. */
  private static final long serialVersionUID = 7575556425452619004L;

  /** WARNING_KEY. */
  public static final String WARNING_KEY = "ca.bc.gov.struts.action.WARNING";

  /**
   * Adds the specified warnings keys into the appropriate request attribute for
   * use by the &lt;bcgov:warnings&gt; tag, if any warnings are required.
   * Initialize the attribute if it has not already been. Otherwise, ensure that
   * the request attribute is not set.
   *
   * @param  request   The servlet request we are processing
   * @param  warnings  Warnings object
   *
   * @since  1.0
   */
  public static void addWarnings(final HttpServletRequest request,
    final ActionMessages warnings) {

    if (warnings == null) {

      // bad programmer! *slap*
      return;
    }

    // get any existing warnings from the request, or make a new one
    ActionMessages requestWarnings = (ActionMessages) request.getAttribute(
        WARNING_KEY);

    if (requestWarnings == null) {
      requestWarnings = new ActionMessages();
    }

    // add incoming warnings
    requestWarnings.add(warnings);

    // if still empty, just wipe it out from the request
    if (requestWarnings.isEmpty()) {
      request.removeAttribute(WARNING_KEY);

      return;
    }

    // Save the warnings
    request.setAttribute(WARNING_KEY, requestWarnings);
  }

  /**
   * Retrieves any existing warnings placed in the request by previous actions.
   * This method could be called instead of creating a <code>new
   * ActionMessages() <code>at the beginning of an <code>Action<code>This will
   * prevent saveWarnings() from wiping out any existing Warnings</code></code></code></code>
   *
   * @return  the Warnings that already exist in the request, or a new
   *          ActionMessages object if empty.
   *
   * @param   request  The servlet request we are processing
   *
   * @since   1.0
   */
  public static ActionMessages getWarnings(final HttpServletRequest request) {
    ActionMessages warnings = (ActionMessages) request.getAttribute(
        WARNING_KEY);

    if (warnings == null) {
      warnings = new ActionMessages();
    }

    return warnings;
  }

  /**
   * <p>Save the specified warnings keys into the appropriate request attribute
   * for use by the &lt;bcgov:warnings&gt; tag, if any warnings are required.
   * Otherwise, ensure that the request attribute is not created.</p>
   *
   * @param  request   The servlet request we are processing
   * @param  warnings  Warnings object
   *
   * @since  1.0
   */
  public static void saveWarnings(final HttpServletRequest request,
    final ActionMessages warnings) {

    // Remove any warnings attribute if none are required
    if ((warnings == null) || warnings.isEmpty()) {
      request.removeAttribute(WARNING_KEY);

      return;
    }

    // Save the warnings we need
    request.setAttribute(WARNING_KEY, warnings);
  }

  /**
   * <p>Save the specified warnings keys into the appropriate session attribute
   * for use by the &lt;bcgov:warnings&gt; tag (if warnings="true" is set), if
   * any warnings are required. Otherwise, ensure that the session attribute is
   * not created.</p>
   *
   * @param  session   The session to save the warnings in.
   * @param  warnings  The warnings to save. <code>null</code> or empty warnings
   *                   removes any existing ActionMessages in the session.
   *
   * @since  1.0
   */
  public static void saveWarnings(final HttpSession session,
    final ActionMessages warnings) {

    // Remove any warnings attribute if none are required
    if ((warnings == null) || warnings.isEmpty()) {
      session.removeAttribute(WARNING_KEY);

      return;
    }

    // Save the warnings we need
    session.setAttribute(WARNING_KEY, warnings);
  }

}
