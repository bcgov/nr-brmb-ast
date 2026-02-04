package ca.bc.gov.webade;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import ca.bc.gov.webade.j2ee.WebADEFilter;
import ca.bc.gov.webade.j2ee.WebAppRequestProcessingUtils;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.security.SecurityUtils;
import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.service.UserInfoService;

public final class WebADEDatabaseApplication implements Application, Serializable {

    private WebADEDatastore datastore;
    private UserInfoService infoService;
    private AppRoles roles;

    protected WebADEDatabaseApplication(WebADEDatastore ds) throws WebADEException {
        this.datastore = ds;

        this.roles = new AppRoles(ds.getApplicationRoles());
    }

    @Override
    public UserInfoService getUserInfoService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserInfoService'");
    }

    @Override
    public WebADEPreferences getWebADEApplicationPreferences() throws WebADEException {
        return this.datastore.getWebADEApplicationPreferences();
    }

    @Override
    public WebADEPreferences getWebADEUserPreferences(UserCredentials targetUserCredentials) throws WebADEException {
        return this.datastore.getWebADEUserPreferences(targetUserCredentials);
    }

    @Override
    public WebADEPreferences saveWebADEUserPreferences(UserCredentials targetUserCredentials,
            WebADEPreferences preferences) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveWebADEUserPreferences'");
    }

    @Override
    public WebADEPreferences getWebADEGlobalPreferences() throws WebADEException {
        return this.datastore.getWebADEGlobalPreferences();
    }

    @Override
    public String getApplicationCode() {
        return this.datastore.getApplicationCode();
    }

    @Override
    public String getApplicationEnvironment() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getApplicationEnvironment'");
    }

    @Override
    public AppRoles getRoles() {
        return this.roles;
    }

    @Override
    public WebADEUserPermissions getPublicWebADEPermissions() throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPublicWebADEPermissions'");
    }

    @Override
    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials userCredentials) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWebADEUserPermissions'");
    }

    @Override
    public WebADEUserPermissions getWebADEUserPermissions(UserCredentials userCredentials, boolean ignoreSessionCache)
            throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWebADEUserPermissions'");
    }

    @Override
    public boolean isUserInGroup(UserCredentials userCredentials, GUID groupGuid) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserInGroup'");
    }

    @Override
    public GUID[] isUserInGroups(UserCredentials userCredentials, GUID[] groupGuids) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserInGroups'");
    }

    @Override
    public boolean hasUserAcceptedAgreement(UserCredentials userCredentials, String agreementName)
            throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasUserAcceptedAgreement'");
    }

    @Override
    public void setUserAcceptedAgreement(UserCredentials userCredentials, String agreementName, boolean agreeFlag)
            throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUserAcceptedAgreement'");
    }

    @Override
    public Organization getOrganizationById(long organizationId) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOrganizationById'");
    }

    @Override
    public boolean isDisabled() throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isDisabled'");
    }

    @Override
    public void setDisabled(boolean disable) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setDisabled'");
    }

    @Override
    public Organization getUserDefaultOrganization(UserCredentials userCredentials) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserDefaultOrganization'");
    }

    @Override
    public void setUserDefaultOrganization(UserCredentials userCredentials, Organization organization)
            throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUserDefaultOrganization'");
    }

    @Override
    public Connection getConnection(WebADEUserPermissions userAuthorizations, Role role)
            throws WebADEException, SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }

    @Override
    public Connection getConnectionByAction(WebADEUserPermissions userAuthorizations, Action action)
            throws WebADEException, SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnectionByAction'");
    }

    @Override
    public Connection getConnectionByPriviledgedAction(Action action) throws WebADEException, SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnectionByPriviledgedAction'");
    }

    @Override
    public boolean hasIndeterminateAuthorizations(Role role) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasIndeterminateAuthorizations'");
    }

    @Override
    public boolean hasIndeterminateAuthorizations(Organization organization) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasIndeterminateAuthorizations'");
    }

    @Override
    public boolean hasIndeterminateAuthorizations(Role role, Organization organization) throws WebADEException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hasIndeterminateAuthorizations'");
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Role role)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Role role, boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Organization organization)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Organization organization,
            boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Role role, Organization organization)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public UserCredentials[] getAuthorizedUsers(Role role, Organization organization,
            boolean ignoreIndeterminateAuthorizationsErrors)
            throws IndeterminateAuthorizationsException, WebADEException {
        return new UserCredentials[0];
    }

    @Override
    public void shutdown() throws SecurityException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'shutdown'");
    }

    /**
     * Returns the WebADE preferences for the application.
     * @return A WebADEPreferences instance.
     * @throws WebADEException
     */
    public WebADEPreferences getWebADEPreferences() throws WebADEException {
        Class<?>[] PERMITTED_CALLING_CLASSES = new Class[] {WebADEFilter.class, WebAppRequestProcessingUtils.class};
        StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean result = SecurityUtils.checkStackCallAccess(stack, PERMITTED_CALLING_CLASSES);
        if (!result) {
            throw new SecurityException("Calling class '" + stack[1].getClassName()
                    + "' is not an instance of a class that is "
                    + "authorized to call this method.");
        }
        return ((WebADEDatabaseDatastore)this.datastore).getWebADEPreferences();
    }

}
