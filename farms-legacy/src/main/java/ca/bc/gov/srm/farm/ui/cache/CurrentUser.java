/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.cache;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.exception.UtilityException;
import ca.bc.gov.srm.farm.security.BusinessRole;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.webade.WebADEServiceUtils;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.user.WebADEUserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


/**
 * CurrentUser.
 */
public final class CurrentUser {

  /** log. */
  private static Logger log = LoggerFactory.getLogger(CurrentUser.class);

  /** Creates a new CurrentUser object. */
  private CurrentUser() {
  }

  /**
   * getUser.
   *
   * @return  The return value.
   */
  public static User getUser() {
    User currentUser = (User) CacheFactory.getUserCache().getItem(
        CacheKeys.CURRENT_USER);

    return currentUser;
  }

  /**
   * isAdministrator.
   *
   * @return  The return value.
   */
  public static boolean isAdministrator() {
    return isInRole(BusinessRole.administrator());
  }

  /**
   * Sets the value for user.
   *
   * @param  request  Input parameter.
   */
  public static void setUser(final HttpServletRequest request) {
    WebADEUserInfo userInfo = HttpRequestUtils.getCurrentUserInfo(request);
    User currentUser = WebADEServiceUtils.convertUser(userInfo);

    if (log.isDebugEnabled()) {
      log.debug("adding current user (" + currentUser.getUserId()
        + ") to the cache...");
    }

    CacheFactory.getUserCache().addItem(CacheKeys.CURRENT_USER, currentUser);
  }

  /**
   * isInRole.
   *
   * @param   role  Input parameter.
   *
   * @return  The return value.
   */
  private static boolean isInRole(final BusinessRole role) {
    boolean result = false;
    User user = getUser();

    try {
      result = SecurityUtility.getInstance().isInRole(role, user);
    } catch (UtilityException e) {

      if (log.isErrorEnabled()) {
        log.error("Error checking if current user in role (" + role + "): "
          + e.getMessage(), e);
      }
    }

    return result;
  }
}
