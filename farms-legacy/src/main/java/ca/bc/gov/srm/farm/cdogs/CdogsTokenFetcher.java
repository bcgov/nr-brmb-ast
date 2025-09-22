/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.cdogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.cdogs.resource.CdogsTokenResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.JsonUtils;

public final class CdogsTokenFetcher {

  private final Logger logger = LoggerFactory.getLogger(CdogsTokenFetcher.class);

  private static final CdogsTokenFetcher instance = new CdogsTokenFetcher();

  private String authority;
  private String clientId;
  private String secret;
  private String token;
  private Date createdDate = new Date();

  private boolean initialized = false;

  private CdogsTokenFetcher() {
    // private constructor
  }

  public static CdogsTokenFetcher getInstance() {
    return instance;
  }

  public String getToken() throws ServiceException {

    try {
      initialize();

      if (needNewToken()) {
        token = getAccessToken();
        createdDate = new Date();
      }

    } catch (Exception e) {
      logger.error("Error getting CDOGS token", e);
      throw new ServiceException("Failed to get CDOGS access token: ", e);
    }

    return token;
  }

  /**
   * If the token will expire in the next 5 minutes then get a new one just to be
   * safe. Tokens expire one hour after they are issued.
   */
  private boolean needNewToken() {

    if (token != null) {

      final long fiveMinute = 5 * 60 * 1000;
      long milliSecondsLeft = (new Date().getTime()) - createdDate.getTime();

      return milliSecondsLeft >= fiveMinute;
    }

    return true;
  }

  private String getAccessToken() throws ServiceException {

    logger.debug("Fetching new CDOGS token...");

    String grantType = "client_credentials";
    CdogsTokenResource resource;

    StringBuilder data = new StringBuilder();
    try {
      data.append("grant_type=").append(URLEncoder.encode(grantType, "UTF-8"));
      data.append("&client_id=").append(URLEncoder.encode(clientId, "UTF-8"));
      data.append("&client_secret=").append(URLEncoder.encode(secret, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new ServiceException("Error creating string", e);
    }

    URL url;
    try {
      url = new URL(authority);

      HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
      httpConn.setRequestMethod("POST");

      httpConn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
      httpConn.setRequestProperty("Accept", "application/json");

      httpConn.setDoOutput(true);

      try(OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());) {
        writer.write(data.toString());
        writer.flush();
      }
      httpConn.getOutputStream().close();

      int httpResponseCode = httpConn.getResponseCode();

      if (httpResponseCode != 200) {
        throw new ServiceException("httpResponseCode: " + httpResponseCode);
      }

      String response = readResponse(httpConn);

      ObjectMapper jsonObjectMapper = new ObjectMapper();
      jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

      resource = jsonObjectMapper.readValue(response, CdogsTokenResource.class);

      JsonUtils.logObjectAsJsonAtDebug(logger, resource, resource.getClass().getName());

    } catch (Exception e) {
      logger.error("Erorr getting CDOGS token: ", e);
      throw new ServiceException(e);

    }

    return resource.getAccessToken();
  }

  private String readResponse(HttpURLConnection conn) throws IOException {
    StringBuilder response;
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

      String inputLine;
      response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
    }
    return response.toString();
  }

  private void initialize() throws ServiceException {

    if (!initialized) {

      ConfigurationUtility cu = ConfigurationUtility.getInstance();

      authority = cu.getValue(ConfigurationKeys.CDOGS_AUTHORITY_URL);
      clientId = cu.getValue(ConfigurationKeys.CDOGS_CLIENT_ID);
      secret = cu.getValue(ConfigurationKeys.CDOGS_CLIENT_SECRET);

      token = getAccessToken();

      initialized = true;
    }
  }

}
