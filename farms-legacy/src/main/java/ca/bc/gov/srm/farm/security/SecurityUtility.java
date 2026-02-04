/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.factory.ObjectFactory;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.user.UserProvider;


/**
 * SecurityUtility.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public abstract class SecurityUtility {

  /** instance. */
  private static SecurityUtility instance = null;

  /** initialized. */
  private boolean initialized = false;

  /** log. */
  private Logger log = LoggerFactory.getLogger(getClass());

  /** providers. */
  private Map<String, SecurityProvider> providers = new LinkedHashMap<>();

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  public abstract void initialize(Object resource);

  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static SecurityUtility getInstance() {

    if (instance == null) {
      instance = (SecurityUtility) ObjectFactory.createObject(
          SecurityUtility.class, null);
    }

    return instance;
  }

  /**
   * canPerformAction.
   *
   * @param   businessAction  The parameter value.
   * @param   user            The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public boolean canPerformAction(final BusinessAction businessAction,
    final User user) throws UtilityException {
    checkInitialized();

    boolean result = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !result) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.canPerformAction(businessAction, user);

      }
    }

    return result;
  }

  /**
   * canPerformAction.
   *
   * @param   businessActionName  The parameter value.
   * @param   user                The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public boolean canPerformAction(final String businessActionName,
    final User user) throws UtilityException {
    checkInitialized();

    boolean result = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !result) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.canPerformAction(businessActionName, user);

      }
    }

    return result;
  }

  /**
   * getAdministrators.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public UserListView[] getAdministrators() throws UtilityException {
    checkInitialized();

    return getUsersInRole(BusinessRole.administrator());
  }
  
  public UserListView[] getVerifiers() throws UtilityException {
    checkInitialized();

    List<BusinessRole> businessRoles = new ArrayList<>();
    businessRoles.add(BusinessRole.administrator());
    businessRoles.add(BusinessRole.seniorVerifier());
    businessRoles.add(BusinessRole.verifier());
    
    UserListView[] users = getUsersInRoles(businessRoles);
    
    return users;
  }

  /**
   * getBusinessRoles.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  public BusinessRole[] getBusinessRoles() throws ProviderException {
    checkInitialized();

    List<BusinessRole> roles = new ArrayList<>();
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        BusinessRole[] roleArray = provider.getBusinessRoles();

        for (int i = 0; i < roleArray.length; i++) {

          if (!roles.contains(roleArray[i])) {
            roles.add(roleArray[i]);
          }
        }

      }
    }

    return roles.toArray(new BusinessRole[roles.size()]);
  }

  /**
   * getBusinessRolesForUser.
   *
   * @param   user  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public BusinessRole[] getBusinessRolesForUser(final User user)
    throws UtilityException {
    checkInitialized();

    List<BusinessRole> roles = new ArrayList<>();
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext()) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        BusinessRole[] roleArray = provider.getBusinessRolesForUser(user);

        for (int i = 0; i < roleArray.length; i++) {

          if (!roles.contains(roleArray[i])) {
            roles.add(roleArray[i]);
          }
        }

      }
    }

    return roles.toArray(new BusinessRole[roles.size()]);
  }


  /**
   * getSecurityRule.
   *
   * @param   businessActionName  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public SecurityRule getSecurityRule(final String businessActionName)
    throws UtilityException {
    checkInitialized();

    SecurityRule result = null;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && (result == null)) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.getSecurityRule(businessActionName);

      }
    }

    return result;

  }

  /**
   * getSecurityRule.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public SecurityRule getSecurityRule(final BusinessAction businessAction)
    throws UtilityException {
    checkInitialized();

    SecurityRule result = null;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && (result == null)) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.getSecurityRule(businessAction);

      }
    }

    return result;
  }

  /**
   * initializeProvider.
   *
   * @param  providerKey  The parameter value.
   * @param  resource     The parameter value.
   */
  public void initializeProvider(final String providerKey,
    final Object resource) {
    SecurityProvider provider = providers.get(providerKey);

    if (provider != null) {
      provider.initialize(resource);
    }
  }

  /**
   * isInitialized.
   *
   * @return  The return value.
   */
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * isInRole.
   *
   * @param   businessRole  Input parameter.
   * @param   user          Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public boolean isInRole(final BusinessRole businessRole, final User user)
    throws UtilityException {
    checkInitialized();

    boolean result = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !result) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.isInRole(businessRole, user);

      }
    }

    return result;
  }

  /**
   * isInRole.
   *
   * @param   businessRoleName  Input parameter.
   * @param   user              Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  public boolean isInRole(final String businessRoleName, final User user)
    throws UtilityException {
    checkInitialized();

    boolean result = false;
    Iterator<String> iter = providers.keySet().iterator();

    while (iter.hasNext() && !result) {
      String providerKey = iter.next();
      SecurityProvider provider = providers.get(providerKey);

      if (!provider.isInitialized()) {
        log.warn("security provider '" + providerKey + "' is not initialized.");
      } else {
        result = provider.isInRole(businessRoleName, user);

      }
    }

    return result;
  }

  /** checkInitialized. */
  protected void checkInitialized() {

    if (!isInitialized()) {
      log.error(getClass() + " not initialized.");
      throw new RuntimeException(getClass() + " not initialized.");
    }
  }

  private UserListView[] getUsersInRole(final BusinessRole role)
      throws UtilityException {
    
    List<BusinessRole> roles = new ArrayList<>();
    roles.add(role);
    return getUsersInRoles(roles);
  }
  
  /**
   * getUsersInRole.
   *
   * @param   role  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  UtilityException  On exception.
   */
  private UserListView[] getUsersInRoles(final List<BusinessRole> roles)
    throws UtilityException {
    checkInitialized();

    UserListView[] users = new UserListView[0];
    UserProvider provider = (UserProvider) ObjectFactory.createObject(
        UserProvider.class, null);
    users = provider.getUserList(roles);

    return users;
  }

  /**
   * getProviders.
   *
   * @return  The return value
   */
  public Map<String, SecurityProvider> getProviders() {
    return providers;
  }

  /**
   * setProviders.
   *
   * @param  providers  Set providers
   */
  public void setProviders(Map<String, SecurityProvider> providers) {
    this.providers = providers;
  }

  /**
   * setInitialized.
   *
   * @param  initialized  Set initialized
   */
  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }
}
