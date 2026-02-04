package ca.bc.gov.webade.user;

import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.Role;

/**
 * @author jross
 */
public interface WebADEUserPermissions {
    /**
     * @return Returns the associated user's credentials.
     */
    public UserCredentials getUserCredentials();

	/**
	 * @return Returns the orgs.
	 */
	public Organization[] getOrganizations();

	/**
	 * @return Returns the roles.
	 */
	public Role[] getRoles();

    /**
     * Returns the set of roles assigned to the user that are not secured by organization.
     * 
     * @return Returns the nonsecured by organization roles assigned to the user.
     */
    public Role[] getRolesNotSecuredByOrganization();

	/**
	 * Returns the set of roles assigned to the user by the given organization.
	 * 
	 * @param targetOrg
	 *            The target organization.
	 * @return Returns the roles assigned to the user for that orgzniation.
	 */
	public Role[] getRolesByOrganization(Organization targetOrg);

	/**
	 * Returns a boolean indicating whether a user is authorized to act in the
	 * given role.
	 * 
	 * @param role
	 *            the name of a role
	 * @return <code>true</code> if the user is authorized to act in the role
	 */
	public boolean isUserInRole(Role role);

	/**
	 * Returns a boolean indicating whether a user is authorized to perform
	 * action.
	 * 
	 * @param action
	 *            the target action
	 * @return <code>true</code> if the user is authorized to perform action
	 */
	public boolean canPerformAction(Action action);

	/**
	 * @return Returns the selected organization.
	 */
	public Organization getSelectedOrganization();

	/**
	 * Sets the selected organization.
	 * 
	 * @param selectedOrg
	 *            The selected organization.
	 */
	public void setSelectedOrganization(Organization selectedOrg);

	/**
	 * Returns the Government Organizations a user has access to.
	 * 
	 * @return The set of organizations this user is assigned to
	 */
	public Organization[] getGovernmentOrganizations();

	/**
	 * Returns the Non-Government Organizations a user has access to.
	 * 
	 * @return The set of organizations this user is assigned to
	 */
	public Organization[] getNonGovernmentOrganizations();

	/**
	 * Returns a boolean indicating whether a user is authorized to act on
	 * behalf of an Organization.
	 * 
	 * @param org
	 *            the name of an organization
	 * @return <code>true</code> if the user is authorized access Org Unit
	 */
	public boolean isUserInOrganization(Organization org);

}
