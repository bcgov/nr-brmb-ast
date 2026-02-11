/**
 * @(#)UserProvider.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.provider;

import java.util.List;
import java.util.Properties;

import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.UserSearchQuery;

/**
 * Interface for remote systems that provide the WebADE with user details for
 * users in the WebADE system.
 * 
 * @author jross
 */
public interface WebADEUserProvider {
    /** The WebADE user provider preference sub-type. */
    public static final String WEBADE_USER_PROVIDER_SUBTYPE = "webade-user-provider";
    /** The user-provider class name preference name. */
    public static final String PROVIDER_CLASS_NAME = "class-name";
    /** The user-provider enabled preference name. */
    public static final String PROVIDER_ENABLED = "enabled";
    
    /**
     * Initializes the provider using the given preference set.
     * 
     * @param properties
     *            Configuration settings for the user info provider hosting user
     *            information for this WebADE application.
     * @throws WebADEUserProviderException
     *             Thrown if the configuration settings are not properly
     *             configured (Missing parameters, etc).
     */
    public void init(Properties properties) throws WebADEUserProviderException;

    /**
     * Returns true if the given sourceDirectory is one of the ones supported by this
     * provider.
     * 
     * @param sourceDirectory
     *            The target sourceDirectory name.
     * @return True if this provider provides support for the given domain.
     * @throws WebADEUserProviderException
     */
    public boolean handlesSourceDirectory(String sourceDirectory) throws WebADEUserProviderException;

    /**
     * Returns true if the given user type is one of the ones supported by this
     * provider.
     * 
     * @param userType
     *            The target user type.
     * @return True if this provider provides support for the given user type.
     * @throws WebADEUserProviderException
     */
    public boolean handlesUserType(UserTypeCode userType) throws WebADEUserProviderException;

    /**
     * Returns the source directory for the given user type if it is supported
     * by this provider.
     * @param userType
     *            The target user type.
     * @return The source directory name.
     * @throws WebADEUserProviderException
     */
    public String getSourceDirectoryForUserType(UserTypeCode userType) throws WebADEUserProviderException;

    /**
     * Returns the user type for the given source directory if it is supported
     * by this provider.
     * @param sourceDirectory
     *            The target sourceDirectory name.
     * @return The user type code.
     * @throws WebADEUserProviderException
     */
    public UserTypeCode getUserTypeForSourceDirectory(String sourceDirectory) throws WebADEUserProviderException;

    /**
     * @return The array of source directory names this provider handles.
     * @throws WebADEUserProviderException
     */
    public String[] getSupportedSourceDirectories() throws WebADEUserProviderException;

    /**
     * @return The array of user types this provider handles.
     * @throws WebADEUserProviderException
     */
    public UserTypeCode[] getSupportedUserTypes() throws WebADEUserProviderException;

    /**
     * @return The array of user types this provider handles.
     * @throws WebADEUserProviderException
     */
    public UserTypeCode[] getSearchableUserTypes() throws WebADEUserProviderException;

    /**
     * Returns the user information from the LDAP directory hosting the user
     * identified by the given credentials.
     * 
     * @param requestingUserCredentials
     *            The requesting user's credentials.
     * @param targetUserCredentials
     *            The user's credentials to look up.
     * @return The loaded user object, or null if the user is not found.
     * @throws WebADEUserProviderException
     *             Thrown if a connection cannot be made to the application's
     *             LDAP directories or an error occurs.
     */
    public WebADEUserInfo getUser(UserCredentials requestingUserCredentials,
            UserCredentials targetUserCredentials) throws WebADEUserProviderException;

    /**
     * Checks to see if the user is a member of the groups identified by the
     * given array of GUID values. This method will also traverse the groups'
     * member groups recursively until it has either found the user or exhausted
     * the set of member groups.
     * 
     * @param requestingUserCredentials
     *            The requesting user's credentials.
     * @param targetUserCredentials
     *            The user's credentials to look up.
     * @param groupGuids
     *            The unique GUID values of the target groups.
     * @return The array of all GUID values for groups in the set that the user
     *         is a member of (Returns a size-0 array if the user is not a
     *         member of any of the given groups).
     * @throws WebADEUserProviderException
     *             Thrown if ant of the groups are not found or an error occurs
     *             while processing the request.
     */
    public GUID[] isUserInGroups(UserCredentials requestingUserCredentials,
            UserCredentials targetUserCredentials, GUID[] groupGuids)
            throws WebADEUserProviderException;

    /**
     * Returns a UserSearchQuery metadata object for the given user type code.
     * @param userType
     *            The user type to create a query object for.
     * @return A UserSearchQuery object for searching for users of the given
     *         user type.
     * @throws WebADEUserProviderException
     *             Thrown if searching for the given user type is not supported.
     */
    public UserSearchQuery createUserSearchQuery(UserTypeCode userType)
            throws WebADEUserProviderException;

    /**
     * Returns the user information of the users from the LDAP directory
     * matching the given search criteria.
     * 
     * @param requestingUserCredentials
     *            The requesting user's credentials.
     * @param query
     *            The user search query containing the search parameters.
     * @return The List of loaded user objects.
     * @throws WebADEUserProviderException
     *             Thrown if a connection cannot be made to the application's
     *             LDAP directories or an error occurs.
     */
    public List<WebADEUserInfo> findUsers(UserCredentials requestingUserCredentials,
            UserSearchQuery query) throws WebADEUserProviderException;
}
