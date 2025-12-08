/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.webade;

import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.AppRoles;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Role;

import java.util.HashMap;
import java.util.Map;


/**
 * WebADERequest.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class WebADERequest {

  /** INSTANCE. */
  private static final WebADERequest INSTANCE = new WebADERequest();

  /** application. */
  private Application application = null;

  /** roleActions. */
  private Map roleActions = null;

  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static final WebADERequest getInstance() {
    return INSTANCE;
  }

  /**
   * getActions.
   *
   * @param   role  Input parameter.
   *
   * @return  The return value.
   */
  public final Action[] getActions(final Role role) {
    return (Action[]) INSTANCE.roleActions.get(role.getName());
  }

  /**
   * getApplication.
   *
   * @return  The return value.
   */
  public final Application getApplication() {
    return INSTANCE.application;
  }


  /**
   * Sets the value for application.
   *
   * @param  app  Input parameter.
   */
  public final void setApplication(final Application app) {
    INSTANCE.application = app;
    INSTANCE.roleActions = new HashMap();

    AppRoles roles = app.getRoles();
    String[] roleNames = roles.getRoleNames();

    for (int i = 0; i < roleNames.length; i++) {
      Role role = roles.getRole(roleNames[i]);
      Action[] actions = role.getActions();
      INSTANCE.roleActions.put(role.getName(), actions);
    }

  }
}
