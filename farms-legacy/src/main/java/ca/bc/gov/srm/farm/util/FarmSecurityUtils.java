/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.util;

import javax.servlet.http.HttpServletRequest;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;

/**
 * @author awilkinson
 * @created October 28, 2023
 */
public final class FarmSecurityUtils {

  /** */
  private FarmSecurityUtils() {
  }
  
  /**
   * @param request request
   * @param webadeAction webadeAction
   * @return true is allowed to do action
   * @throws WebADEException on error
   */
  public static boolean canPerformAction(
      final HttpServletRequest request, 
      final String webadeAction) 
  throws WebADEException {
    WebADEUserInfo userInfo = HttpRequestUtils.getCurrentUserInfo(request);
    Application application = HttpRequestUtils.getApplication(
        request.getSession().getServletContext());
    WebADEUserPermissions permissions = application.getWebADEUserPermissions(
        userInfo.getUserCredentials());
    
    boolean authorizedAction = permissions.canPerformAction(
        new ca.bc.gov.webade.Action(webadeAction));
    
    return authorizedAction;
  }

  public static String maskSecret(String value) {
    int length = value.length();
    StringBuilder sb = new StringBuilder(length);
    
    for(int i = 0; i < length; i++) {
      sb.append("*");
    }
    return sb.toString();
  }
}
