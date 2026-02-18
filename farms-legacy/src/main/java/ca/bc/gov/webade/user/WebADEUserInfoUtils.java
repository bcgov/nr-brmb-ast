/**
 * @(#)WebADEUserInfoUtils.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;


/**
 * @author jross
 */
public class WebADEUserInfoUtils {

    private static final String SITE_MINDER_ACCOUNT_TYPE_INTERNAL = "Internal";
    private static final String SITE_MINDER_ACCOUNT_TYPE_BUSINESS = "Business";
    private static final String SITE_MINDER_ACCOUNT_TYPE_INDIVIDUAL = "Individual";
    private static final String SITE_MINDER_ACCOUNT_TYPE_VERIFIED_INDIVIDUAL = "VerifiedIndividual";

    /**
     * Helper constant for the '\' character.
     */
    public static final String BACKSLASH = "\\";
    /**
     * Helper constant for the '/' character.
     */
    public static final String FORWARDSLASH = "/";

    /**
     * Converts the given GUID between native GUID and Microsoft GUID and
     * returns the result. For example: 00D26B7C1AECEE4F8B8353B74D2E5BA5 to
     * 7C6BD200EC1A4FEE8B8353B74D2E5BA5
     * @param guid
     *            The guid to convert.
     * @return The converted guid.
     */
    public static final String convertGUIDFormat(String guid) {
        if (guid == null || guid.length() != 32) {
            return null;
        }
        String segment1 = guid.substring(0, 2).toUpperCase();
        String segment2 = guid.substring(2, 4).toUpperCase();
        String segment3 = guid.substring(4, 6).toUpperCase();
        String segment4 = guid.substring(6, 8).toUpperCase();
        String segment5 = guid.substring(8, 10).toUpperCase();
        String segment6 = guid.substring(10, 12).toUpperCase();
        String segment7 = guid.substring(12, 14).toUpperCase();
        String segment8 = guid.substring(14, 16).toUpperCase();
        String segment9 = guid.substring(16).toUpperCase();
        String convertedGuid = segment4 + segment3 + segment2 + segment1
                + segment6 + segment5 + segment8 + segment7 + segment9;
        return convertedGuid;
    }

    /**
     * Converts '\' characters to '/' characters in the given string and returns
     * the result.
     * @param value
     *            The value to convert.
     * @return The converted string.
     */
    public static final String convertBackSlashesToForward(String value) {
        String convertedString = value;
        if (value != null) {
            convertedString = convertedString.replace('\\', '/');
        }
        return convertedString;
    }

    /**
     * Converts '/' characters to '\' characters in the given string and returns
     * the result.
     * @param value
     *            The value to convert.
     * @return The converted string.
     */
    public static final String convertForwardSlashesToBack(String value) {
        String convertedString = value;
        if (value != null) {
            convertedString = convertedString.replace('/', '\\');
        }
        return convertedString;
    }

    /**
     * Extracts a user's account name from a "domain/user" format.
     * @param userId
     *            The user's fully-qualified id.
     * @return The user's account name.
     */
    public static final String extractAccountName(String userId) {
        if (userId != null && userId.indexOf(BACKSLASH) > -1) {
            userId = userId.substring(userId.indexOf(BACKSLASH) + 1);
        } else if (userId != null && userId.indexOf(FORWARDSLASH) > -1) {
            userId = userId.substring(userId.indexOf(FORWARDSLASH) + 1);
        }
        return userId;
    }

    /**
     * Extracts a user's source directory, or domain, from a "domain/user"
     * format.
     * @param userId
     *            The user's fully-qualified id.
     * @return The user's source directory.
     */
    public static final String extractSourceDirectory(String userId) {
        String dirName = null;
        if (userId != null && userId.indexOf(BACKSLASH) > -1) {
            dirName = userId.substring(0, userId.indexOf(BACKSLASH));
        } else if (userId != null && userId.indexOf(FORWARDSLASH) > -1) {
            dirName = userId.substring(0, userId.indexOf(FORWARDSLASH));
        }
        return dirName;
    }

    /**
     * Strips all non-numeric characters from the given String, returning only
     * the numeric string.
     * @param inputString
     * @return The given string without non-numeric characters.
     */
    public static final String stripNonNumericCharacters(String inputString) {
        if (inputString == null) {
            return inputString;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inputString.length(); i++) {
            if (Character.isDigit(inputString.charAt(i))) {
                sb.append(inputString.charAt(i));
            }
        }
        return sb.toString();
    }

    @Deprecated
    public static final String userTypeToBCeIDAccountType(String webADEUserTypeCode) {
      UserTypeCode webADEUserType = UserTypeCode.getUserTypeCode(webADEUserTypeCode);
      return userTypeToBCeIDAccountType(webADEUserType);
    }
    
    public static final String userTypeToSiteMinderAccountType(String webADEUserTypeCode) {
        UserTypeCode webADEUserType = UserTypeCode.getUserTypeCode(webADEUserTypeCode);
        return userTypeToSiteMinderAccountType(webADEUserType);
      }

    @Deprecated
    public static final String userTypeToBCeIDAccountType(UserTypeCode webADEUserType) {
        return userTypeToSiteMinderAccountType(webADEUserType);
    }

    public static final String userTypeToSiteMinderAccountType(UserTypeCode webADEUserType) {
        String bceidAccountType = null;
        if (UserTypeCode.GOVERNMENT.equals(webADEUserType)) {
          bceidAccountType = SITE_MINDER_ACCOUNT_TYPE_INTERNAL;
        } else if (UserTypeCode.BUSINESS_PARTNER.equals(webADEUserType)) {
          bceidAccountType = SITE_MINDER_ACCOUNT_TYPE_BUSINESS;
        } else if (UserTypeCode.INDIVIDUAL.equals(webADEUserType)) {
          bceidAccountType = SITE_MINDER_ACCOUNT_TYPE_INDIVIDUAL;
        } else if (UserTypeCode.VERIFIED_INDIVIDUAL.equals(webADEUserType)) {
          bceidAccountType = SITE_MINDER_ACCOUNT_TYPE_VERIFIED_INDIVIDUAL;
        } else if(UserTypeCode.BC_SERVICES_CARD.equals(webADEUserType)) {
            bceidAccountType = SITE_MINDER_ACCOUNT_TYPE_VERIFIED_INDIVIDUAL;
        }
        return bceidAccountType;
    }

}
