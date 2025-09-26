/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.jasper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.log.LoggingUtils;

/**
 * @author awilkinson
 */
public class JasperConnection {

  private Logger logger = LoggerFactory.getLogger(getClass());

  // Name of the cookie that maintain a jasper session
  public final static String JSESSIONID_NAME = "JSESSIONID";
  public final static String LOADBALANCING_NAME = "ROUTEID0";
  public final static String LOGIN_PATH = "rest/login";
  public final static String LOGOUT_PATH = "logout.html";
  public final static String REPORTS_PATH = "rest_v2/reports/";

  private String restContextUrl;
  private String j_password;
  private String j_username;
  private String loginPostData;
  private URL loginUrl;
  private URL logoutUrl;
  private String reportsUrlString;

  private Map<String, Cookie> cookieMap;

  private static final String ERROR_PREFIX_LOGIN = "Login error: ";
  private static final String ERROR_PREFIX_LOGOUT = "Logout error: ";
  private static final String ERROR_PREFIX_FETCH_REPORT = "Error fetching report: ";
  
  private static final String JASPER_LICENSE_ERROR = "License expired or otherwise not valid.";
  private static final String JASPER_UNAUTHORIZED_ERROR = "Authorization has been refused for the credentials";
  private static final String JASPER_CREATE_SESSION_ERROR = "Could not create session with Jasper Web Server";
  private static final String JASPER_NOT_FOUND_ERROR = "The specified report URI is not found in the repository.";

  /**
   * Constructor.
   * 
   * @param restContextUrl
   * @throws MalformedURLException
   */
  public JasperConnection(String restContextUrl, String restUsername, String restPassword) throws MalformedURLException {
    logger.debug("<JasperConnection");

    this.restContextUrl = restContextUrl;

    this.loginUrl = new URL(this.restContextUrl + LOGIN_PATH);
    this.logoutUrl = new URL(this.restContextUrl + LOGOUT_PATH);
    this.reportsUrlString = this.restContextUrl + REPORTS_PATH;

    this.j_username = restUsername;
    this.j_password = "";
    for (int i = 0; i < restPassword.length(); ++i) {
      this.j_password = this.j_password + "*";
    }

    this.loginPostData = "j_username=" + restUsername + "&j_password=" + restPassword;

    logger.debug(">JasperConnection");
  }

  /**
   * Connect to the given url, using this connection's session.
   */
  public void fetchReport(String reportName, String reportFormat, String arguments, Path reportFilePath)
      throws ReportingException {
    logger.info("<fetchReport");
    
    if(cookieMap == null) {
      throw new SessionInitializationException(JASPER_CREATE_SESSION_ERROR);
    }

    HttpURLConnection conn = null;
    try {

      // http://<host>:<port>/jasperserver[-pro]/rest_v2/reports/path/to/report.<format>?<arguments>
      String reportUrlString = this.reportsUrlString + reportName + "." + reportFormat + (arguments == null ? "" : "?" + arguments);
      logger.info("Url=" + reportUrlString);
      URL reportUrl = new URL(reportUrlString);

      conn = (HttpURLConnection) reportUrl.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("GET");
      conn.setUseCaches(false);


      // Append all cookies set in the login response as semicolon-delimited
      // name-value pairs (not just the JSESSIONID cookie)

      StringBuffer buf = new StringBuffer();
      String delim = "";
      for (Cookie cookie : cookieMap.values()) {
        buf.append(delim);
        delim = "; ";
        buf.append(cookie.getName());
        buf.append("=");
        buf.append(cookie.getValue());
      }

      conn.setRequestProperty("Cookie", buf.toString());
      logger.info("Request_Cookie=" + buf.toString());

      int responseCode = conn.getResponseCode();
      logger.info(responseCode + ": " + conn.getResponseMessage());
      if (responseCode == 200) {

        copyToFile(conn, reportFilePath);

      } else if (responseCode == 302) { // Found
        logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_FETCH_REPORT + JASPER_LICENSE_ERROR);
      } else if (responseCode == 401) { // Unauthorized
        logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_FETCH_REPORT + JASPER_UNAUTHORIZED_ERROR);
      } else if (responseCode == 404) { // Not Found
        logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_FETCH_REPORT + JASPER_NOT_FOUND_ERROR);
      } else {
        logResponse(conn, true);
        String message = logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_LOGOUT + "Unexpected HTTP response: " + responseCode + " " + message);
      }

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new SessionInitializationException(ERROR_PREFIX_FETCH_REPORT + LoggingUtils.getStackTraceString(e));
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }

    logger.info(">fetchReport " + reportFilePath);
  }

  /**
   * Connect to the given url, using this connection's session.
   * 
   * @throws SessionInitializationException
   */
  public void jasperRestLogin() throws SessionInitializationException {
    logger.info("<jasperRestLogin");

    logger.debug("j_username=" + j_username);
    logger.debug("j_password=" + j_password);

    this.cookieMap = new HashMap<>();

    // Login to Jasper
    HttpURLConnection conn = null;
    try {

      logger.debug("URL=" + this.loginUrl);
      conn = (HttpURLConnection) this.loginUrl.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("charset", "utf-8");
      conn.setRequestProperty("Content-Length", Integer.toString(this.loginPostData.length()));
      conn.setUseCaches(false);

      DataOutputStream wr = null;
      try {
        wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(this.loginPostData);
        wr.flush();
        wr.close();
        wr = null;
      } finally {
        if (wr != null) {
          try {
            wr.close();
          } catch (IOException e) {
            // do nothing
          }
        }
      }

      int responseCode = conn.getResponseCode();
      logger.info(responseCode + ": " + conn.getResponseMessage());
      if (responseCode == 200) {

        // load balancing issue - keep load balancing cookie as well, if set,
        // not just JSESSIONID

        Cookie[] cookies = this.getCookies(conn);

        for (Cookie cookie : cookies) {
          String name = cookie.getName();
          this.cookieMap.put(name, cookie);
          this.cookieMap.put(name, cookie);
          if(LOADBALANCING_NAME.equals(name)) {
            logger.debug("Load Balancing Cookie: [" + cookie.toString() + "]");
          } else {
            logger.debug(name + " Cookie: [" + cookie.toString() + "]");
          }
        }

        if (!this.cookieMap.containsKey(JSESSIONID_NAME)) {
          throw new SessionInitializationException(ERROR_PREFIX_LOGIN + JASPER_CREATE_SESSION_ERROR);
        }

      } else if (responseCode == 302) {
        logResponse(conn, true);
        throw new SessionInitializationException(ERROR_PREFIX_LOGIN + JASPER_LICENSE_ERROR);
      } else if (responseCode == 401) {
        logResponse(conn, true);
        throw new SessionInitializationException(ERROR_PREFIX_LOGIN + JASPER_UNAUTHORIZED_ERROR);
      } else {
        String message = logResponse(conn, true);
        throw new SessionInitializationException(ERROR_PREFIX_LOGIN + "Unexpected HTTP response: " + responseCode + " " + message);
      }

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      throw new SessionInitializationException(ERROR_PREFIX_LOGIN + e.getMessage() + " " + LoggingUtils.getStackTraceString(e));
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }

    logger.info(">jasperRestLogin " + cookieMap);
  }

  /**
   * Connect to the given url, using this connection's session.
   */
  public void jasperRestLogout()
      throws IOException, ReportingException {
    logger.info("<jasperRestLogout");
    
    if(cookieMap == null || cookieMap.get(JSESSIONID_NAME) == null) {
      // Not logged in. Nothing to do.
      return;
    }

    HttpURLConnection conn = null;
    try {

      // http://<host>:<port>/jasperserver[-pro]/rest_v2/reports/path/to/report.<format>?<arguments>
      logger.info("Url=" + this.logoutUrl.toString());

      conn = (HttpURLConnection) this.logoutUrl.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setInstanceFollowRedirects(false);
      conn.setRequestMethod("GET");
      conn.setUseCaches(false);


      // Append all cookies set in the login response as semicolon-delimited
      // name-value pairs (not just the JSESSIONID cookie)

      StringBuffer buf = new StringBuffer();
      String delim = "";
      for (Cookie cookie : cookieMap.values()) {
        buf.append(delim);
        delim = "; ";
        buf.append(cookie.getName());
        buf.append("=");
        buf.append(cookie.getValue());
      }

      conn.setRequestProperty("Cookie", buf.toString());
      logger.info("Request_Cookie=" + buf.toString());

      int responseCode = conn.getResponseCode();
      logger.info(responseCode + ": " + conn.getResponseMessage());
      if (responseCode == 200) { // OK
        // logout successful
      } else if (responseCode == 302) { // Found
        // logout successful
        logResponse(conn, false);
      } else if (responseCode == 401) { // Unauthorized
        logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_LOGOUT + JASPER_UNAUTHORIZED_ERROR);
      } else {
        String message = logResponse(conn, true);
        throw new ServerRequestException(ERROR_PREFIX_LOGOUT + "Unexpected HTTP response: " + responseCode + " " + message);
      }

    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    
    cookieMap.clear();

    logger.info(">jasperRestLogout");
  }

  private String logResponse(HttpURLConnection conn, boolean isError) throws IOException {
    
    try(ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = conn.getInputStream();) {

      byte[] b = new byte[1024];
      int read = -1;
      while ((read = is.read(b)) != -1) {
        baos.write(b, 0, read);
      }
      is.close();
      String message = baos.toString();
      if(isError) {
        logger.error(message);
      } else {
        logger.info(message);
      }
      return message;
    }

  }

  /**
   * Copy the results of the given connection to the given file.
   * 
   * @param connection The JasperConnection to copy
   * @param filePath The file to copy to.
   */
  private void copyToFile(URLConnection connection, Path filePath) throws IOException {
    
    try (InputStream in = connection.getInputStream();
        OutputStream out = Files.newOutputStream(filePath, StandardOpenOption.TRUNCATE_EXISTING);) {
      byte[] buffer = new byte[256];
      while (true) {
        int bytesRead = in.read(buffer);
        if (bytesRead == -1) {
          break;
        }
        out.write(buffer, 0, bytesRead);
      }
      out.flush();
    }
  }

  /**
   * Retrieves cookies from the given url connection.
   * 
   * @param conn - a URLConnection
   * @return an array of cookie strings.
   */
  private Cookie[] getCookies(URLConnection conn) {
    logger.debug("called getcookies");
    List<Cookie> cookies = new ArrayList<>();
    String name = null;

    int n = 1;
    while (true) {
      name = conn.getHeaderFieldKey(n);
      if (name == null) {
        break;
      }
      if (name.equalsIgnoreCase("Set-Cookie")) {
        // we have a cookie
        try {
          cookies.add(Cookie.parseCookie(conn.getHeaderField(n)));
        } catch (CookieFormatException e) {
          logger.error(e.getMessage());
        }
      }
      n++;
    }
    return cookies.toArray(new Cookie[0]);
  }

}
