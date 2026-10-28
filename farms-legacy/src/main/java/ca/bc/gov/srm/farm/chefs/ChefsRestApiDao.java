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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private static final String HEADER_RETRY_AFTER = "Retry-After";
  private static final Pattern RATELIMIT_RESET_PATTERN = Pattern.compile("reset=(\\d+)");

  // On a 429, wait and retry a limited number of times. Waits longer than the max are not
  // attempted because callers include user requests and the ChefsAgent timer, which
  // does not schedule its next run until the current one finishes.
  private static final int MAX_RATE_LIMIT_RETRIES = 2;
  private static final int MAX_RATE_LIMIT_WAIT_SECONDS = 60;
  private static final int DEFAULT_RATE_LIMIT_WAIT_SECONDS = 15;

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
    // A 429 means the request was rejected without being processed, so a POST is safe to retry.
    for(int retry = 0; ; retry++) {
      try {
        return postSubmissionOnce(endpointUrl, resource, resourceClass);
      } catch(ServiceException e) {
        waitBeforeRetry(e, retry);
      }
    }
  }

  private <T extends ChefsResource> SubmissionParentResource<T> postSubmissionOnce(String endpointUrl,
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

      String responseContent;
      if(httpResponseCode == HTTP_STATUS_TOO_MANY_REQUEST || httpResponseCode != HttpURLConnection.HTTP_CREATED) {
        try {
          responseContent = readErrorResponse(conn);
        } catch (IOException e) {
          responseContent = "(unable to read response body: " + e.getMessage() + ")";
        }
      } else {
        responseContent = readResponseIfPresent(conn);
      }

      if(httpResponseCode == HTTP_STATUS_TOO_MANY_REQUEST) {
        throw newTooManyRequestsException(conn, responseContent);
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
    for(int retry = 0; ; retry++) {
      try {
        return getOnce(endpointUrl);
      } catch(ServiceException e) {
        waitBeforeRetry(e, retry);
      }
    }
  }

  private String getOnce(String endpointUrl) throws ServiceException {
    logger.debug(String.format("GET request to %s", endpointUrl));
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_GET);
    
    String response;
    
    try {

      int httpResponseCode = conn.getResponseCode();

      if(httpResponseCode == HttpURLConnection.HTTP_OK) {
        response = readResponseIfPresent(conn);

      } else if(httpResponseCode == HttpURLConnection.HTTP_NOT_FOUND) {
        response = null;

      } else {
        String errorResponse;
        try {
          errorResponse = readErrorResponse(conn);
        } catch (IOException e) {
          errorResponse = "(unable to read response body: " + e.getMessage() + ")";
        }

        if(httpResponseCode == HTTP_STATUS_TOO_MANY_REQUEST) {
          throw newTooManyRequestsException(conn, errorResponse);
        }

        throw new IOException("Error getting resource. Expected 200 - OK. Actual HTTP code: " +
            httpResponseCode + " - " + conn.getResponseMessage() +
            ". Response Body: " + errorResponse);
      }
      // else HTTP_NOT_FOUND: response stays null

      logRateLimit(conn);
    } catch(IOException e) {
      logger.error("IOException getting resource: ", e);
      logger.error("Response headers: " + conn.getHeaderFields());
      throw new ServiceException("Error getting resource", e);
    }
    return response;
  }

  private ServiceException newTooManyRequestsException(HttpURLConnection conn, String responseContent) throws IOException {
    Integer retryAfterSeconds = getRetryAfterSeconds(conn);
    return new ServiceException("Too many request to CHEFS. httpResponseCode: " +
        HTTP_STATUS_TOO_MANY_REQUEST + " Message: " + conn.getResponseMessage() +
        " Retry-After: " + retryAfterSeconds + " Response Body: " + responseContent,
        new TooManyRequestsException("Too many request to CHEF", retryAfterSeconds));
  }

  /**
   * @return seconds to wait from the Retry-After header, falling back to the reset value
   *         of the RateLimit header (e.g. "limit=120, remaining=0, reset=42"), or null if neither is usable.
   */
  private Integer getRetryAfterSeconds(HttpURLConnection conn) {
    String retryAfter = conn.getHeaderField(HEADER_RETRY_AFTER);
    if(retryAfter != null) {
      try {
        return Integer.valueOf(retryAfter.trim());
      } catch(NumberFormatException e) {
        // Could be an HTTP-date. Fall through to the RateLimit header.
      }
    }

    String rateLimit = conn.getHeaderField(HEADER_RATELIMIT);
    if(rateLimit != null) {
      Matcher matcher = RATELIMIT_RESET_PATTERN.matcher(rateLimit);
      if(matcher.find()) {
        return Integer.valueOf(matcher.group(1));
      }
    }

    logger.warn("Unable to determine retry delay. Headers: " + conn.getHeaderFields());
    return null;
  }

  /**
   * Waits out a CHEFS rate limit so the request can be retried.
   * Rethrows e if it is not a rate limit error, or if it is not worth retrying.
   */
  private void waitBeforeRetry(ServiceException e, int retry) throws ServiceException {
    TooManyRequestsException tooManyRequests = TooManyRequestsException.findIn(e);
    if(tooManyRequests == null || retry >= MAX_RATE_LIMIT_RETRIES) {
      throw e;
    }

    Integer retryAfterSeconds = tooManyRequests.getRetryAfterSeconds();
    int waitSeconds = retryAfterSeconds == null ? DEFAULT_RATE_LIMIT_WAIT_SECONDS : Math.max(retryAfterSeconds, 1);
    if(waitSeconds > MAX_RATE_LIMIT_WAIT_SECONDS) {
      logger.warn(String.format("CHEFS rate limit reached. Retry-After of %d seconds exceeds the maximum wait of %d. Not retrying.",
          waitSeconds, MAX_RATE_LIMIT_WAIT_SECONDS));
      throw e;
    }

    logger.warn(String.format("CHEFS rate limit reached. Waiting %d seconds before retry %d of %d.",
        waitSeconds, retry + 1, MAX_RATE_LIMIT_RETRIES));
    try {
      Thread.sleep(waitSeconds * 1000L);
    } catch(InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw e;
    }
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
