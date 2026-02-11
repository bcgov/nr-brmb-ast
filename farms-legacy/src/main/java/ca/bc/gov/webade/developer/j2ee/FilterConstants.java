package ca.bc.gov.webade.developer.j2ee;

/**
 * Constants used by the Developer Filter.
 * 
 * @author Vivid Solutions Inc
 */
class FilterConstants {

    static final String WEBADE_DEVELOPER_USERGUID = "ca.bc.gov.webade.developer.filter.USERGUID";
    static final String WEBADE_DEVELOPER_PASSWORD = "ca.bc.gov.webade.developer.filter.PASSWORD";
    static final String WEBADE_DEVELOPER_LOGON_TYPE = "ca.bc.gov.webade.developer.filter.LOGON_TYPE";

    static final String INVALID_LOGIN_MESSAGE = "The user / password combination was invalid.";

    static final String SESSION_AUTHENTICATION = "webade.developer.module.authentication.method";
    static final String LOGON_TYPE_ANONYMOUS = "anonymous";
    static final String LOGON_TYPE_AUTHENTICATED = "authenticated";

    static final String PASSWORD_ENABLED = "password-enabled";
    static final String FILTER_USERS_BY_ACCESS = "filter-users-by-access";
    static final String AUTHENTICATION_METHOD = "authentication-method";
    static final String AUTHENTICATION_FORM = "form";
    static final String AUTHENTICATION_BASIC = "basic";
    static final String AUTHENTICATION_FORM_PLUS_BASIC = "form+basic";
    static final String AUTHENTICATION_REALM = "authentication-realm";

    static final String USE_COOKIE = "use-cookie";
    static final String COOKIE_DOMAIN = "cookie-domain";
    static final String COOKIE_NAME = "WEBADEUSERGUID";
}
