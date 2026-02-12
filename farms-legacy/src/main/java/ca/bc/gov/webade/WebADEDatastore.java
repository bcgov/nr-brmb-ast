package ca.bc.gov.webade;

import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.security.enterprise.SecurityConfiguration;

public interface WebADEDatastore {

    /**
     * Returns the application preferences defined for the current WebADE
     * application. Each time this method is called, it will reload the
     * application preferences. Developers should cache this information, if
     * they don't want this information to change during deployment of the
     * application.
     * @return A loaded WebADEPreferences object with the set of preference values.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the datastore.
     */
    public WebADEPreferences getWebADEApplicationPreferences() throws WebADEException;

    /**
     * Returns the set of user preferences for this user and this application.
     * @param targetUserCredentials
     *            The user's identifying credentials.
     * @return Returns the user's preferences.
     * @throws WebADEException
     *             Thrown when a WebADE error occurs.
     */
    public WebADEPreferences getWebADEUserPreferences(UserCredentials targetUserCredentials)
            throws WebADEException;

    /**
     * Returns the global preferences defined for all of WebADE.
     * @return A loaded WebADEPreferences object with the set of preference values.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the datastore.
     */
    public WebADEPreferences getWebADEGlobalPreferences() throws WebADEException;

    /**
     * Returns the current application code set for the WebADE datastore.
     * @return The application code value.
     */
    public String getApplicationCode();

    /**
     * Returns the set of roles for the given application code.
     * @return The array or Role objects defined for this application.
     * @throws WebADEException
     *             Thrown when an error occurs while trying to retrieve the
     *             information from the datastore.
     */
    public Role[] getApplicationRoles() throws WebADEException;

    /**
     * Returns the public permissions from the datastore. If no user can be
     * identified with the request, this method will be userd to return a set of
     * public authorizations.
     * @return A WebADEUserPermissions object for the public unauthenticated
     *         user.
     * @throws WebADEException
     */
    public WebADEUserPermissions getPublicWebADEPermissions()
            throws WebADEException;

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
     * Get the SecurityConfiguration.  This method is used by the WebAppRequestPrecessingUtils.
     * 
     * @return SecurityConfiguration as configured by webade preferences.
     * @throws WebADEException
     */
    public SecurityConfiguration getSecurityConfiguration() throws WebADEException;

}
