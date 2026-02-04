/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.user;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.security.BusinessRole;
import ca.bc.gov.srm.farm.webade.WebADEProviderUtils;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.srm.farm.webade.WebADEServiceUtils;
import ca.bc.gov.webade.AppRoles;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Role;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.WildcardOptions;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.UserSearchQuery;
import ca.bc.gov.webade.user.service.UserInfoService;
import ca.bc.gov.webade.user.service.UserInfoServiceException;


/**
 * WebADEUserProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
final class WebADEUserProvider implements UserProvider {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * getSourceDirectoryList.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public ListView[] getSourceDirectoryList() throws ProviderException {
    List<CodeListView> list = new ArrayList<>();

    try {
      Application app = WebADERequest.getInstance().getApplication();
      UserInfoService userInfoService = app.getUserInfoService();
      String[] sourceDirectories =
        userInfoService.getSupportedSourceDirectories();

      for (int i = 0; i < sourceDirectories.length; i++) {
        UserTypeCode userTypeCode =
          userInfoService.getUserTypeForSourceDirectory(sourceDirectories[i]);
        String label = userTypeCode.getDescription() + " ("
          + sourceDirectories[i] + ")";
        String value = sourceDirectories[i];

        CodeListView sourceDirectory = new CodeListView(value, label);
        list.add(sourceDirectory);
      }
    } catch (UserInfoServiceException e) {
      throw new ProviderException(e.getMessage(), e);
    }

    ListView[] result = list.toArray(new ListView[list.size()]);

    return result;
  }

  /**
   * getUserList.
   *
   * @param   businessRole  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public UserListView[] getUserList(final BusinessRole businessRole)
    throws ProviderException {
    
    List<BusinessRole> businessRoles = new ArrayList<>();
    businessRoles.add(businessRole);
    
    return getUserList(businessRoles);
  }

  /**
   * getUserList.
   *
   * @param   businessRole  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public UserListView[] getUserList(final List<BusinessRole> businessRoles)
    throws ProviderException {
    logMethodStart(logger);
    logger.debug("Loading authorized users for roles: " + businessRoles);
    
    List<UserListView> result = new ArrayList<>();

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();

      // find the webade role...
      AppRoles roles = app.getRoles();
      
      for(BusinessRole businessRole : businessRoles) {
        Role role = roles.getRole(businessRole.getRoleName());
  
        //get a list of webade users authorized to that role...
        UserCredentials[] creds = app.getAuthorizedUsers(role);
        
        logger.debug("Loaded authorized users for role: " + role);
  
        //convert them and add to the result...
        for (UserCredentials cred : creds) {
          logger.debug("Loaded user credentials: " + cred);
          
          boolean alreadyInTheList = result.stream().anyMatch(u -> u.getGuid().equals(cred.getUserGuid().toString()));
          if(!alreadyInTheList) {
            WebADEUserInfo info = app.getUserInfoService().getWebADEUserInfo(cred);
            
            if(info == null) {
              logger.debug("UserInfoService.getWebADEUserInfo returned null for: " + cred + ". The user is no longer active. Skipping.");
            } else {
              logger.debug("Loaded user info. Account Name: " + info.getUserCredentials().getSourceDirectory() + "\\" +
                  info.getUserCredentials().getAccountName() +
                  ", Display Name: " + info.getDisplayName(),
                  ", GUID: " + info.getUserCredentials().getUserGuid().toString());
              User user = WebADEServiceUtils.convertUser(info);
              result.add(new UserListView(user));
            }
          }
        }
        
      }

    } catch (WebADEException ex) {
      throw new ProviderException(ex);
    } catch (UserInfoServiceException e) {
      throw new ProviderException(e);
    }

    logMethodEnd(logger, result);
    return result.toArray(new UserListView[result.size()]);
  }

  /**
   * search.
   *
   * @param   sourceDirectory  The parameter value.
   * @param   firstName        The parameter value.
   * @param   lastName         The parameter value.
   * @param   email            The parameter value.
   * @param   startRow         The parameter value.
   * @param   rowCount         pageSize The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public UserSearchResults search(final String sourceDirectory,
    final String firstName, final String lastName, final String email,
    final int startRow, final int rowCount) throws ProviderException {
    boolean startRowInitialized = false;
    int resultsStartRow = 0;
    int resultsNextRow = 0;
    int resultsTotalRows = 0;
    List<UserListView> resultsList = new ArrayList<>();

    try {
      Application app = WebADERequest.getInstance().getApplication();
      UserInfoService userInfoService = app.getUserInfoService();
      UserTypeCode userTypeCode = userInfoService.getUserTypeForSourceDirectory(
          sourceDirectory);
      UserSearchQuery userSearchQuery = userInfoService.createUserSearchQuery(
          userTypeCode);

      if (StringUtils.isNotBlank(firstName)) {
        userSearchQuery.getFirstName().setSearchValue(firstName);
        userSearchQuery.getFirstName().setWildcardOption(
          WildcardOptions.WILDCARD_RIGHT);
      }

      if (StringUtils.isNotBlank(lastName)) {
        userSearchQuery.getLastName().setSearchValue(lastName);
        userSearchQuery.getLastName().setWildcardOption(
          WildcardOptions.WILDCARD_RIGHT);
      }

      WebADEUserInfo[] webadeUsers = userInfoService.findUsers(userSearchQuery);
      webadeUsers = WebADEProviderUtils.sort(webadeUsers);
      resultsTotalRows = webadeUsers.length;

      for (int i = startRow;
          (i < resultsTotalRows) && (resultsList.size() < rowCount); i++) {
        WebADEUserInfo user = webadeUsers[i];

        if (user.isVisible()) {

          if (!startRowInitialized) {
            startRowInitialized = true;
            resultsStartRow = i;
          }

          resultsList.add(new UserListView(
              WebADEServiceUtils.convertUser(webadeUsers[i])));
          resultsNextRow = i + 1;
        }
      }
    } catch (Exception e) {
      throw new ProviderException(e);
    }

    UserListView[] users = resultsList.toArray(
        new UserListView[resultsList.size()]);

    UserSearchResults result = new UserSearchResults(users, resultsTotalRows,
        resultsStartRow, resultsNextRow);

    return result;
  }


}
