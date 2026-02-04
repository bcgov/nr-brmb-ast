package ca.bc.gov.srm.farm.service;

import ca.bc.gov.webade.WebADEException;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Service to perform the WebADE interactions.
 *
 * @author   dzwiers
 * @version  1.0
 * @created  16-Sep-2009 4:52:35 PM
 */
public interface SecurityService {

  /**
   * Checks if the current user can perform the action specified.
   *
   * @return  true when the user can perform the action specified
   *
   * @param   actionName  The action against which the user should be checked
   * @param   actionUser  actionUser
   * @param   actionUserDirectory actionUserDirectory
   */
  boolean canPerformAction(final String actionName, final String actionUser, final String actionUserDirectory);

  /**
   * Gets a connection from the WebADE pool for the action specified.
   *
   * @return  an SQL connection if one exists for the action, null otherwise.
   *
   * @param   actionName  The action against which the connection should be
   *                      created.
   * @param   actionUser  actionUser
   * @param   actionUserDirectory actionUserDirectory
   *
   * @throws  SQLException     An exception indicating the under-laying
   *                           connection problem if one occurred.
   * @throws  WebADEException  An exception indicating the under-laying
   *                           connection problem if one occurred.
   */
  Connection getConnectionByAction(final String actionName, final String actionUser, final String actionUserDirectory) throws SQLException,
    WebADEException;
  

  /**
   * 
   * @param setName String
   * @param pref String
   * @return String
   */
  String getWebadePref(final String setName, final String pref);

}
