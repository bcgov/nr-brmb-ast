/**
 * @(#)Application.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

import java.sql.Connection;
import java.sql.SQLException;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.service.UserInfoService;

/**
 * @author jross
 */
public interface Application extends ApplicationMBean {

    /**
     * Returns the UserInfoService object for this application. This singleton
     * centralizes all user info related API calls. The reason this was done was
     * to separate them from the rest of the WebADE API, in an attempt to reduce
     * the clutter of the monolithic Application interface.
     * @return The UserInfoService singleton for this application.
     */
    public UserInfoService getUserInfoService();

    /**
     * Returns the set of WebADE application preferences.
     * 
     * @return Returns the applicationPreferences.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             WebADE application preferences.
     */
    public WebADEPreferences getWebADEApplicationPreferences() throws WebADEException;

    /**
     * Returns the set of user preferences for this user and this application.
     * 
     * @param targetUserCredentials
     *            The user's identifying credentials.
     * @return Returns the user's preferences.
     * @throws WebADEException
     *             Thrown when a WebADE error occurs.
     */
    public WebADEPreferences getWebADEUserPreferences(UserCredentials targetUserCredentials)
            throws WebADEException;

    /**
     * Saves the given user preferences for this user and this application.
     * 
     * @param targetUserCredentials
     *            The user's identifying credentials.
     * @param preferences The user's User preferences.
     * @return The saved user's preferences object.
     * @throws WebADEException
     *             Thrown when a WebADE error occurs.
     */
    public WebADEPreferences saveWebADEUserPreferences(UserCredentials targetUserCredentials, WebADEPreferences preferences)
            throws WebADEException;

    /**
     * Returns the global preferences for WebADE.
     * 
     * @return Returns the WebADE global preferences.
     * @throws WebADEException
     *             Thrown when a WebADE error occurs.
     */
    public WebADEPreferences getWebADEGlobalPreferences() throws WebADEException;

    /**
     * Get the code (acronym) for the application.
     * 
     * @return the code
     */
    @Override
	public String getApplicationCode();

    /**
     * Get the deployment environment this application is deployed in.
     * 
     * @return The application environment code.
     */
    public String getApplicationEnvironment();

    /**
     * get the roles used by this application.
     * 
     * @return a list of the application roles
     */
    public AppRoles getRoles();

    /**
     * Returns the public unauthenticated permissions from the datastore. If no user can be
     * identified with the request, this method will be userd to return a set of
     * public authorizations.
     * 
     * @return A WebADEUserPermissions object for the public unauthenticated
     *         user.
     * @throws WebADEException
     */
    public WebADEUserPermissions getPublicWebADEPermissions() throws WebADEException;

    /**
     * Loads the target user's permissions from the datastore, or returns null
     * if the user is not found.  Will return the session user's cached permissions
     * object, if the given credentials match the current user's credentials.
     * 
     * @param userCredentials
     *            The user's identifying credentials.
     * 
     * @return A WebADEUserPermissions object for the target user.
     * @throws WebADEException
     */
    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials userCredentials)
            throws WebADEException;

    /**
     * Loads the target user's permissions from the datastore, or returns null
     * if the user is not found.
     * 
     * @param userCredentials
     *            The user's identifying credentials.
     * @param ignoreSessionCache 
     *            A flag indicating whether to ignore the session user permissions 
     *            object, should it match the given user credentials.
     * 
     * @return A WebADEUserPermissions object for the target user.
     * @throws WebADEException
     */
    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials userCredentials, boolean ignoreSessionCache)
            throws WebADEException;

    /**
     * Checks to see if the user is a member of the group identified by the
     * given GUID. This method will also traverse the group's member groups
     * recursively until it has either found the user or exhausted the set of
     * member groups.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @param groupGuid
     *            The unique GUID of the target group.
     * @return True if the user was found as a member, otherwise false.
     * @throws WebADEException
     *             Thrown if the group is not found or an error occurs while
     *             processing the request.
     */
    public boolean isUserInGroup(UserCredentials userCredentials, GUID groupGuid)
            throws WebADEException;

    /**
     * Checks to see if the user is a member of the groups identified by the
     * given array of GUID values. This method will also traverse the groups'
     * member groups recursively until it has either found the user or exhausted
     * the set of member groups.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @param groupGuids
     *            The unique GUID values of the target groups.
     * @return The array of all GUID values for groups in the set that the user
     *         is a member of (Returns a size-0 array if the user is not a
     *         member of any of the given groups).
     * @throws WebADEException
     *             Thrown if ant of the groups are not found or an error occurs
     *             while processing the request.
     */
    public GUID[] isUserInGroups(UserCredentials userCredentials, GUID[] groupGuids)
            throws WebADEException;

    /**
     * Returns a flag indicating whether the user has previously agreed to the
     * given agreement.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @param agreementName
     *            The unique name of the target agreement.
     * @return The set of application role names authorized to that user.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the datastore.
     */
    public boolean hasUserAcceptedAgreement(UserCredentials userCredentials, String agreementName)
            throws WebADEException;

    /**
     * Sets the user's "accepted" flag on the given agreement to the given flag
     * value.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @param agreementName
     *            The unique name of the target agreement.
     * @param agreeFlag
     *            A flag indicating whether the user accepts or refuses the
     *            given agreement.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to save the flag to
     *             the datastore.
     */
    public void setUserAcceptedAgreement(UserCredentials userCredentials, String agreementName,
            boolean agreeFlag) throws WebADEException;

    /**
     * Returns the organization with the given organization id.
     * 
     * @param organizationId
     *            The unique organization id.
     * @return A ministry-specific organization.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the datastore.
     */
    public Organization getOrganizationById(long organizationId) throws WebADEException;

    /**
     * Tests if the application is currently disabled. The disabled status
     * 
     * @return <code>true</code> if the application is currently disabled
     * @throws WebADEException
     *             Thrown if an error occurs while determining if the
     *             application has been disabled.
     */
    public boolean isDisabled() throws WebADEException;

    /**
     * Sets the disabled status of this application in the current execution
     * environment.
     * 
     * @param disable
     *            Flag indicating whether to disable the application.
     * @throws WebADEException
     */
    public void setDisabled(boolean disable) throws WebADEException;

    /**
     * Gets the user's default organization.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @return The selected organization.
     * @throws WebADEException
     *             Thrown if an error occurs while getting the default
     *             organization.
     */
    public Organization getUserDefaultOrganization(UserCredentials userCredentials)
            throws WebADEException;

    /**
     * Sets the user's default organization to the given organization.
     * 
     * @param userCredentials
     *            The target user's credentials.
     * @param organization
     *            The selected organization.
     * @throws WebADEException
     *             Thrown if an error occurs while setting the default
     *             organization.
     */
    public void setUserDefaultOrganization(UserCredentials userCredentials,
            Organization organization) throws WebADEException;

    /**
     * gets a Connection for the given role. Checks to see whether the user is
     * in the specified role.
     * 
     * @param userAuthorizations
     *            The target user's authorizations.
     * @param role
     *            the role to get the Connection for
     * 
     * @return A connection from the pool.
     * @throws WebADEException
     *             Thrown if an error occurs.
     * @throws SQLException
     *             Thrown if an error occurs.
     * @deprecated Deprecated Use a JNDI data source for applications running in a container
     */
    @Deprecated
    public Connection getConnection(WebADEUserPermissions userAuthorizations, Role role)
            throws WebADEException, SQLException;

    /**
     * Gets a Connection for the given action. Checks to see whether has role
     * access to the action.
     * 
     * @param userAuthorizations
     *            The target user's authorizations.
     * @param action
     *            The action to get the Connection for.
     * 
     * @return <code>connection</code> a connection
     * @throws WebADEException
     *             Thrown if an error occurs.
     * @throws SQLException
     *             Thrown if an error occurs.
     * @deprecated Deprecated Use a JNDI data source for applications running in a container
     */
    @Deprecated
    public Connection getConnectionByAction(WebADEUserPermissions userAuthorizations,
            Action action) throws WebADEException, SQLException;

    /**
     * Gets a Connection for the given priviledged action. Checks to see if the
     * action has been defined as priviledged in the WebADE datastore. Only
     * priviledged actions can return connections without requiring a user to
     * authorize against. See the WebADE documentation for more information
     * about priviledged actions.
     * 
     * @param action
     *            The action to get the Connection for.
     * 
     * @return <code>connection</code> a connection
     * @throws WebADEException
     *             Thrown if an error occurs, or if the action is not marked as
     *             priviledged.
     * @throws SQLException
     *             Thrown if an error occurs.
     * @deprecated Deprecated Use a JNDI data source for applications running in a container
     */
    @Deprecated
    public Connection getConnectionByPriviledgedAction(Action action) throws WebADEException,
            SQLException;

    /**
     * Checks to see if a call to getUsersByRole() will throw a
     * IndeterminateAuthorizationsException for the given role.
     * 
     * @param role
     *            The WebADE role in this application.
     * @return True if there are authorizations that cannot be fully evaluated,
     *         when looking for users in this role.
     * @throws WebADEException
     *             Thrown if an error occurs while performing this query.
     */
    public boolean hasIndeterminateAuthorizations(Role role) throws WebADEException;

    /**
     * Checks to see if a call to getUsersByRole() will throw a
     * IndeterminateAuthorizationsException for the given organization.
     * 
     * @param organization
     *            The organization to load users of this application for.
     * @return True if there are authorizations that cannot be fully evaluated,
     *         when looking for users in this role.
     * @throws WebADEException
     *             Thrown if an error occurs while performing this query.
     */
    public boolean hasIndeterminateAuthorizations(Organization organization) throws WebADEException;

    /**
     * Checks to see if a call to getUsersByRole() will throw a
     * IndeterminateAuthorizationsException for the given role and organization.
     * 
     * @param role
     *            The WebADE role in this application.
     * @param organization
     *            The organization to load users of this application for.
     * @return True if there are authorizations that cannot be fully evaluated,
     *         when looking for users in this role.
     * @throws WebADEException
     *             Thrown if an error occurs while performing this query.
     */
    public boolean hasIndeterminateAuthorizations(Role role, Organization organization)
            throws WebADEException;

    /**
     * Loads all users from the datastore that are members of the given role.
     * 
     * @param role
     *            The role to load users for.
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Role role)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Loads all users from the datastore that are members of the given role.
     * 
     * @param role
     *            The role to load users for.
     * @param ignoreIndeterminateAuthorizationsErrors
     *            If set to true, prevents the
     *            IndeterminateAuthorizationsException from being thrown, if it
     *            occurs.
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Role role,
            boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Loads all users from the datastore that are members of roles via an
     * authorization associated with the given organization.
     * 
     * @param organization
     *            The organization to load users for.
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Organization organization)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Loads all users from the datastore that are members of roles via an
     * authorization associated with the given organization.
     * 
     * @param organization
     *            The organization to load users for.
     * @param ignoreIndeterminateAuthorizationsErrors
     *            If set to true, prevents the
     *            IndeterminateAuthorizationsException from being thrown, if it
     *            occurs.
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Organization organization,
            boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Loads all users from the datastore that are members the given roles via
     * an authorization associated with the given organization.
     * 
     * @param role
     *            The role to load users for (optional).
     * @param organization
     *            The organization to load users for (optional).
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Role role, Organization organization)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Loads all users from the datastore that are members the given roles via
     * an authorization associated with the given organization.
     * 
     * @param role
     *            The role to load users for (optional).
     * @param organization
     *            The organization to load users for (optional).
     * @param ignoreIndeterminateAuthorizationsErrors
     *            If set to true, prevents the
     *            IndeterminateAuthorizationsException from being thrown, if it
     *            occurs.
     * 
     * @return An array of member UserCredentials objects.
     * @throws IndeterminateAuthorizationsException
     *             Throws IndeterminateAuthorizationsException if the list of
     *             users cannot be completely loaded.
     * @throws WebADEException
     *             Throws a base WebADEException if an error occurs while
     *             performing this query.
     */
    public UserCredentials[] getAuthorizedUsers(Role role, Organization organization,
            boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException;

    /**
     * Notifies the application singleton to shutdown, stopping monitoring
     * threads and closing dependant resources.
     * 
     * @throws SecurityException
     *             If the caller is not an instance of an authorized class, a
     *             SecurityException is thrown.
     */
    public void shutdown() throws SecurityException;

}