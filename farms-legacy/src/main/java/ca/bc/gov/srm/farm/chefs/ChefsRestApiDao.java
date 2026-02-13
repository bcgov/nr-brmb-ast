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
package ca.bc.gov.srm.farm.chefs;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionRequestDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionRequestDataResource;
import ca.bc.gov.srm.farm.chefs.resource.preflight.PreflightWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionRequestDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.rest.RestApiDao;
import ca.bc.gov.srm.farm.rest.RestAuthenticationHandler;
import ca.bc.gov.srm.farm.rest.RestResource;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * @author awilkinson
 */
public class ChefsRestApiDao extends RestApiDao {

  private final Logger logger = LoggerFactory.getLogger(ChefsRestApiDao.class);

  private static final int HTTP_STATUS_TOO_MANY_REQUEST = 429;

  public ChefsRestApiDao(RestAuthenticationHandler authenticationHandler) {
    super(authenticationHandler);

  }

  public <T extends ChefsResource> SubmissionWrapperResource<T> getSubmissionWrapperResource(String endpointUrl,
      Class<T> clazz) throws ServiceException {

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionWrapperResource.class,
        clazz);
    return getResource(endpointUrl, parametricType);
  }

  public <T extends RestResource> List<T> getResourceList(String endpointUrl, Class<T> clazz) throws ServiceException {

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(List.class, clazz);
    return getResource(endpointUrl, parametricType);
  }

  public <T extends ChefsResource> SubmissionParentResource<T> postSubmission(String endpointUrl,
      Object resource, Class<T> resourceClass) throws ServiceException {

    logMethodStart(logger);

    SubmissionParentResource<T> result = null;
    try {

      ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
      String content = jsonObjectMapper.writeValueAsString(resource);

      JsonUtils.logObjectAsJsonAtDebug(logger, resource, resource.getClass().getName());

      URL url = new URL(endpointUrl);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.addRequestProperty("Content-Type", "application/json; charset=utf-8");
      conn.setRequestProperty("Content-Length", String.valueOf(content.length()));
      conn.setRequestMethod(HTTP_METHOD_POST);

      authenticationHandler.handleAuthentication(conn);

      try (OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream())) {
        wr.write(content);
        wr.flush();
      }

      int httpResponseCode = conn.getResponseCode();

      String responseContent = readResponse(conn);

      if(httpResponseCode == HTTP_STATUS_TOO_MANY_REQUEST) {
        throw new ServiceException("Too many request to CHEFS. httpResponseCode: " +
            httpResponseCode + " Message: " + conn.getResponseMessage() +
            " Response Body: " + responseContent, new TooManyRequestsException("Too many request to CHEF"));
      } else if(httpResponseCode != HttpURLConnection.HTTP_CREATED) {
        String formattedJson = getFormattedJson(resource);
        logger.error("Error posting JSON:\n" + formattedJson);
        
        throw new ServiceException("Error posting to CHEFS. Expected 201 - Created. Actual HTTP code: "
            + httpResponseCode + " - " + conn.getResponseMessage() + ". Response Body: " + responseContent);
      }

      JavaType parametricType;
      try {
        parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionParentResource.class,
            resourceClass);
      } catch (Exception e) {
        throw new ServiceException(e);
      }
      jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
      result = jsonObjectMapper.readValue(responseContent, parametricType);
      JsonUtils.logObjectAsJsonAtDebug(logger, resource, resource.getClass().getName());

    } catch (IOException e) {
      logger.error("Error posting to CHEFS submission: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return result;
  }

  public SubmissionParentResource<NppSubmissionDataResource> postNppSubmission(String endpointUrl,
      NppSubmissionRequestDataResource<NppSubmissionDataResource> resource) throws ServiceException {
    return postSubmission(endpointUrl, resource, NppSubmissionDataResource.class);
  }

  public SubmissionParentResource<InterimSubmissionDataResource> postInterimSubmission(String endpointUrl,
      InterimSubmissionRequestDataResource<InterimSubmissionDataResource> resource) throws ServiceException {
    return postSubmission(endpointUrl, resource, InterimSubmissionDataResource.class);
  }

  public SubmissionParentResource<StatementASubmissionDataResource> postStatementASubmission(String endpointUrl,
      StatementASubmissionRequestDataResource<StatementASubmissionDataResource> resource) throws ServiceException {
    return postSubmission(endpointUrl, resource, StatementASubmissionDataResource.class);
  }

  public <T extends ChefsResource> PreflightWrapperResource<T> getSubmissionOptionsResource(String endpointUrl, Class<T> clazz) throws ServiceException {

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(PreflightWrapperResource.class,
        clazz);
    return getResource(endpointUrl, parametricType);
  }

  @Override
  protected <T> T getResource(String endpointUrl, JavaType type) throws ServiceException {
    logMethodStart(logger);
    
    String response = get(endpointUrl);
    
    logMethodEnd(logger);
    return parseResource(response, type);
  }

  public String get(String endpointUrl) throws ServiceException {
    logger.debug(String.format("GET request to %s", endpointUrl));
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_GET);
    
    String response = null;
    
    try {

      int httpResponseCode = conn.getResponseCode();  
      response = readResponse(conn);

      if(httpResponseCode == HTTP_STATUS_TOO_MANY_REQUEST) {
        throw new ServiceException("Too many request to CHEFS. httpResponseCode: " +
            httpResponseCode + " Message: " + conn.getResponseMessage() +
            " Response Body: " + response, new TooManyRequestsException("Too many request to CHEF"));
        
      } else if(httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
        // return null
        
      } else if(httpResponseCode != HttpURLConnection.HTTP_OK) {
        
        throw new IOException("Error getting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + response);
      }
      
      logRateLimit(conn);
    } catch(IOException e) {
      logger.error("getResource response: \n" + response);
      logger.error("IOException getting resource: ", e);
      logger.error("Response headers: " + conn.getHeaderFields());
      throw new ServiceException("Error getting resource", e);
    }
    return response;
  }

  public <T> T parseResource(String response, JavaType type) throws ServiceException {
    try {
      logMethodStart(logger);
      
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
      throw new ServiceException("Error getting resource", e);
    }
  }

  public void deleteSubmission(String submissionGuid) throws ServiceException {
    ChefsConfigurationUtil chefsConfig = ChefsConfigurationUtil.getInstance();
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
    delete(submissionUrl);
  }

}
