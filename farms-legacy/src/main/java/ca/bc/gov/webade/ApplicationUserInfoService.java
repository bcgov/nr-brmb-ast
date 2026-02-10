package ca.bc.gov.webade;

import java.io.Serializable;

import ca.bc.gov.webade.user.GUID;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.search.UserSearchQuery;
import ca.bc.gov.webade.user.service.UserInfoService;
import ca.bc.gov.webade.user.service.UserInfoServiceException;

final class ApplicationUserInfoService implements UserInfoService, Serializable {
    private static final long serialVersionUID = 5021524966929485274L;

    private WebADEDatastore datastore;

    ApplicationUserInfoService(WebADEDatastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public boolean handlesSourceDirectory(String sourceDirectory) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handlesSourceDirectory'");
    }

    @Override
    public boolean handlesUserType(UserTypeCode userType) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handlesUserType'");
    }

    @Override
    public String getSourceDirectoryForUserType(UserTypeCode userType) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSourceDirectoryForUserType'");
    }

    @Override
    public UserTypeCode getUserTypeForSourceDirectory(String sourceDirectory) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserTypeForSourceDirectory'");
    }

    @Override
    public String[] getSupportedSourceDirectories() throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupportedSourceDirectories'");
    }

    @Override
    public UserTypeCode[] getSupportedUserTypes() throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSupportedUserTypes'");
    }

    @Override
    public UserTypeCode[] getSearchableUserTypes() throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSearchableUserTypes'");
    }

    @Override
    public WebADEUserInfo getWebADEUserInfo(UserCredentials userCredentials) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWebADEUserInfo'");
    }

    @Override
    public WebADEUserInfo getWebADEUserInfo(UserCredentials userCredentials, boolean ignoreSessionCache)
            throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWebADEUserInfo'");
    }

    @Override
    public boolean isUserInGroup(UserCredentials userCredentials, GUID groupGuid) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserInGroup'");
    }

    @Override
    public GUID[] isUserInGroups(UserCredentials userCredentials, GUID[] groupGuids) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isUserInGroups'");
    }

    @Override
    public UserSearchQuery createUserSearchQuery(UserTypeCode userType) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createUserSearchQuery'");
    }

    @Override
    public WebADEUserInfo[] findUsers(UserSearchQuery searchQuery) throws UserInfoServiceException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findUsers'");
    }

    @Override
    public void setProviderRequestorUserCredentials(UserCredentials userCredentials) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProviderRequestorUserCredentials'");
    }

}
