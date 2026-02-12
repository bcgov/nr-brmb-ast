/**
 * @(#)DefaultHtmlPageWriter.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.WebADEUtils;
import ca.bc.gov.webade.j2ee.WebAppInitializationUtils;
import ca.bc.gov.webade.tags.beans.WebADETagUtils;
import ca.bc.gov.webade.user.WebADEUserPermissions;

/**
 * @author jross
 */
public final class DefaultHtmlPageWriter {
    private static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Writes the application initialization error page to a String a returns
     * it.
     * @return The application initialization error page.
     */
    public static final String writeInitializationErrorPage() {
        String titleText = "Application Initialization Error";
        String msg = "This application has been temporarily "
                + "disabled due to an initialization error. Please "
                + "contact the application administrator for support.";
        String errorMessage = WebAppInitializationUtils
                .getInitializationErrorMessage();
        if (errorMessage != null) {
            msg += "<p>Reason:<br /><br />" + errorMessage + "</p>";
        }
        return writePage(titleText, msg);
    }

    /**
     * Writes the default application disabled page to a String a returns it.
     * @param errorOnDisabledCheck
     *            A flag indicating whether an error occured while checking the
     *            application's disabled flag.
     * @return The user default application disabled page.
     */
    public static final String writeApplicationDisabledPage(
            boolean errorOnDisabledCheck) {
        return writeApplicationDisabledPage(null, errorOnDisabledCheck);
    }

    /**
     * Writes the default application disabled page to a String a returns it.
     * @param appDisabledMessage 
     *            The message to display to the user if the application has been disabled.
     * @param errorOnDisabledCheck
     *            A flag indicating whether an error occured while checking the
     *            application's disabled flag.
     * @return The user default application disabled page.
     */
    public static final String writeApplicationDisabledPage(String appDisabledMessage,
            boolean errorOnDisabledCheck) {
        String titleText = "Application Disabled";
        String msg;
        if (appDisabledMessage != null && appDisabledMessage.trim() != null) {
            msg = appDisabledMessage.trim();
        } else {
            msg = "This WebADE application has been temporarily "
                    + "disabled by the application administrator. Please "
                    + "try again later.";
        }
        if (errorOnDisabledCheck) {
            msg = "This WebADE application has been temporarily "
                    + "disabled because of an internal error. Please "
                    + "contact the application administrator for support.";
        }
        return writePage(titleText, msg);
    }

    /**
     * Writes the user default organization form page to a String a returns it.
     * @param user
     *            The user to select an organization for.
     * @param byOrgParam
     *            The org param that indicates what set of organizations to
     *            allow the user to select from.
     * @param path
     *            The path of the interrupted request.
     * @param params
     *            The request params of the interrupted request.
     * @param defaultOrg
     *            The user's default organization in the WebADE datastore, or
     *            null if none is set.
     * @return The user default organization form page.
     * @throws UnsupportedEncodingException
     */
    public static final String writeUserOrganizationPage(
            WebADEUserPermissions user, String byOrgParam, String path,
            String params, Organization defaultOrg)
            throws UnsupportedEncodingException {
        if (path != null) {
            path = URLDecoder.decode(path, WebADEUtils.DEFAULT_ENCODING);
        }
        if (params != null) {
            params = URLDecoder.decode(params, WebADEUtils.DEFAULT_ENCODING);
        }
        String form = WebADETagUtils.writeUserOrganizationForm(user,
                byOrgParam, path, params, defaultOrg);

        String title = "WebADE Organization Selector";
        String msg = "We have detected that you have the ability to act on behalf of multiple organizations.<br /><br />"
                + "From this screen you can choose which organization you are going to do work on behalf "
                + "of for this session. Once you have selected an organization from the list, choose "
                + "&quot;<b>Continue</b>&quot; and you will be acting on behalf of that organization "
                + "for the entire session. In order to change to another organization, simply close the "
                + "browser and restart your session with this application and you will be prompted with "
                + "this dialog again.<br /><br />" + NEWLINE + form;

        return writePage(title, msg);
    }

    /**
     * Writes the user agreement form page to a String a returns it.
     * @param agreementId
     *            The target agreement id.
     * @param agreementText
     *            The target agreement text.
     * @param path
     *            The path of the interrupted request.
     * @param params
     *            The request params of the interrupted request.
     * @return The user agreement form page.
     */
    public static final String writeUserAgreementPage(String agreementId,
            String agreementText, String path, String params) {
        if (path != null) {
            try {
                path = URLDecoder.decode(path, WebADEUtils.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (params != null) {
            try {
                params = URLDecoder
                        .decode(params, WebADEUtils.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String form = WebADETagUtils.writeUserAgreementForm(agreementId,
                agreementText, path, params);

        String titleText = "WebADE User Agreement";
        String msg = form;
        return writePage(titleText, msg);
    }

    private static final String writePage(String titleText, String msg) {
        StringBuffer out = new StringBuffer();
        out.append("<html>");
        out.append(NEWLINE);
        out.append("<head>");
        out.append(NEWLINE);
        out.append("<title>");
        out.append(titleText);
        out.append("</title>");
        out.append(NEWLINE);
        out.append("<style type=\"text/css\">");
        out.append(NEWLINE);
        out.append("<!--");
        out.append(NEWLINE);
        out.append(".dialog th {");
        out.append(NEWLINE);
        out.append("	font-family: Verdana, Arial, Helvetica, sans-serif;");
        out.append(NEWLINE);
        out.append("	font-size: 10pt;");
        out.append(NEWLINE);
        out.append("	font-weight: bold;");
        out.append(NEWLINE);
        out.append("	text-align: left;");
        out.append(NEWLINE);
        out.append("	padding: 5px;");
        out.append(NEWLINE);
        out.append("	background-color: #0000FF;");
        out.append(NEWLINE);
        out.append("	color: #FFFFFF;");
        out.append(NEWLINE);
        out.append("}");
        out.append(NEWLINE);
        out.append(".dialog td {");
        out.append(NEWLINE);
        out.append("	font-family: Verdana, Arial, Helvetica, sans-serif;");
        out.append(NEWLINE);
        out.append("	font-size: 10pt;");
        out.append(NEWLINE);
        out.append("	text-align: left;");
        out.append(NEWLINE);
        out.append("	padding: 10px;");
        out.append(NEWLINE);
        out.append("	border: 1px solid #000000;");
        out.append(NEWLINE);
        out.append("}");
        out.append(NEWLINE);
        out.append(".dialog p {");
        out.append(NEWLINE);
        out.append("    font-family: Verdana, Arial, Helvetica, sans-serif;");
        out.append(NEWLINE);
        out.append("    font-size: 8pt;");
        out.append(NEWLINE);
        out.append("    text-align: left;");
        out.append(NEWLINE);
        out.append("    padding: 10px;");
        out.append(NEWLINE);
        out.append("    border: 1px solid #000000;");
        out.append(NEWLINE);
        out.append("}");
        out.append(NEWLINE);
        out.append("-->");
        out.append(NEWLINE);
        out.append("</style>");
        out.append(NEWLINE);
        out.append("</head>");
        out.append(NEWLINE);
        out.append("<body>");
        out.append(NEWLINE);
        out.append("  <table width=\"100%\" height=\"100%\"  border=\"0\" "
                + "cellpadding=\"0\" cellspacing=\"0\">");
        out.append(NEWLINE);
        out.append("    <tr>");
        out.append(NEWLINE);
        out.append("      <td align=\"center\" valign=\"middle\">");
        out.append(NEWLINE);
        out.append("	  <table width=\"400\"  border=\"0\" align=\"center\" "
                + "cellpadding=\"0\" cellspacing=\"0\" class=\"dialog\">");
        out.append(NEWLINE);
        out.append("        <tr>");
        out.append(NEWLINE);
        out.append("          <th>");
        out.append(titleText);
        out.append("</th>");
        out.append(NEWLINE);
        out.append("        </tr>");
        out.append(NEWLINE);
        out.append("        <tr>");
        out.append(NEWLINE);
        out.append("          <td>");
        out.append(msg);
        out.append("</td>");
        out.append(NEWLINE);
        out.append("        </tr>");
        out.append(NEWLINE);
        out.append("      </table>");
        out.append(NEWLINE);
        out.append("	  </td>");
        out.append(NEWLINE);
        out.append("    </tr>");
        out.append(NEWLINE);
        out.append("  </table>");
        out.append(NEWLINE);
        out.append("</body>");
        out.append(NEWLINE);
        out.append("</html>");
        return out.toString();
    }
}
