/**
 * @(#)UserAgreementTag.java
 * Copyright (c) 2004, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.tags;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import ca.bc.gov.webade.tags.beans.WebADETagUtils;
import ca.bc.gov.webade.WebADEUtils;

/**
 * @author jross
 */
public class UserAgreementTag extends TagSupport {
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
     * The attribute name for the form parameter used to which agreement the user is agreeing to.
     */
    public static final String WEBADE_USER_AGREEMENT_ID_PARAMETER = "webade.user.agreement.id";
    /**
     * The attribute name for the form parameter used to which if the user is agreeing to the specified agreement.
     */
    public static final String WEBADE_USER_AGREEMENT_IND_PARAMETER = "webade.user.agreement.ind";
    /**
     * The request parameter containing the agreement text.
     */
    public static final String WEBADE_AGREEMENT_TEXT = "webade.agreement.text";

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
	public int doStartTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        ServletRequest request = this.pageContext.getRequest();

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

        String agreementId = (String)request.getAttribute(WEBADE_USER_AGREEMENT_ID_PARAMETER);
        String agreementText = (String)request.getAttribute(WEBADE_AGREEMENT_TEXT);
        String form = WebADETagUtils.writeUserAgreementForm(agreementId, agreementText, path, params);
        try {
            out.println(form);
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return EVAL_PAGE;
    }

}
