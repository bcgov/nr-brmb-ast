/**
 * @(#)UserOrganizationSelector.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.tags.beans;

import java.util.ArrayList;

import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.tags.UserAgreementTag;
import ca.bc.gov.webade.tags.UserOrganizationSelectorTag;
import ca.bc.gov.webade.user.WebADEUserPermissions;

/**
 * @author jross
 */
public class WebADETagUtils {
    private static final String NEWLINE = System.getProperty("line.separator");
    
    /**
     * Writes the user default organization form to a String and returns it.
     * @param user The user to select an organization for.
     * @param byOrgParam The org param that indicates what set of organizations to allow the user to select from.
     * @param path The path of the interrupted request.
     * @param params The request params of the interrupted request.
     * @return Returns the form html.
     */
    public static final String writeUserOrganizationForm(WebADEUserPermissions user, String byOrgParam, String path, String params) {
        return writeUserOrganizationForm(user, byOrgParam, path, params, null);
    }
    
    /**
     * Writes the user default organization form to a String and returns it.
     * @param user The user to select an organization for.
     * @param byOrgParam The org param that indicates what set of organizations to allow the user to select from.
     * @param path The path of the interrupted request.
     * @param params The request params of the interrupted request.
     * @param defaultOrg The user's default organization in the WebADE datastore, or null if none is set.
     * @return Returns the form html.
     */
    public static final String writeUserOrganizationForm(WebADEUserPermissions user, String byOrgParam, String path, String params, Organization defaultOrg) {
        Organization[] orgs;
        if (byOrgParam == null || byOrgParam.equals(UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_ALL_ORGANIZATIONS)) {
            orgs = user.getOrganizations();
        } else if (byOrgParam.equals(UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_GOVERNMENT_ORGANIZATION)) {
            orgs = user.getGovernmentOrganizations();
        } else {
            orgs = user.getNonGovernmentOrganizations();
        }
        return writeUserOrganizationForm(orgs, byOrgParam, path, params, defaultOrg);
    }
    
    /**
     * Writes the user default organization form to a String and returns it.
     * @param orgs The organizations to select from.
     * @param byOrgParam The org param that indicates what set of organizations to allow the user to select from.
     * @param path The path of the interrupted request.
     * @param params The request params of the interrupted request.
     * @return Returns the form html.
     */
    public static final String writeUserOrganizationForm(Organization[] orgs, String byOrgParam, String path, String params) {
        return writeUserOrganizationForm(orgs, byOrgParam, path, params, null);
    }
    
    /**
     * Writes the user default organization form to a String and returns it.
     * @param orgs The organizations to select from.
     * @param byOrgParam The org param that indicates what set of organizations to allow the user to select from.
     * @param path The path of the interrupted request.
     * @param params The request params of the interrupted request.
     * @param defaultOrg The user's default organization in the WebADE datastore, or null if none is set.
     * @return Returns the form html.
     */
    public static final String writeUserOrganizationForm(Organization[] orgs, String byOrgParam, String path, String params, Organization defaultOrg) {
        StringBuffer out = new StringBuffer();
        out.append(NEWLINE + "<div align=\"center\">");
        out.append(" <form name=\"userContextForm\" method='POST' action=\"" + path + "\">" + NEWLINE);
        String[][] parsedParams = parseParameters(params);
        for (int x = 0; x < parsedParams.length; x++) {
            out.append("   <input type=\"hidden\" name=\"" + parsedParams[x][0] + "\" value=\""+ parsedParams[x][1] + "\">" + NEWLINE);
        }
        out.append(" <table>" + NEWLINE);
        out.append(" <tr>" + NEWLINE);
        out.append(" <td style=\"border: 0px\" colspan=\"2\">" + NEWLINE);
        out.append(" <select name=\"" + UserOrganizationSelectorTag.WEBADE_USER_CONTEXT_SWITCH_PARAMETER + "\">" + NEWLINE);
        for (int x = 0; x < orgs.length; x++) {
            if (orgs[x] != null) {
                long value = orgs[x].getOrganizationId();
                String name = orgs[x].getName();
                out.append(" <option value=\"" + value + "\" " + ((orgs[x].equals(defaultOrg)) ? "selected" : "") + ">" + name + "</option>" + NEWLINE);
            }
        }
        out.append(" </select>" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" </tr>" + NEWLINE);
        out.append(" <tr>" + NEWLINE);
        out.append(" <td style=\"border:0px;text-align=left\" align=\"left\">" + NEWLINE);
        out.append("   Set as Default: <input type=\"checkbox\" name=\"" + UserOrganizationSelectorTag.SAVE_AS_DEFAULT_PARAMETER + "\" value=\"true\" " + ((defaultOrg != null) ? "checked" : "") + ">" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" <td style=\"border:0px;text-align=right\" align=\"right\">" + NEWLINE);
        out.append(" <input type=\"submit\" name=\"Continue\" value=\"Continue\"/>" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" </tr>" + NEWLINE);
        out.append(" </table>" + NEWLINE);
        out.append(" </form>" + NEWLINE);
        out.append(" </div>" + NEWLINE);
        return out.toString();
    }
    
    /**
     * Writes the user agreement form to a String and returns it.
     * @param agreementId The id of the target agreement.
     * @param agreementText The agreement text.
     * @param path The path of the interrupted request.
     * @param params The request params of the interrupted request.
     * @return Returns the form html.
     */
    public static final String writeUserAgreementForm(String agreementId, String agreementText, String path, String params) {
        StringBuffer out = new StringBuffer();
        out.append(NEWLINE + "<div align=\"center\">");
        out.append(" <form name=\"userAgreementForm\" method=\"POST\" action=\"" + path + "\">" + NEWLINE);
        out.append("   <input type=\"hidden\" name=\"" + UserAgreementTag.WEBADE_USER_AGREEMENT_ID_PARAMETER + "\" value=\"" + agreementId + "\">" + NEWLINE);
        String[][] parsedParams = parseParameters(params);
        for (int x = 0; x < parsedParams.length; x++) {
            out.append("   <input type=\"hidden\" name=\"" + parsedParams[x][0] + "\" value=\""+ parsedParams[x][1] + "\">" + NEWLINE);
        }
        out.append(" <table>" + NEWLINE);
        out.append(" <tr>" + NEWLINE);
        out.append(" <td style=\"border: 0px\" colspan=\"2\">" + NEWLINE);
        out.append("   <textarea readonly=\"true\" rows=\"10\" cols=\"40\">" + agreementText + "</textarea>" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" </tr>" + NEWLINE);
        out.append(" <tr>" + NEWLINE);
        out.append(" <td style=\"border:0px;text-align=left\" align=\"left\">" + NEWLINE);
        out.append("   Agree: <input type=\"checkbox\" name=\"" + UserAgreementTag.WEBADE_USER_AGREEMENT_IND_PARAMETER + "\" value=\"true\" >" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" <td style=\"border:0px;text-align=right\" align=\"right\">" + NEWLINE);
        out.append(" <input type=\"submit\" value=\"Submit\" />" + NEWLINE);
        out.append(" </td>" + NEWLINE);
        out.append(" </tr>" + NEWLINE);
        out.append(" </table>" + NEWLINE);
        out.append(" </form>" + NEWLINE);
        out.append(" </div>" + NEWLINE);
        return out.toString();
    }

    private static String[][] parseParameters(String parameters) {
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> values = new ArrayList<String>();
        if (parameters != null) {
            String s = parameters;
            int equalsIndex = -1;
            int andIndex = -1;
            do {
                equalsIndex = s.indexOf("=");
                andIndex = s.indexOf("&");
                if (equalsIndex > 0) { //Must be at least one character before
                    // '=' symbol.
                    String name = s.substring(0, equalsIndex);
                    String value = (andIndex == -1) ? s.substring(equalsIndex + 1) : s.substring(
                            equalsIndex + 1, andIndex);
                    names.add(name);
                    values.add(value);
                }
                if (andIndex > -1) {
                    s = (andIndex == s.length() - 1) ? "" : s.substring(andIndex + 1);
                }
            } while (andIndex > -1);
        }
        String[][] returnVals = new String[names.size()][2];
        for (int x = 0; x < names.size(); x++) {
            returnVals[x][0] = names.get(x);
            returnVals[x][1] = values.get(x);
        }
        return returnVals;
    }
}
