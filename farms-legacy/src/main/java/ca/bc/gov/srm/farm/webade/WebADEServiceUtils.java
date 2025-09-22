/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.webade;

import ca.bc.gov.srm.farm.Organization;
import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.UserBusinessPartnerView;
import ca.bc.gov.srm.farm.UserGovernmentView;
import ca.bc.gov.srm.farm.UserVerifiedIndividualView;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.security.BusinessRole;
import ca.bc.gov.webade.user.BusinessPartnerUserInfo;
import ca.bc.gov.webade.user.GovernmentUserInfo;
import ca.bc.gov.webade.user.IndividualUserInfo;
import ca.bc.gov.webade.user.WebADEUserInfo;


/**
 * WebADEServiceUtils.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 5662 $
 */
public final class WebADEServiceUtils {

  /** instance. */
  private static WebADEServiceUtils instance = new WebADEServiceUtils();

  /** Creates a new WebADEServiceUtils object. */
  private WebADEServiceUtils() {
  }

  /**
   * convertUser.
   *
   * @param   userInfo  The parameter value.
   *
   * @return  The return value.
   */
  public static User convertUser(final WebADEUserInfo userInfo) {
    User result = null;

    if (userInfo != null) {
      result = createUser(userInfo);
      populateUser(result, userInfo);
    }

    return result;
  }


  /**
   * getInstance.
   *
   * @return  The return value.
   */
  public static WebADEServiceUtils getInstance() {
    return instance;
  }

  /**
   * convertAction.
   *
   * @param   action  The parameter value.
   *
   * @return  The return value.
   */
  public BusinessAction convertAction(final ca.bc.gov.webade.Action action) {
    return new BusinessAction(action.getName());
  }


  /**
   * convertActions.
   *
   * @param   actions  The parameter value.
   *
   * @return  The return value.
   */
  public BusinessAction[] convertActions(
    final ca.bc.gov.webade.Action[] actions) {
    BusinessAction[] result = new BusinessAction[actions.length];

    for (int i = 0; i < actions.length; i++) {
      result[i] = convertAction(actions[i]);
    }

    return result;
  }

  /**
   * convertOrganization.
   *
   * @param   organization  The parameter value.
   *
   * @return  The return value.
   */
  public Organization convertOrganization(
    final ca.bc.gov.webade.Organization organization) {
    Organization result = new Organization();
    result.setDescription(organization.getDescription());
    result.setEffectiveDate(organization.getEffectiveDate());
    result.setExpiryDate(organization.getExpiryDate());
    result.setId(new Long(organization.getOrganizationId()));
    result.setName(organization.getName());
    result.setOrganizationCode(organization.getOrganizationCode());
    result.setOrganizationTypeCode(organization.getOrganizationType());
    result.setRevisionCount(new Long(0));

    return result;
  }

  /**
   * convertOrganizations.
   *
   * @param   organizations  The parameter value.
   *
   * @return  The return value.
   */
  public Organization[] convertOrganizations(
    final ca.bc.gov.webade.Organization[] organizations) {
    Organization[] result = new Organization[organizations.length];

    for (int i = 0; i < organizations.length; i++) {
      result[i] = convertOrganization(organizations[i]);
    }

    return result;
  }

  /**
   * convertRole.
   *
   * @param   role  The parameter value.
   *
   * @return  The return value.
   */
  public BusinessRole convertRole(final ca.bc.gov.webade.Role role) {
    return new BusinessRole(role.getName());
  }


  /**
   * convertRoles.
   *
   * @param   roles  The parameter value.
   *
   * @return  The return value.
   */
  public BusinessRole[] convertRoles(final ca.bc.gov.webade.Role[] roles) {
    BusinessRole[] result = new BusinessRole[roles.length];

    for (int i = 0; i < roles.length; i++) {
      result[i] = convertRole(roles[i]);
    }

    return result;
  }

  /**
   * createUser.
   *
   * @param   userInfo  The parameter value.
   *
   * @return  The return value.
   */
  private static User createUser(final WebADEUserInfo userInfo) {
    User result = null;

    if (userInfo instanceof BusinessPartnerUserInfo) {
      result = new UserBusinessPartnerView();
    } else if (userInfo instanceof GovernmentUserInfo) {
      result = new UserGovernmentView();
    } else if (userInfo instanceof IndividualUserInfo) {
      result = new UserVerifiedIndividualView();
    }

    return result;
  }


  /**
   * populateUser.
   *
   * @param  user      The parameter value.
   * @param  userInfo  The parameter value.
   */
  private static void populateUser(final User user,
    final WebADEUserInfo userInfo) {

    // populate normal user fields first, populate user credentials stuff...
    user.setGuid(userInfo.getUserCredentials().getUserGuid().toString());
    user.setUserId(userInfo.getUserCredentials().getUserId());
    user.setAccountName(userInfo.getUserCredentials().getAccountName());
    user.setSourceDirectory(userInfo.getUserCredentials().getSourceDirectory());
    user.setEmailAddress(userInfo.getEmailAddress());
    user.setFirstName(userInfo.getFirstName());
    user.setLastName(userInfo.getLastName());
    user.setMiddleInitial(userInfo.getMiddleInitial());
    user.setUserTypeCode(userInfo.getUserCredentials().getUserTypeCode()
      .getCodeValue());

    // try and change the default display name...
    if ((userInfo.getFirstName() != null) && (userInfo.getLastName() != null)) {
      user.setDisplayName(userInfo.getFirstName() + " "
        + userInfo.getLastName());
    } else {
      user.setDisplayName(userInfo.getDisplayName());
    }

    final int x = 10;
    final int iii = 3;
    final int vi = 6;
    final int vii = 7;

    // try to format the phone number...
    if ((userInfo.getPhoneNumber() != null)
        && (userInfo.getPhoneNumber().length() == x)) {
      String number = "(" + userInfo.getPhoneNumber().substring(0, iii) + ") "
        + userInfo.getPhoneNumber().substring(iii, vi) + "-"
        + userInfo.getPhoneNumber().substring(vi);
      user.setPhoneNumber(number);
    } else if ((userInfo.getPhoneNumber() != null)
        && (userInfo.getPhoneNumber().length() == vii)) {
      String number = userInfo.getPhoneNumber().substring(0, iii) + "-"
        + userInfo.getPhoneNumber().substring(iii);
      user.setPhoneNumber(number);
    } else {
      user.setPhoneNumber(userInfo.getPhoneNumber());
    }

    // now specific types
    if (userInfo instanceof BusinessPartnerUserInfo) {
      BusinessPartnerUserInfo info = (BusinessPartnerUserInfo) userInfo;
      UserBusinessPartnerView u = (UserBusinessPartnerView) user;
      u.setBusinessActivationCode(info.getBusinessActivationCode());
      u.setBusinessGuid(info.getBusinessGUID().toString());
      u.setBusinessLegalName(info.getBusinessLegalName());
    } else if (userInfo instanceof GovernmentUserInfo) {
      GovernmentUserInfo info = (GovernmentUserInfo) userInfo;
      UserGovernmentView u = (UserGovernmentView) user;
      u.setAccountType(info.getUserCredentials().getUserTypeCode().getCodeValue());
      u.setEmployeeId(info.getEmployeeId());
    }
  }
}
