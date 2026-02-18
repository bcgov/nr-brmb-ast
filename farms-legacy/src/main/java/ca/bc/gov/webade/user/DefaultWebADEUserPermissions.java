package ca.bc.gov.webade.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.Role;

/**
 * @author jross
 */
public class DefaultWebADEUserPermissions implements WebADEUserPermissions, Serializable {
    private static final long serialVersionUID = -7913068773501753042L;
    private UserCredentials userCredentials;
    /** The authorized roles for this user that are not secured by organization. */
    private ArrayList<Role> nonSecuredByOrgRoles = new ArrayList<Role>();
    /** The authorized roles for this user, ordered by organization. */
    private HashMap<Organization,ArrayList<Role>> rolesByOrganization = new HashMap<Organization,ArrayList<Role>>();
    /** The organization the user will user for operations in this session. */
    private Organization selectedOrg = null;

    /**
     * @param userCredentials
     *            The associated user credentials.
     * @param rolesByOrg
     *            The authorized roles, organized by organization.
     */
    public DefaultWebADEUserPermissions(UserCredentials userCredentials,
            HashMap<Organization,ArrayList<Role>> rolesByOrg) {
        this(userCredentials, null, rolesByOrg);
    }

    /**
     * @param userCredentials
     *            The associated user credentials.
     * @param nonSecuredByOrgRoles
     *            The authorized roles for this user that are not secured by
     *            organization.
     * @param rolesByOrg
     *            The authorized roles, organized by organization.
     */
    public DefaultWebADEUserPermissions(UserCredentials userCredentials,
            ArrayList<Role> nonSecuredByOrgRoles, HashMap<Organization,ArrayList<Role>> rolesByOrg) {
        this.userCredentials = userCredentials;
        if (nonSecuredByOrgRoles != null) {
            for (Iterator<Role> iter = nonSecuredByOrgRoles.iterator(); iter.hasNext();) {
                Role current = iter.next();
                if (!this.nonSecuredByOrgRoles.contains(current)) {
                    this.nonSecuredByOrgRoles.add(current);
                }
            }
        }
        for (Iterator<Organization> i = rolesByOrg.keySet().iterator(); i.hasNext();) {
            Organization currentOrg = i.next();
            ArrayList<Role> currentRoles = rolesByOrg.get(currentOrg);
            ArrayList<Role> roles = new ArrayList<Role>();
            for (Iterator<Role> j = currentRoles.iterator(); j.hasNext();) {
                roles.add(j.next());
            }
            this.rolesByOrganization.put(currentOrg, roles);
        }
    }

    /**
     * Copy Constructor.
     * @param permissions
     *            The permissions instance to copy.
     */
    public DefaultWebADEUserPermissions(WebADEUserPermissions permissions) {
        if (permissions instanceof DefaultWebADEUserPermissions) {
            DefaultWebADEUserPermissions perms = (DefaultWebADEUserPermissions) permissions;
            this.nonSecuredByOrgRoles = perms.nonSecuredByOrgRoles;
            this.rolesByOrganization = perms.rolesByOrganization;
            this.selectedOrg = perms.selectedOrg;
            this.userCredentials = perms.userCredentials;
        }
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getUserCredentials()
     */
    @Override
	public final UserCredentials getUserCredentials() {
        return userCredentials;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getOrganizations()
     */
    @Override
	public final Organization[] getOrganizations() {
        Set<Organization> orgKeys = this.rolesByOrganization.keySet();
        return orgKeys
                .toArray(new Organization[orgKeys.size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getRoles()
     */
    @Override
	public final Role[] getRoles() {
        ArrayList<Role> roleList = new ArrayList<Role>();
        for (Iterator<Role> iter = this.nonSecuredByOrgRoles.iterator(); iter.hasNext();) {
            Role current = iter.next();
            if (!roleList.contains(current)) {
                roleList.add(current);
            }
        }
        if (getSelectedOrganization() == null) {
            for (Iterator<Organization> iter = rolesByOrganization.keySet().iterator(); iter
                    .hasNext();) {
                Organization org = iter.next();
                ArrayList<Role> orgRoles = rolesByOrganization.get(org);
                for (Iterator<Role> iterator = orgRoles.iterator(); iterator
                        .hasNext();) {
                    Role current = iterator.next();
                    if (!roleList.contains(current)) {
                        roleList.add(current);
                    }
                }
            }
        } else {
            ArrayList<Role> orgRoles = rolesByOrganization
                    .get(getSelectedOrganization());
            for (Iterator<Role> iterator = orgRoles.iterator(); iterator.hasNext();) {
                Role current = iterator.next();
                if (!roleList.contains(current)) {
                    roleList.add(current);
                }
            }
        }
        return roleList.toArray(new Role[roleList.size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getRolesNotSecuredByOrganization()
     */
    @Override
	public Role[] getRolesNotSecuredByOrganization() {
        return this.nonSecuredByOrgRoles.toArray(new Role[this.nonSecuredByOrgRoles.size()]);
    }
    
    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getRolesByOrganization(ca.bc.gov.webade.Organization)
     */
    @Override
	public final Role[] getRolesByOrganization(Organization targetOrg) {
        if (targetOrg == null) {
            return getRolesNotSecuredByOrganization();
        }
        ArrayList<Role> roleList = new ArrayList<Role>();
        for (Iterator<Organization> iter = rolesByOrganization.keySet().iterator(); iter
                .hasNext();) {
            Organization org = iter.next();
            if (org == null || org.equals(targetOrg)) {
                roleList.addAll(rolesByOrganization.get(org));
            }
        }
        return roleList.toArray(new Role[roleList.size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#isUserInRole(ca.bc.gov.webade.Role)
     */
    @Override
	public final boolean isUserInRole(Role role) {
        boolean inRole = false;
        Role[] userRoles = getRoles();
        for (int i = 0; i < userRoles.length; i++) {
            if (userRoles[i].equals(role)) {
                inRole = true;
                break;
            }
        }
        return inRole;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#canPerformAction(ca.bc.gov.webade.Action)
     */
    @Override
	public final boolean canPerformAction(Action action) {
        boolean canPerform = false;
        Role[] userRoles = getRoles();
        for (int i = 0; i < userRoles.length; i++) {
            for (int j = 0; j < userRoles[i].getActions().length; j++) {
                if (userRoles[i].getActions()[j].equals(action)) {
                    canPerform = true;
                    break;
                }
            }
        }
        return canPerform;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getSelectedOrganization()
     */
    @Override
	public final Organization getSelectedOrganization() {
        return selectedOrg;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#setSelectedOrganization(ca.bc.gov.webade.Organization)
     */
    @Override
	public final void setSelectedOrganization(Organization selectedOrg) {
        if (selectedOrg == null) {
            this.selectedOrg = null;
        } else {
            Organization[] orgs = getOrganizations();
            for (int x = 0; x < orgs.length; x++) {
                if (orgs[x] != null && orgs[x].equals(selectedOrg)) {
                    this.selectedOrg = orgs[x];
                }
            }
        }
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getGovernmentOrganizations()
     */
    @Override
	public final Organization[] getGovernmentOrganizations() {
        ArrayList<Organization> orgUnits = new ArrayList<Organization>();
        for (Iterator<Organization> iter = this.rolesByOrganization.keySet().iterator(); iter
                .hasNext();) {
            Organization current = iter.next();
            if (current != null
                    && current.getOrganizationType().equals(
                            Organization.BC_GOVERNMENT_ORG)) {
                orgUnits.add(current);
            }
        }
        return orgUnits.toArray(new Organization[orgUnits
                .size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#getNonGovernmentOrganizations()
     */
    @Override
	public final Organization[] getNonGovernmentOrganizations() {
        ArrayList<Organization> orgUnits = new ArrayList<Organization>();
        for (Iterator<Organization> iter = this.rolesByOrganization.keySet().iterator(); iter
                .hasNext();) {
            Organization current = iter.next();
            if (current != null
                    && current.getOrganizationType().equals(
                            Organization.NON_GOVERNMENT_ORG)) {
                orgUnits.add(current);
            }
        }
        return orgUnits.toArray(new Organization[orgUnits
                .size()]);
    }

    /**
     * @see ca.bc.gov.webade.user.WebADEUserPermissions#isUserInOrganization(ca.bc.gov.webade.Organization)
     */
    @Override
	public final boolean isUserInOrganization(Organization org) {
        return (org == null) || (this.rolesByOrganization.keySet().contains(org));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getUserCredentials() + "[ NON-ORG-ROLES={");
        int x = 0;
        for (Iterator<Role> i = this.nonSecuredByOrgRoles.iterator(); i.hasNext(); x++) {
            if (x > 0) {
                buffer.append(", ");
            }
            buffer.append(i.next());
        }
        buffer.append("}, ORG-ROLES={");
        x = 0;
        for (Iterator<Organization> i = this.rolesByOrganization.keySet().iterator(); i.hasNext(); x++) {
        	Organization org = i.next();
            if (x > 0) {
                buffer.append(", ");
            }
            buffer.append(org + "(");
            ArrayList<Role> roles = this.rolesByOrganization.get(org);
            int y = 0;
            for (Iterator<Role> j = roles.iterator(); j.hasNext(); y++) {
                if (y > 0) {
                    buffer.append(", ");
                }
                buffer.append(j.next());
            }
            buffer.append(")");
        }
        buffer.append("} ]");
        
        return buffer.toString();
    }
}
