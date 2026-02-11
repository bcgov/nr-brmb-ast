package ca.bc.gov.webade.developer.j2ee;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.AppRoles;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Role;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.developer.WebADEDeveloperException;
import ca.bc.gov.webade.developer.util.IOUtils;
import ca.bc.gov.webade.http.HttpRequestUtils;
import ca.bc.gov.webade.user.UserTypeCode;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;

/**
 * Generator for authentication form page.
 * 
 * @author Vivid Solutions Inc
 */
class LoginPageGenerator {

	public static final String XML_LOGON_FILE_NAME = "logon.htm";
	
	private static final Logger logger = LoggerFactory.getLogger(LoginPageGenerator.class);
	private FilterConfigurator filterConfigurator;
	private static final String NEW_LINE = "\n";

	LoginPageGenerator(FilterConfigurator filterConfigurator) {
		this.filterConfigurator = filterConfigurator;
	}

	String loginPage(HttpServletRequest request, List<WebADEUserInfo> users, String error) throws WebADEDeveloperException {
		String loginPageSource = null;
		try {
			InputStream is = this.getClass().getResourceAsStream("/" + XML_LOGON_FILE_NAME);
			loginPageSource = IOUtils.streamToString(is);
			loginPageSource = replacePlaceholder(loginPageSource, "\\{action\\}", getPageRelativePath(request));
			loginPageSource = replacePlaceholder(loginPageSource, "\\{method\\}", getMethod(request));
			loginPageSource = replacePlaceholder(loginPageSource, "\\{hidden\\}", getHidden(request));

			StringBuffer buffer1 = new StringBuffer(getApplication(request).getApplicationCode());
			StringBuffer buffer2 = new StringBuffer("");
			StringBuffer buffer3 = new StringBuffer(FilterConstants.WEBADE_DEVELOPER_LOGON_TYPE);
			StringBuffer buffer4 = new StringBuffer("display='none'");
			StringBuffer buffer5 = new StringBuffer("");
			StringBuffer buffer6 = new StringBuffer("");
			StringBuffer buffer7 = new StringBuffer("");

			if (error != null) {
				buffer2.append("<p class=\"error\">" + error + "</p>").append(NEW_LINE);
			}
			if (users == null || users.size() == 0) {
				buffer7.append("<p>The WebADE Developer Module could not find any users in your webade user info xml file.</p>").append(NEW_LINE);
				buffer7.append("<p>Check your user info xml file is valid and you have configured the webade developer module in WEB-INF/web.xml.</p>").append(NEW_LINE);
			} else {
				// filter user list by access privileges
				List<WebADEUserInfo> usersWithAccess = getUsersWithAccess(request, users);

				// sort users by login
				UserLoginComparator comparator = new UserLoginComparator();
				Collections.sort(usersWithAccess, comparator);

				Iterator<WebADEUserInfo> i = usersWithAccess.iterator();
				if (i.hasNext()) {
					buffer4 = new StringBuffer("display='block'");
					buffer5.append("<select id=\"userSelect\" name=\"").append(FilterConstants.WEBADE_DEVELOPER_USERGUID).append("\"").append(" onchange=\"showRoles()\"").append(">").append(NEW_LINE);
					while (i.hasNext()) {
						WebADEUserInfo userInfo = i.next();
						buffer5.append("<option value =\"" + userInfo.getUserCredentials().getUserGuid().toMicrosoftGUIDString() + "\">");
						
						String username = userInfo.getUserCredentials().getUserId().replaceAll("\\\\", "\\\\\\\\");
						
						if(UserTypeCode.BC_SERVICES_CARD.equals(userInfo.getUserCredentials().getUserTypeCode())) {
							if(userInfo.getFirstName()!=null&&userInfo.getLastName()!=null) {
								username = username+" ("+userInfo.getFirstName()+" "+userInfo.getLastName() + ")";
							}
						}
						
						buffer5.append(username);
						buffer5.append("</option>").append(NEW_LINE);

						buffer6.append("<option value =\"" + userInfo.getUserCredentials().getUserGuid().toMicrosoftGUIDString() + "\">");
						buffer6.append(getRolesString(request, userInfo));
						buffer6.append("</option>").append(NEW_LINE);
					}
					buffer5.append("</select>").append(NEW_LINE);
					if (filterConfigurator.isPasswordEnabled()) {
						buffer5.append("<h3>Password:</h3>").append(NEW_LINE);
						buffer5.append("<input type=\"password\" name=\"" + FilterConstants.WEBADE_DEVELOPER_PASSWORD + "\" />").append(NEW_LINE);
					}
				} else {
					buffer7.append("<p>No authorized users for this application were found in the user info xml file.</p>").append(NEW_LINE);
					buffer7.append("<p>One or more users must be authorized to access this application using WebADE.</p>").append(NEW_LINE);
				}
			}
			loginPageSource = loginPageSource.replaceAll("\\{1\\}", buffer1.toString());
			loginPageSource = loginPageSource.replaceAll("\\{2\\}", buffer2.toString());
			loginPageSource = loginPageSource.replaceAll("\\{3\\}", buffer3.toString());
			loginPageSource = loginPageSource.replaceAll("\\{4\\}", buffer4.toString());
			loginPageSource = loginPageSource.replaceAll("\\{5\\}", buffer5.toString());
			loginPageSource = loginPageSource.replaceAll("\\{6\\}", buffer6.toString());
			loginPageSource = loginPageSource.replaceAll("\\{7\\}", buffer7.toString());
		} catch (IOException e) {
			throw new WebADEDeveloperException(e.getMessage(), e);
		}
		return loginPageSource;
	}

	private static Application getApplication(HttpServletRequest request) throws WebADEDeveloperException {
		Application app = HttpRequestUtils.getApplication(request.getSession().getServletContext());
		if (app == null) {
			throw new WebADEDeveloperException("WebADE is not responding. Verify the database connection is operating.");
		}
		return app;
	}

	private static String getRolesString(HttpServletRequest request, WebADEUserInfo userInfo) throws WebADEDeveloperException {
		Application app = getApplication(request);
		AppRoles appRoles = app.getRoles();
		String[] roleNames = appRoles.getRoleNames();
		List<Role> roleList = new ArrayList<Role>();
		try {
			WebADEUserPermissions permissions = app.getWebADEUserPermissions(userInfo.getUserCredentials());
			if (permissions != null) {
				for (int i = 0; i < roleNames.length; i++) {
					Role role = appRoles.getRole(roleNames[i]);
					if (permissions.isUserInRole(role)) {
						roleList.add(role);
					}
				}
			}
		} catch (WebADEException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		ListIterator<Role> i = roleList.listIterator();
		if (i.hasNext()) {
			Role role = i.next();
			sb.append(role.getName());
		}
		while (i.hasNext()) {
			sb.append(", ");
			Role role = i.next();
			sb.append(role.getName());
		}
		return sb.toString();
	}

	private List<WebADEUserInfo> getUsersWithAccess(HttpServletRequest request, List<WebADEUserInfo> users) throws WebADEDeveloperException {
		List<WebADEUserInfo> usersWithAccess = new ArrayList<WebADEUserInfo>();
		if (filterConfigurator.filterUsersByAccess()) {
			Application app = getApplication(request);
			AppRoles appRoles = app.getRoles();
			String[] roleNames = appRoles.getRoleNames();

			Iterator<WebADEUserInfo> itr = users.iterator();
			if (itr.hasNext()) {
				while (itr.hasNext()) {
					WebADEUserInfo userInfo = itr.next();
					try {
						WebADEUserPermissions permissions = app.getWebADEUserPermissions(userInfo.getUserCredentials());
						for (int i = 0; i < roleNames.length; i++) {
							Role role = appRoles.getRole(roleNames[i]);
							if (permissions.isUserInRole(role)) {
								usersWithAccess.add(userInfo);
								break;
							}
						}
					} catch (WebADEException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}
			}
		} else {
			usersWithAccess = users;
		}
		return usersWithAccess;
	}

	private static String getMethod(HttpServletRequest request) {
		String method = request.getMethod();
		if (logger.isDebugEnabled()) {
			logger.debug("method = " + method);
		}
		return method;
	}

	private static String getHidden(HttpServletRequest request) {
		StringBuffer hidden = new StringBuffer("");

		Map<String, String[]> parameterMap = request.getParameterMap();
		Iterator<String> i = parameterMap.keySet().iterator();
		while (i.hasNext()) {
			String parameterKey = i.next();
			String[] parameterValues = parameterMap.get(parameterKey);
			for (int j = 0; j < parameterValues.length; j++) {
				hidden.append("<input type=\"hidden\" name=\"" + parameterKey + "\" value=\"" + parameterValues[j] + "\" />").append(NEW_LINE);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("hidden = " + hidden.toString());
		}
		return hidden.toString();
	}

	private static String getPageRelativePath(HttpServletRequest request) {
		if (logger.isDebugEnabled()) {
			logger.debug("request.getContextPath() = " + request.getContextPath());
			logger.debug("request.getPathInfo() = " + request.getPathInfo());
			logger.debug("request.getPathTranslated() = " + request.getPathTranslated());
			logger.debug("request.getProtocol() = " + request.getProtocol());
			logger.debug("request.getQueryString() = " + request.getQueryString());
			logger.debug("request.getRequestURI() = " + request.getRequestURI());
			logger.debug("request.getServerName() = " + request.getServerName());
			logger.debug("request.getServerPort() = " + request.getServerPort());
			logger.debug("request.getServletPath() = " + request.getServletPath());
			logger.debug("request.getRequestURL() = " + request.getRequestURL());
			logger.debug("request.getServerPort() = " + request.getServerPort());
		}
		StringBuffer url = new StringBuffer();
		String pageRelativePath = request.getRequestURI();
		if (logger.isDebugEnabled()) {
			logger.debug("pageRelativePath = " + pageRelativePath);
		}
		url.append(pageRelativePath);
		if (logger.isDebugEnabled()) {
			logger.debug("url = " + url.toString());
		}
		return url.toString();
	}

	static String replacePlaceholder(String source, String placeholder, String replacement) {
		return source.replaceFirst(placeholder, quoteReplacement(replacement));
	}

	private static String quoteReplacement(String s) {
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1))
			return s;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				sb.append('\\');
				sb.append('\\');
			} else if (c == '$') {
				sb.append('\\');
				sb.append('$');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
