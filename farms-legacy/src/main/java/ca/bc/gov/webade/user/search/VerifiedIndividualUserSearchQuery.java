package ca.bc.gov.webade.user.search;

public interface VerifiedIndividualUserSearchQuery extends UserSearchQuery {

    /**
     * @return Returns the emailAddress.
     */
    public TextSearchAttribute getEmailAddress();

    /**
     * @return Returns the lastName.
     */
    public TextSearchAttribute getPhoneNumber();

    /**
     * @return Returns the middleInitial.
     */
    public TextSearchAttribute getMiddleInitial();

}
