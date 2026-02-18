/**
 * @(#)WebADEUserUtils.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

import java.util.ArrayList;
import java.util.HashMap;

import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.Role;
import ca.bc.gov.webade.WebADEException;

/**
 * @author jross
 */
public class WebADEUserUtils {

    /**
     * Merges the array of given authorizations into a single instance.
     * 
     * @param auths
     *            The set of authorization objects to merge.
     * @return The merged authorizations object.
     * @throws WebADEException
     *             Thrown if the given permissions belong to multiple users.
     */
    public static final WebADEUserPermissions mergeWebADEUserPermissions(
            WebADEUserPermissions[] auths) throws WebADEException {
        if (auths == null) {
            return null;
        }
        UserCredentials credentials = UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS;
        ArrayList<Role> nonSecuredRoles = new ArrayList<Role>();
        HashMap<Organization,ArrayList<Role>> orgRoles = new HashMap<Organization,ArrayList<Role>>();
        for (int i = 0; i < auths.length; i++) {
            WebADEUserPermissions currentPerms = auths[i];
            if (currentPerms == null) {
                continue;
            }
            UserCredentials authCreds = currentPerms.getUserCredentials();
            if (!UserCredentials.areUnauthenticated(authCreds)
                    && !UserCredentials.areUnauthenticated(credentials)
                    && !credentials.equals(authCreds)) {
                // Cannot merge permissions from multiple users.
                throw new WebADEException("Cannot merge permissions from multiple users.");
            } else if (authCreds != null && !UserCredentials.areUnauthenticated(authCreds)) {
                credentials = authCreds;
            }
            Role[] userroles = currentPerms.getRolesNotSecuredByOrganization();
            for (int j = 0; j < userroles.length; j++) {
                if (!nonSecuredRoles.contains(userroles[j])) {
                    nonSecuredRoles.add(userroles[j]);
                }
            }
            
            Organization[] orgs = currentPerms.getOrganizations();
            for (int j = 0; j < orgs.length; j++) {
            	ArrayList<Role> rolesList = orgRoles.get(orgs[j]);
                if (rolesList == null) {
                    rolesList = new ArrayList<Role>();
                    orgRoles.put(orgs[j], rolesList);
                }

                Role[] roles = currentPerms.getRolesByOrganization(orgs[j]);
                for (int k = 0; k < roles.length; k++) {
                    if (!rolesList.contains(roles[k])) {
                        rolesList.add(roles[k]);
                    }
                }
            }
        }
        return new DefaultWebADEUserPermissions(credentials, nonSecuredRoles, orgRoles);
    }

}
