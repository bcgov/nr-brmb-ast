/**
 * @(#)AppRoles.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.io.Serializable;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>ApplicationRoles</code> contains the {@link Role}objects for the roles in an
 * {@link Application}. The roles are loaded from the WebADE Directory.
 * Currently the WebADE Directory is implemented in the NT Registry.
 */
public final class AppRoles implements Serializable {
    private static final long serialVersionUID = -7635150849028514052L;
    /** Array of all application roles */
    private Role[] roles;
	
	private static final Logger log = LoggerFactory.getLogger(AppRoles.class);

    /**
     * Create a new <code>AppRoles</code> container for a given WebADE
     * {@link Application}.
     * @param roles The roles defined for this application.
     */
    AppRoles(Role[] roles) {
        this.roles = roles;
        if (this.roles == null) {
            roles = new Role[0];
        }
        if (this.roles.length == 0) {
            log.warn("AppRoles created with no defined roles.");
        }
    }

    /**
     * get the list of valid role names for the parent {@link Application}.
     * @return A set of the role names.
     */
    public String[] getRoleNames() {
        String[] roleNames;
        roleNames = new String[this.roles.length];
        for (int x = 0; x < this.roles.length; x++) {
            roleNames[x] = this.roles[x].getName();
        }
        return roleNames;
    }

    /**
     * Test if the name is a valid role name in the parent {@link Application}.
     * @param roleName The role name to locate in the application's set of roles.
     * @return True it the role name was found, ignoring case.
     */
    public boolean isRoleName(String roleName) {
        return getRole(roleName) != null;
    }

    /**
     * test if the name is a valid role name in the parent {@link Application}.
     * @param roleName The name of the target role to check.
     * @param actionName The action name to locate in that role.
     * @return True if the action name is found.
     */
    public boolean roleHasAction(String roleName, String actionName) {
        boolean hasAction = false;
        Role role = getRole(roleName);
        if (role != null) {
            hasAction = role.hasAction(new Action(actionName));
        }
        return hasAction;
    }

    /**
     * get the {@link Role}object for the given role name.
     * 
     * @param roleName
     *            the name of an role for this application
     * @return the role, or <code>null</code> if the name is not a valid
     *         rolename.
     */
    public Role getRole(String roleName) {
        Role targetRole = null;
        for (int x = 0; x < this.roles.length; x++) {
            if (this.roles[x].getName().equalsIgnoreCase(roleName)) {
                targetRole = this.roles[x];
                break;
            }
        }
        return targetRole;
    }

    /**
     * Get the Roles associated with an action, if any. This association is
     * maintained in the WebADE Directory.
     * 
     * @param action
     *            The target action
     * @return The Role array of roles with the given action.
     */
    public Role[] getRolesByAction(Action action) {
        ArrayList<Role> actionRoles = new ArrayList<Role>();
        for (int x = 0; x < this.roles.length; x++) {
            if (this.roles[x].hasAction(action)) {
                actionRoles.add(this.roles[x]);
            }
        }
        return actionRoles.toArray(new Role[actionRoles.size()]);
    }

}