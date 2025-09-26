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
  
      String responseContent = readResponse(conn);
      
      if(httpResponseCode != HttpURLConnection.HTTP_NO_CONTENT) {
        String formattedJson = getFormattedJson(resource);
        logger.error("Error posting JSON:\n" + formattedJson);
        
        throw new ServiceException("Error posting update to CRM. Expected 204 - No Content. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
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

  protected String readResponse(HttpURLConnection conn) throws IOException {
    StringBuilder response;
    try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))){

      String inputLine;
      response = new StringBuilder();
      while (( inputLine = in.readLine()) != null) {
          response.append(inputLine);
      }
    }
    return response.toString();
  }


  protected <T> T getResource(String endpointUrl, JavaType type) throws ServiceException {
    logMethodStart(logger);
    
    logger.debug(String.format("GET request to %s", endpointUrl));
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_GET);
    
    try {

    	int httpResponseCode = conn.getResponseCode();  
    	String response = readResponse(conn);

      if(httpResponseCode != HttpURLConnection.HTTP_OK) {
        
        throw new IOException("Error getting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + response);
      }
      
      logRateLimit(conn);
      
      ObjectMapper jsonObjectMapper = new ObjectMapper();
      jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
      // Fix for LabelValue being empty string
      jsonObjectMapper.coercionConfigFor(LabelValue.class)
      .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
      
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
      
      String response = readResponse(conn);
      
      if(httpResponseCode != HttpURLConnection.HTTP_OK) {
        
        throw new IOException("Error deleting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + response);
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
