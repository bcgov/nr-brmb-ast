/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.rest;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;

import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * @author awilkinson
 */
public class RestApiDao {
  
  private static Logger logger = LoggerFactory.getLogger(RestApiDao.class);
  
  protected static final String HTTP_METHOD_DELETE = "DELETE";
  protected static final String HTTP_METHOD_GET = "GET";
  protected static final String HTTP_METHOD_PUT = "PUT";
  protected static final String HTTP_METHOD_POST = "POST";
  protected static final String HTTP_METHOD_PATCH = "PATCH";
  
  protected static final String HEADER_RATELIMIT = "ratelimit";

  protected static final int CONNECT_TIMEOUT_MILLIS = 30_000;
  protected static final int READ_TIMEOUT_MILLIS = 300_000;

  protected RestAuthenticationHandler authenticationHandler;
  
  public RestApiDao(RestAuthenticationHandler authenticationHandler) {
    this.authenticationHandler = authenticationHandler;
  }


  public void post(RestResource resource, String endpointUrl) throws ServiceException {
    post(resource, endpointUrl, null);
  }
  
  public <T> T post(T resource, String endpointUrl, String resourceUrlHeader) throws ServiceException {
    return send(resource, endpointUrl, resourceUrlHeader, HTTP_METHOD_POST);
  }

  public void patch(RestResource resource, String endpointUrl) throws ServiceException {
    patch(resource, endpointUrl, null);
  }
  
  public <T> T patch(T resource, String endpointUrl, String resourceUrlHeader) throws ServiceException {
    return send(resource, endpointUrl, resourceUrlHeader, HTTP_METHOD_PATCH);
  }
  
  public void put(RestResource resource, String endpointUrl) throws ServiceException {
    put(resource, endpointUrl, null);
  }
  
  public <T> T put(T resource, String endpointUrl, String resourceUrlHeader) throws ServiceException {
    return send(resource, endpointUrl, resourceUrlHeader, HTTP_METHOD_PUT);
  }
  
  
  private <T> T send(T resource, String endpointUrl, String resourceUrlHeader, String method) throws ServiceException {
    logMethodStart(logger);

    logger.debug(String.format("%s request to %s", method, endpointUrl));

    T result = null;

    try {

      ObjectMapper jsonObjectMapper = new ObjectMapper();
      String content = jsonObjectMapper.writeValueAsString(resource);

      JsonUtils.logObjectAsJsonAtDebug(logger, resource, resource.getClass().getName());

      URL url = new URL(endpointUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
      conn.setReadTimeout(READ_TIMEOUT_MILLIS);
      conn.setDoOutput(true);
      conn.addRequestProperty("Content-Type", "application/json; charset=utf-8");
      conn.setRequestProperty("Content-Length", String.valueOf(content.length()));

      conn.setRequestMethod(method);

      authenticationHandler.handleAuthentication(conn);

      try(OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());) {
        wr.write(content);
        wr.flush();
      }

      int httpResponseCode = conn.getResponseCode();
      logRateLimit(conn);

      boolean isError = httpResponseCode >= 400;
      String responseContent;
      if (isError) {
        try {
          responseContent = readErrorResponse(conn);
        } catch (IOException e) {
          responseContent = "(unable to read response body: " + e.getMessage() + ")";
        }
      } else {
        responseContent = readResponseIfPresent(conn);
      }

      if(isError) {
        String formattedJson = getFormattedJson(resource);
        logger.error("Error posting JSON:\n" + formattedJson);
        logger.error("Error response body:\n" + responseContent);

        throw new ServiceException("Error posting update to CRM. Expected 204 - No Content. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + responseContent);
      }

      if(httpResponseCode != HttpURLConnection.HTTP_NO_CONTENT) {
        // Success, but not the expected 204 - worth knowing about even though we proceed.
        logger.warn("Expected 204 - No Content but got " + httpResponseCode +
            ". Response Body: " + responseContent);
      }

      if(resourceUrlHeader != null) {
        String resourceUrl = conn.getHeaderField(resourceUrlHeader);

        JavaType parametricType = jsonObjectMapper.getTypeFactory().constructType(resource.getClass());
        result = getResource(resourceUrl, parametricType);
      }

    } catch(IOException e) {
      logger.error("Error posting to CRM: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return result;
  }

  
  protected <T> String getFormattedJson(T resource) throws JsonProcessingException {
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    String formattedJson = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resource);
    return formattedJson;
  }

  
  protected String readErrorResponse(HttpURLConnection conn) throws IOException {
    java.io.InputStream errorStream = conn.getErrorStream();
    if (errorStream == null) {
      return "";
    }
    try (BufferedReader in = new BufferedReader(new InputStreamReader(errorStream))) {
      return readAll(in);
    }
  }

  
  protected String readResponseIfPresent(HttpURLConnection conn) throws IOException {
    // HTTP_NO_CONTENT (204) has no body by spec; other 2xx codes might.
    if (conn.getContentLength() == 0 || conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
      return "";
    }
    try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      return readAll(in);
    }
  }

  
  private String readAll(BufferedReader in) throws IOException {
    StringBuilder response = new StringBuilder();
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    return response.toString();
  }


  protected <T> T getResource(String endpointUrl, JavaType type) throws ServiceException {
    logMethodStart(logger);
    
    logger.debug(String.format("GET request to %s", endpointUrl));
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_GET);
    
    try {

      int httpResponseCode = conn.getResponseCode();
      boolean isError = httpResponseCode >= 400;

      if(isError) {
        if(httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
          logger.warn("Resource not found: " + endpointUrl);
          return null;
        }

        String errorResponse;
        try {
          errorResponse = readErrorResponse(conn);
        } catch (IOException e) {
          errorResponse = "(unable to read response body: " + e.getMessage() + ")";
        }
        throw new IOException("Error getting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + errorResponse);
      }

      String response = readResponseIfPresent(conn);

      if(httpResponseCode != HttpURLConnection.HTTP_OK) {
        // Success, but not the expected 200 - worth knowing about even though we proceed.
        logger.debug("Expected 200 - OK but got " + httpResponseCode +
            ". Response Body: " + response);
      }
      
      logRateLimit(conn);
      
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
      // Fix for LabelValue being empty string
      jsonObjectMapper.coercionConfigFor(LabelValue.class).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
      
      T resource = jsonObjectMapper.readValue(response, type);
      
      JsonUtils.logObjectAsJsonAtDebug(logger, resource, resource.getClass().getName());
      
      logMethodEnd(logger);
      
      return resource;
      
    } catch(IOException e) {
      logger.error("IOException getting resource: ", e);
      logger.error("Response headers: " + conn.getHeaderFields());
      throw new ServiceException("Error getting resource", e);
    }
  }

  
	protected HttpURLConnection getHttpURLConnection(String endpointUrl, String method) throws ServiceException {

		HttpURLConnection conn;
		try {
			URL url = new URL(endpointUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
			conn.setReadTimeout(READ_TIMEOUT_MILLIS);
			conn.setDoOutput(true);
			conn.addRequestProperty("Accept", "application/json; charset=utf-8");
			conn.setRequestMethod(method);

			authenticationHandler.handleAuthentication(conn);
		} catch (IOException e) {
		  logger.error("IOException getting HttpURLConnection: ", e);
			throw new ServiceException("Error getting HttpURLConnection resource", e);
		}
		return conn;
	}

	public void delete(String endpointUrl) throws ServiceException {
    logMethodStart(logger);
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_DELETE);
    
    try {
    
      int httpResponseCode = conn.getResponseCode();
      logRateLimit(conn);

      boolean isError = httpResponseCode >= 400;

      if(isError) {
        String errorResponse;
        try {
          errorResponse = readErrorResponse(conn);
        } catch (IOException e) {
          errorResponse = "(unable to read response body: " + e.getMessage() + ")";
        }

        throw new IOException("Error deleting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + errorResponse);
      }

      String response = readResponseIfPresent(conn);

      if(httpResponseCode != HttpURLConnection.HTTP_OK) {
        logger.warn("Expected 200 - OK but got " + httpResponseCode);
      }
      
      if(logger.isDebugEnabled()) {
        logger.debug("Response Body: " + response);
      }
      
    } catch(IOException e) {
      logger.error("IOException deleting resource: ", e);
      logger.error("Response headers: " + conn.getHeaderFields());
      throw new ServiceException("Error deleting resource", e);
    }
    
    logMethodEnd(logger);
  }
  protected void logRateLimit(HttpURLConnection conn) {
    logger.debug(String.format("Header %s: %s", HEADER_RATELIMIT, conn.getHeaderField(HEADER_RATELIMIT)));
  }

}
