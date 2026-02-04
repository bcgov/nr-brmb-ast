package ca.bc.gov.webade.user;

import java.io.Serializable;

/**
 * @author jross
 */
public final class UserTypeCode implements Serializable {
    private static final long serialVersionUID = 6672483592026101234L;

    /** Government user type code value. */
    public static final UserTypeCode GOVERNMENT = new UserTypeCode("GOV", "Government");

    /** Business Partner user type code value. */
    public static final UserTypeCode BUSINESS_PARTNER = new UserTypeCode("BUP", "Business Partner");

    /** Individual user type code value. */
    public static final UserTypeCode VERIFIED_INDIVIDUAL = new UserTypeCode("VIN", "Verified Individual");

    /** Individual user type code value. */
    public static final UserTypeCode INDIVIDUAL = new UserTypeCode("UIN", "Unverified Individual");

    /** Individual user type code value. */
    public static final UserTypeCode SERVICE_CLIENT = new UserTypeCode("SCL", "Service Client");

    /** Individual user type code value. */
    public static final UserTypeCode BC_SERVICES_CARD = new UserTypeCode("BCS", "BC Services Card");

    /**
     * Returns the matching UserTypeCode instance for the given code value, or
     * null if no match is found.
     * 
     * @param codeValue
     *            The code value string.
     * @return The matching UserTypeCode instance.
     */
    public static final UserTypeCode getUserTypeCode(String codeValue) {
        UserTypeCode code = null;
        if (GOVERNMENT.getCodeValue().equals(codeValue)) {
            code = GOVERNMENT;
        } else if (BUSINESS_PARTNER.getCodeValue().equals(codeValue)) {
            code = BUSINESS_PARTNER;
        } else if (VERIFIED_INDIVIDUAL.getCodeValue().equals(codeValue)) {
            code = VERIFIED_INDIVIDUAL;
        } else if (INDIVIDUAL.getCodeValue().equals(codeValue)) {
            code = INDIVIDUAL;
        } else if (SERVICE_CLIENT.getCodeValue().equals(codeValue)) {
            code = SERVICE_CLIENT;
        } else if (BC_SERVICES_CARD.getCodeValue().equals(codeValue)) {
            code = BC_SERVICES_CARD;
        }
        return code;
    }

    private String userTypeCode;
    private String description;

    private UserTypeCode(String userTypeCode, String description) {
        this.userTypeCode = userTypeCode;
        this.description = description;
    }

    /**
     * @return Returns the userTypeCode.
     */
    public String getCodeValue() {
        return userTypeCode;
    }

    /**
     * @return Returns the userTypeCode description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        return (obj instanceof UserTypeCode)
                && ((UserTypeCode)obj).userTypeCode.equals(this.userTypeCode);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() {
        return this.userTypeCode.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
	public String toString() {
        return this.userTypeCode;
    }

}
