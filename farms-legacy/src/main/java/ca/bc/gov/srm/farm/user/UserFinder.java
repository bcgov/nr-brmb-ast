package ca.bc.gov.srm.farm.user;

import ca.bc.gov.srm.farm.security.BusinessRole;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.AppRoles;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Role;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.UserSearchQuery;
import ca.bc.gov.webade.user.service.UserInfoService;
import ca.bc.gov.webade.user.service.UserInfoServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
public final class UserFinder {

  /** DOCUMENT ME! */
  private static Logger log = LoggerFactory.getLogger(UserFinder.class);

  /** constructor. */
  private UserFinder() {
  }

  /**
   * findUserById.
   *
   * @param   userId    The parameter value.
   * @param   userGuid  The parameter value.
   *
   * @return  The return value.
   *
   * @throws  UserInfoServiceException  On exception.
   */
  public static WebADEUserInfo findUserById(final String userId,
    final String userGuid) throws UserInfoServiceException {
    Application app = WebADERequest.getInstance().getApplication();
    UserInfoService userInfoService = app.getUserInfoService();
    String[] sourceDirectories =
      userInfoService.getSupportedSourceDirectories();

    String choppedUserId = userId.substring(userId.indexOf("\\") + 1);
    log.debug("choppedUserId " + choppedUserId);

    for (int i = 0; i < sourceDirectories.length; i++) {
      log.debug("searching " + sourceDirectories[i]);

      UserTypeCode userTypeCode = userInfoService.getUserTypeForSourceDirectory(
          sourceDirectories[i]);
      UserSearchQuery userSearchQuery = userInfoService.createUserSearchQuery(
          userTypeCode);

      userSearchQuery.getUserId().setSearchValue(choppedUserId);

      WebADEUserInfo[] webadeUsers = userInfoService.findUsers(userSearchQuery);

      log.debug("webadeUsers.length: " + webadeUsers.length);

      for (int ii = 0; ii < webadeUsers.length; ii++) {
        log.debug("GUID: "
          + webadeUsers[ii].getUserCredentials().getUserGuid()
          .toMicrosoftGUIDString());

        if (webadeUsers[ii].getUserCredentials().getUserGuid()
            .toMicrosoftGUIDString().equals(userGuid)) {
          return webadeUsers[ii];
        }
      }
    }

    return null;
  }


  /**
   * @param   userGuid  userGuid
   *
   * @return  WebADEUserInfo
   *
   * @throws  UserInfoServiceException  on exception
   */
  public static WebADEUserInfo findUserByGuid(final String userGuid)
    throws UserInfoServiceException {
    Application app = WebADERequest.getInstance().getApplication();
    AppRoles roles = app.getRoles();
    String[] roleNames = {BusinessRole.user().getRoleName()};

    try {

      for (int ii = 0; ii < roleNames.length; ii++) {
        Role role = roles.getRole(roleNames[ii]);
        UserCredentials[] creds = app.getAuthorizedUsers(role);

        for (int jj = 0; jj < creds.length; jj++) {

          if (creds[jj].getUserGuid().toMicrosoftGUIDString().equals(
                userGuid)) {
            WebADEUserInfo info = app.getUserInfoService().getWebADEUserInfo(
                creds[jj]);

            return info;
          }
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new UserInfoServiceException(ex.getMessage());
    }

    return null;
  }
}
