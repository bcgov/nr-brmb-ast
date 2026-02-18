/**
 * @(#)UserInfoService.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.service;

import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.UserSearchQuery;

/**
 * API that contains all functionality related to user information, including
 * the retrieval and searching for user information, along with various
 * supporting API calls.
 * @author jross
 */
public interface UserInfoService {
    /**
     * Returns true if the given sourceDirectory is one of the ones supported by
     * this provider.
     * @param sourceDirectory
     *            The target sourceDirectory name.
     * @return True if this provider provides support for the given domain.
     * @throws UserInfoServiceException
     */
    public boolean handlesSourceDirectory(String sourceDirectory)
            throws UserInfoServiceException;

    /**
     * Returns true if the given user type is one of the ones supported by this
     * provider.
     * @param userType
     *            The target user type.
     * @return True if this provider provides support for the given user type.
     * @throws UserInfoServiceException
     */
    public boolean handlesUserType(UserTypeCode userType)
            throws UserInfoServiceException;

    /**
     * Returns the source directory for the given user type if it is supported
     * by this provider.
     * @param userType
     *            The target user type.
     * @return The source directory name.
     * @throws UserInfoServiceException
     */
    public String getSourceDirectoryForUserType(UserTypeCode userType)
            throws UserInfoServiceException;

    /**
     * Returns the user type for the given source directory if it is supported
     * by this provider.
     * @param sourceDirectory
     *            The target sourceDirectory name.
     * @return The user type code.
     * @throws UserInfoServiceException
     */
    public UserTypeCode getUserTypeForSourceDirectory(String sourceDirectory)
            throws UserInfoServiceException;

    /**
     * Returns the set of source directory names supported by this instance of
     * the UserInfoService.
     * @return The array of source directory names this provider handles.
     * @throws UserInfoServiceException
     */
    public String[] getSupportedSourceDirectories()
            throws UserInfoServiceException;

    /**
     * Returns the set of user type codes supported by this instance of the
     * UserInfoService.
     * @return The array of user types this provider handles.
     * @throws UserInfoServiceException
     */
    public UserTypeCode[] getSupportedUserTypes()
            throws UserInfoServiceException;

    /**
     * Returns the set of user type codes that can be queried by the instance of the
     * UserInfoService.
     * @return The array of user types this provider handles.
     * @throws UserInfoServiceException
     */
    public UserTypeCode[] getSearchableUserTypes()
            throws UserInfoServiceException;

    /**
     * Loads the target user's personal information from the user provider, or
     * returns null if the user is not found. Will return the internally cached
     * info object, if the given credentials match the current user's
     * credentials.
     * @param userCredentials
     *            The user's identifying credentials.
     * @return A WebADEUserInfo object for the target user.
     * @throws UserInfoServiceException
     */
    public WebADEUserInfo getWebADEUserInfo(UserCredentials userCredentials)
            throws UserInfoServiceException;

    /**
     * Loads the target user's personal information from the user provider, or
     * returns null if the user is not found.
     * @param userCredentials
     *            The user's identifying credentials.
     * @param ignoreSessionCache
     *            A flag indicating whether to ignore the internally cached user
     *            info object, should it match the given user credentials.
     * @return A WebADEUserInfo object for the target user.
     * @throws UserInfoServiceException
     */
    public WebADEUserInfo getWebADEUserInfo(UserCredentials userCredentials,
            boolean ignoreSessionCache) throws UserInfoServiceException;

    /**
     * Checks to see if the user is a member of the group identified by the
     * given GUID. This method will also traverse the group's member groups
     * recursively until it has either found the user or exhausted the set of
     * member groups.
     * @param userCredentials
     *            The target user's credentials.
     * @param groupGuid
     *            The unique GUID of the target group.
     * @return True if the user was found as a member, otherwise false.
     * @throws UserInfoServiceException
     *             Thrown if the group is not found or an error occurs while
     *             processing the request.
     */
    public boolean isUserInGroup(UserCredentials userCredentials, GUID groupGuid)
            throws UserInfoServiceException;

    /**
     * Checks to see if the user is a member of the groups identified by the
     * given array of GUID values. This method will also traverse the groups'
     * member groups recursively until it has either found the user or exhausted
     * the set of member groups.
     * @param userCredentials
     *            The target user's credentials.
     * @param groupGuids
     *            The unique GUID values of the target groups.
     * @return The array of all GUID values for groups in the set that the user
     *         is a member of (Returns a size-0 array if the user is not a
     *         member of any of the given groups).
     * @throws UserInfoServiceException
     *             Thrown if ant of the groups are not found or an error occurs
     *             while processing the request.
     */
    public GUID[] isUserInGroups(UserCredentials userCredentials,
            GUID[] groupGuids) throws UserInfoServiceException;

    /**
     * Returns the UserSearchQuery meta-data object to perform user searching
     * for this application on the provided user type.
     * @param userType
     *            The user type to perform searching for.
     * @return A UserSearchQuery object.
     * @throws UserInfoServiceException
     *             Thrown when an error occurs while trying to create the search
     *             query from the user provider for the given user type.
     */
    public UserSearchQuery createUserSearchQuery(UserTypeCode userType)
            throws UserInfoServiceException;

    /**
     * Finds the set of WebADEUserInfo objects that match the provided search
     * criteria.
     * @param searchQuery
     *            The search query.
     * @return A List of WebADEUserInfo objects.
     * @throws UserInfoServiceException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the user provider.
     */
    public WebADEUserInfo[] findUsers(UserSearchQuery searchQuery)
            throws UserInfoServiceException;

    /**
     * Set the credentials used by the user provider as the requestor.
     * This value is cleared by the WebADEFilter at the end of each HTTP Request.
     * If not using the WebADEFilter, it should be cleared manually.
     * 
     * @param userCredentials The credentials used by the user provider as the requestor.
     */
    public void setProviderRequestorUserCredentials(UserCredentials userCredentials);
}
