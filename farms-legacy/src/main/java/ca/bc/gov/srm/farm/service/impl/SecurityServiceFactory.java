/**
 *
 * Copyright (c) 2006,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.service.SecurityService;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferenceSet;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserPermissions;


/**
 * Builds or finds an instance of SecurityService.
 *
 * @author   $author$
 * @version  $Revision: 5660 $, $Date: 2024-11-22 16:15:03 -0800 (Fri, 22 Nov 2024) $
 */
public final class SecurityServiceFactory implements SecurityService {

  /**  */
  private static Logger logger = LoggerFactory.getLogger(SecurityServiceFactory.class);

  /** Creates a new SecurityServiceFactory object. */
  private SecurityServiceFactory() {
  }

  /** Factory Pattern. */
  private static SecurityService instance = new SecurityServiceFactory();

  /**
   * Checks if the current user can perform the action specified.
   *
   * @param   actionName  The action against which the user should be checked
   * @param   actionUser  actionUser
   * @param   actionUserDirectory actionUserDirectory
   *
   * @return  true when the user can perform the action specified
   */
  @Override
  public boolean canPerformAction(final String actionName, final String actionUser, final String actionUserDirectory) {
    logger.debug("Testing for action  " + actionName);

    try {
      WebADEUserPermissions usr = getUserPermissions(actionUser, actionUserDirectory);
      Action act = new Action(actionName);
      if(usr != null){
    	  return usr.canPerformAction(act);
      }
    } catch (RuntimeException e) {
        logger.error("Unexpected error: ", e);
	}
    return false;
  }

  /**
   * Gets a connection from the webade pool for the action specified.
   *
   * @param   actionName  The action against which the connection should be
   *                      created.
   * @param   actionUser  actionUser
   * @param   actionUserDirectory actionUserDirectory
   *
   * @return  an SQL connection if one exists for the action, null otherwise.
   *
   *          <p>@ throws WebADEException An exception indicating the
   *          under-laying security problem if one occurred.</p>
   *
   * @throws  SQLException     An exception indicating the under-laying
   *                           connection problem if one occurred.
   * @throws  WebADEException  An exception indicating the under-laying
   *                           connection problem if one occurred.
   */
  @Override
  public Connection getConnectionByAction(final String actionName, final String actionUser, final String actionUserDirectory)
    throws WebADEException, SQLException {
    logger.debug("Get Connection for action  " + actionName);

    try {
      WebADEUserPermissions usr = getUserPermissions(actionUser, actionUserDirectory);
      Action act = new Action(actionName);
      Connection c = getApp().getConnectionByAction(usr, act);
      logger.debug("Connection is null? " + (c == null));

      if(c!=null){
        c.setAutoCommit(false);
      }
      return c;
    } catch (RuntimeException e) {
      logger.error("Unexpected error: ", e);
    }

    return null;
  }

  /**
   * 
   * @return Application
   */
  private Application getApp(){
  	Servlet s = (Servlet) MessageContext.getCurrentContext().getProperty(
          HTTPConstants.MC_HTTP_SERVLET);
    ServletContext sc = s.getServletConfig().getServletContext();
    return HttpRequestUtils.getApplication(sc);
  }
  
  /**
   * 
   * @param accountName String
   * @param sourceDirectory String
   * @return WebADEUserPermissions
   */
  private WebADEUserPermissions getUserPermissions(final String accountName, final String sourceDirectory) {
	WebADEUserPermissions perms = null;  
    try {
        Servlet s = (Servlet) MessageContext.getCurrentContext().getProperty(
          HTTPConstants.MC_HTTP_SERVLET);
      ServletContext sc = s.getServletConfig().getServletContext();
      Application a = HttpRequestUtils.getApplication(sc);
      UserCredentials uc = new UserCredentials();
      uc.setAccountName(accountName);
      uc.setSourceDirectory(sourceDirectory);
      perms = a.getWebADEUserPermissions(uc);
	} catch (WebADEException e) {
		e.printStackTrace();
	}
	return perms;
  }
  
  
  /**
   * Builds or finds an instance of SecurityService.
   *
   * @return  A SecurityService
   */
  public static SecurityService getInstance() {
    return instance;
  }

  /**
   * @param   setName  Set Name
   * @param   pref     Pref Name
   *
   * @return  The servlet pref if found, null otherwise
   */
  public String getServPref(final String setName, final String pref) {

    logger.debug("Get pref  " + setName + ":" + pref);

    Servlet s = (Servlet) MessageContext.getCurrentContext().getProperty(
        HTTPConstants.MC_HTTP_SERVLET);
    String v = s.getServletConfig().getInitParameter(pref);
    logger.debug("Got Pref Value " + v);

    return v;

  }

  /**
   * @param   setName  Webade Set Name
   * @param   pref     Webade Pref Name
   *
   * @return  The webade pref if found, null otherwise
   */
  @Override
  public String getWebadePref(final String setName, final String pref) {

    logger.debug("Get pref  " + setName + ":" + pref);

    WebADEPreference r = null;

    try {
      Servlet s = (Servlet) MessageContext.getCurrentContext().getProperty(
          HTTPConstants.MC_HTTP_SERVLET);
      ServletContext sc = s.getServletConfig().getServletContext();
      Application a = HttpRequestUtils.getApplication(sc);
      WebADEPreferences prefs = a.getWebADEApplicationPreferences();
      WebADEPreferenceSet ps = prefs.getPreferenceSet("app-config",
          setName);

      if (ps != null) {
        r = ps.getPreference(pref);
      }

      if (r == null) {
        r = prefs.getPreference("app-config", pref);
      }

    } catch (WebADEException e) {
      logger.error("Unexpected error: ", e);
    } catch (RuntimeException e) {
      logger.error("Unexpected error: ", e);
      r = null;
    }

    if (r == null) {
      return null;
    }

    logger.debug("Got: " + r.getPreferenceValue());

    return r.getPreferenceValue();
  }

 }
