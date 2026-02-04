/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.webade;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.sort.SortUtils;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.service.UserInfoServiceException;


/**
 * WebADEProviderUtils.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public final class WebADEProviderUtils {

  /** Creates a new WebADEProviderUtils object. */
  private WebADEProviderUtils() {
  }


  /**
   * getPermissions.
   *
   * @param   app   Input parameter.
   * @param   user  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  WebADEException  On exception.
   */
  public static WebADEUserPermissions getPermissions(final Application app,
    final User user) throws WebADEException {
    return getPermissions(app, user.getGuid());
  }


  /**
   * getPermissions.
   *
   * @param   app       Input parameter.
   * @param   userGuid  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  WebADEException  On exception.
   */
  public static WebADEUserPermissions getPermissions(final Application app,
    final String userGuid) throws WebADEException {
    WebADEUserPermissions result = null;

    // find webade user for our user
    result = app.getWebADEUserPermissions(getUserCredentials(userGuid));

    return result;
  }


  /**
   * getUserCredentials.
   *
   * @param   user  Input parameter.
   *
   * @return  The return value.
   */
  public static UserCredentials getUserCredentials(final User user) {
    UserCredentials result = new UserCredentials();
    GUID guid = new GUID(user.getGuid());
    result.setUserGuid(guid);
    result.setAccountName(user.getAccountName());
    result.setSourceDirectory(user.getSourceDirectory());

    return result;
  }


  /**
   * getUserCredentials.
   *
   * @param   userGuid  Input parameter.
   *
   * @return  The return value.
   */
  public static UserCredentials getUserCredentials(final String userGuid) {
    UserCredentials result = new UserCredentials();
    GUID guid = new GUID(userGuid);
    result.setUserGuid(guid);

    return result;
  }


  /**
   * getUserInfo.
   *
   * @param   app   Input parameter.
   * @param   user  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  WebADEException  On exception.
   */
  public static WebADEUserInfo getUserInfo(final Application app,
    final User user) throws WebADEException {
    return getUserInfo(app, user.getGuid());
  }

  /**
   * getUserInfo.
   *
   * @param   app       Input parameter.
   * @param   userGuid  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  WebADEException  On exception.
   */
  public static WebADEUserInfo getUserInfo(final Application app,
    final String userGuid) throws WebADEException {
    UserCredentials credentials = app.getWebADEUserPermissions(
        getUserCredentials(userGuid)).getUserCredentials();

    try {
      return app.getUserInfoService().getWebADEUserInfo(credentials);
    } catch (UserInfoServiceException e) {
      throw new WebADEException(e);
    }
  }

  /**
   * sort.
   *
   * @param   users  The parameter value.
   *
   * @return  The return value.
   */
  public static WebADEUserInfo[] sort(final WebADEUserInfo[] users) {
    String[] sortOrder = new String[] {"userCredentials.userId"};

    return (WebADEUserInfo[]) SortUtils.sort(users, sortOrder);
  }

}
