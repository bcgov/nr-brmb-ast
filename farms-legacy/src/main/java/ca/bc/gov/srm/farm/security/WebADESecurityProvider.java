
package ca.bc.gov.srm.farm.security;

import ca.bc.gov.srm.farm.User;
import ca.bc.gov.srm.farm.exception.ProviderException;
import ca.bc.gov.srm.farm.webade.WebADEProviderUtils;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.AppRoles;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Role;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.user.WebADEUserPermissions;


/**
 * WebADESecurityProvider.
 *
 * @author   $Author: awilkinson $
 * @version  $Revision: 2145 $
 */
final class WebADESecurityProvider extends SecurityProvider {

  /** securityRuleProvider. */
  private SecurityRuleProvider securityRuleProvider = null;

  /**
   * Creates a new WebADESecurityProvider object.
   *
   * @param  value  Input parameter to initialize class.
   */
  WebADESecurityProvider(final SecurityRuleProvider value) {
    initialize(value);
  }


  /**
   * canPerformAction.
   *
   * @param   businessAction  The parameter value.
   * @param   user            The parameter value.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public boolean canPerformAction(final BusinessAction businessAction,
    final User user) throws ProviderException {

    checkInitialized();

    boolean result = false;

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();

      // create the webade action based on our business action
      SecurityRule rule = securityRuleProvider.getSecurityRule(businessAction);
      Action action = new Action(rule.getRuleName());

      WebADEUserPermissions perms = WebADEProviderUtils.getPermissions(app,
          user);

      result = perms.canPerformAction(action);

    } catch (WebADEException ex) {
      throw new ProviderException(ex);
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
   * @throws  ProviderException  On exception.
   */
  @Override
  public boolean canPerformAction(final String businessActionName,
    final User user) throws ProviderException {
    BusinessAction businessAction = new BusinessAction(businessActionName);

    return canPerformAction(businessAction, user);
  }


  /**
   * getBusinessRoles.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public BusinessRole[] getBusinessRoles() throws ProviderException {

    checkInitialized();

    BusinessRole[] result = new BusinessRole[0];

    // get the webade application
    Application app = WebADERequest.getInstance().getApplication();
    AppRoles roles = app.getRoles();
    String[] roleNames = roles.getRoleNames();
    result = new BusinessRole[roleNames.length];

    for (int i = 0; i < roleNames.length; i++) {
      BusinessRole item = new BusinessRole(roleNames[i]);
      result[i] = item;
    }

    return result;
  }

  /**
   * getBusinessRolesForUser.
   *
   * @param   user  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public BusinessRole[] getBusinessRolesForUser(final User user)
    throws ProviderException {

    checkInitialized();

    BusinessRole[] result = new BusinessRole[0];

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();
      WebADEUserPermissions perms = WebADEProviderUtils.getPermissions(app,
          user.getGuid());
      Role[] roles = perms.getRoles();
      result = new BusinessRole[roles.length];

      for (int i = 0; i < roles.length; i++) {
        BusinessRole item = new BusinessRole(roles[i].getName());
        result[i] = item;
      }
    } catch (WebADEException ex) {
      throw new ProviderException(ex);
    }

    return result;
  }

  /**
   * getSecurityRule.
   *
   * @param   key  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public SecurityRule getSecurityRule(final String key)
    throws ProviderException {
    checkInitialized();

    return this.securityRuleProvider.getSecurityRule(key);
  }

  /**
   * getSecurityRule.
   *
   * @param   businessAction  Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public SecurityRule getSecurityRule(final BusinessAction businessAction)
    throws ProviderException {
    checkInitialized();

    return this.securityRuleProvider.getSecurityRule(businessAction);
  }

  /**
   * initialize.
   *
   * @param  resource  The parameter value.
   */
  @Override
  public void initialize(final Object resource) {
    setInitialized(false);

    if (resource == null) {
      throw new IllegalArgumentException("resource cannot be null.");
    }

    if (!(resource instanceof SecurityRuleProvider)) {
      throw new IllegalArgumentException(
        "resource is expected to be SecurityRuleProvider, not "
        + resource.getClass().getName());
    }

    this.securityRuleProvider = (SecurityRuleProvider) resource;
    setInitialized(true);
  }

  /**
   * isInRole.
   *
   * @param   businessRole  Input parameter.
   * @param   user          Input parameter.
   *
   * @return  The return value.
   *
   * @throws  ProviderException  On exception.
   */
  @Override
  public boolean isInRole(final BusinessRole businessRole, final User user)
    throws ProviderException {

    checkInitialized();


    boolean result = false;

    try {

      // get the webade application
      Application app = WebADERequest.getInstance().getApplication();
      WebADEUserPermissions perms = WebADEProviderUtils.getPermissions(app,
          user.getGuid());
      Role[] roles = perms.getRoles();

      for (int i = 0; i < roles.length; i++) {

        if (roles[i].getName().equalsIgnoreCase(businessRole.getRoleName())) {
          result = true;

          break;
        }
      }
    } catch (WebADEException ex) {
      throw new ProviderException(ex);
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
   * @throws  ProviderException  On exception.
   */
  @Override
  public boolean isInRole(final String businessRoleName, final User user)
    throws ProviderException {
    BusinessRole businessRole = new BusinessRole(businessRoleName);

    return isInRole(businessRole, user);
  }

}
