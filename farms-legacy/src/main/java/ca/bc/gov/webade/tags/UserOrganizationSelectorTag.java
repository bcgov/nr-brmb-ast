/**
 * @(#)UserOrganizationSelectorTag.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.tags;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.tags.beans.WebADETagUtils;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.WebADEUtils;

/**
 * @author jross
 */
public class UserOrganizationSelectorTag extends TagSupport {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The request attribute containing the original query to be forwarded to
     * after the user selects a location.
     */
    public static final String PATH = "path";
    /**
     * The request attribute containing the parameters of the original request.
     */
    public static final String PARAMETERS = "parameters";
    /**
     * The attribute name for the form parameter used to allow a user with
     * multiple client location codes to select one as his default when logging
     * in to the application.
     */
    public static final String WEBADE_USER_CONTEXT_SWITCH_PARAMETER = "webade.user.context.switch.value";
    /**
     * The name of the request parameter that contains the indicator that
     * defines which organizations the user can select from when selecting a
     * default organization.
     */
    public static final String WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER = "webade.user.select.by.organization.type";
    /**
     * The name of the request parameter that contains the 
     * default organization of the current user as set in the WebADE datastore.
     */
    public static final String WEBADE_USER_DEFAULT_ORGANIZATION_PARAMETER = "webade.user.default.organization";
    /**
     * The name of the request parameter that contains the indicator that
     * defines to save the selected organization as the user's default 
     * organization.
     */
    public static final String SAVE_AS_DEFAULT_PARAMETER = "webade.save.as.default.organization";
    /**
     * The value of the WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER
     * request parameter indicating to only allow government organizations in
     * the selected list.
     */
    public static final String WEBADE_USER_SELECT_BY_GOVERNMENT_ORGANIZATION = "government";
    /**
     * The value of the WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER
     * request parameter indicating to only allow non-government organizations
     * in the selected list.
     */
    public static final String WEBADE_USER_SELECT_BY_NON_GOVERNMENT_ORGANIZATION = "non-government";
    /**
     * The value of the WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER
     * request parameter indicating to only all organizations in the selected
     * list.
     */
    public static final String WEBADE_USER_SELECT_BY_ALL_ORGANIZATIONS = "all";

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
	public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        ServletRequest request = this.pageContext.getRequest();
        HttpSession session = this.pageContext.getSession();
        String path = (String)request.getAttribute(PATH);
        String params = (String)request.getAttribute(PARAMETERS);
        if (path != null) {
            try {
                path = URLDecoder.decode(path, WebADEUtils.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
            	throw new JspException(e.getMessage());
            }
        }
        if (params != null) {
            try {
                params = URLDecoder.decode(params, WebADEUtils.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
            	throw new JspException(e.getMessage());
            }
        }
        WebADEUserPermissions user = null;
        if (session != null) {
            user = (WebADEUserPermissions)session.getAttribute(HttpRequestUtils.CURRENT_WEBADE_USER_PERMISSIONS);
        }
        if (user == null) {
            user = (WebADEUserPermissions)request.getAttribute(HttpRequestUtils.CURRENT_WEBADE_USER_PERMISSIONS);
        }

        String byOrgParam = (String)request.getAttribute(WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER);
        Organization defaultOrgParam = (Organization)request.getAttribute(WEBADE_USER_DEFAULT_ORGANIZATION_PARAMETER);
        String form = WebADETagUtils.writeUserOrganizationForm(user, byOrgParam, path, params, defaultOrgParam);
        try {
            out.println(form);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return EVAL_PAGE;
    }
}