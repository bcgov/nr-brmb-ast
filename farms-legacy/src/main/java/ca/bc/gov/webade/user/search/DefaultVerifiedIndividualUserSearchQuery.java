package ca.bc.gov.webade.user.search;

import ca.bc.gov.webade.user.UserTypeCode;

public class DefaultVerifiedIndividualUserSearchQuery extends
		AbstractUserSearchQuery implements VerifiedIndividualUserSearchQuery {

	private static final long serialVersionUID = -507715773257892905L;

	@Override
	public UserTypeCode getDirectoryUserType() {
		return UserTypeCode.VERIFIED_INDIVIDUAL;
	}

}
