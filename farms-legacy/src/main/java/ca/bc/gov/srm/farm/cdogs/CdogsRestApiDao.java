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
package ca.bc.gov.srm.farm.cdogs;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.rest.RestApiDao;

public class CdogsRestApiDao extends RestApiDao {

  private final Logger logger = LoggerFactory.getLogger(CdogsRestApiDao.class);

  private static final int BUFFER_SIZE = 4096;

  private static final CdogsConfigurationUtil cdogsConfig = CdogsConfigurationUtil.getInstance();

  public CdogsRestApiDao() {
    super(new CdogsAuthenticationHandler());
  }

  static {
    allowMethods("PATCH");
  }

  private static void allowMethods(String... methods) {
    try {
      Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

      methodsField.setAccessible(true);

      String[] oldMethods = (String[]) methodsField.get(null);
      Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
      methodsSet.addAll(Arrays.asList(methods));
      String[] newMethods = methodsSet.toArray(new String[0]);

      methodsField.set(null/* static field */, newMethods);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  public String getHealthCheck() throws IOException, ServiceException {
    logMethodStart(logger);

    HttpURLConnection conn = getHttpURLConnection(cdogsConfig.getHealthCheckUrl(), HTTP_METHOD_GET);

    String response = parseResponse(conn);

    logMethodEnd(logger);
    return response;
  }
  
  public String checkTemplateCache(String templateGuid) throws IOException, ServiceException {
    logMethodStart(logger);

    HttpURLConnection conn = getHttpURLConnection(cdogsConfig.getExistingTemplateCacheUrl(templateGuid), HTTP_METHOD_GET);

    String response = parseResponse(conn);

    logMethodEnd(logger);
    return response;
  }

  public String getFileTypes() throws IOException, ServiceException {
    logMethodStart(logger);

    HttpURLConnection conn = getHttpURLConnection(cdogsConfig.getFileTypeUrl(), HTTP_METHOD_GET);

    String response = parseResponse(conn);

    logMethodEnd(logger);
    return response;
  }

  public String generatePdfFromTemplateGuid(String templateGuid, String saveFilePath, String jsonInputString)
      throws IOException, ServiceException {
    logMethodStart(logger);

    HttpURLConnection conn = postHttpURLConnection(cdogsConfig.getExistingTemplateUrl(templateGuid),
        jsonInputString);
  
    try {
      @SuppressWarnings("resource")
      InputStream inputStream = conn.getInputStream();
      
      // opens an output stream to save into file
      try(FileOutputStream outputStream = new FileOutputStream(saveFilePath); ) {

        int bytesRead;
        byte[] buffer = new byte[BUFFER_SIZE];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
          outputStream.write(buffer, 0, bytesRead);
        }
      }
    } catch (IOException e) {
      logger.error("IOException: ", e);
      throw new ServiceException("Error getting CDOGS InputStream", e);
    }

    String response = parseResponse(conn);

    logMethodEnd(logger);
    return response;
  }

  public InputStream generateInputStreamFromTemplateGuid(String templateGuid, String jsonInputString)
      throws ServiceException {

    HttpURLConnection conn = postHttpURLConnection(cdogsConfig.getExistingTemplateUrl(templateGuid),
        jsonInputString);
    InputStream inputStream = null;

    try {
      inputStream = conn.getInputStream();
    } catch (IOException e) {
      logger.error("IOException: ", e);
      throw new ServiceException("Error getting CDOGS InputStream", e);
    }
    return inputStream;
  }

  private HttpURLConnection postHttpURLConnection(String endpointUrl, String jsonInputString)
      throws ServiceException {

    HttpURLConnection conn;
    try {
      URL url = new URL(endpointUrl);
      conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.addRequestProperty("Accept", "application/json;");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestMethod(HTTP_METHOD_POST);
      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setRequestProperty("Cache-Control", "no-cache");

      authenticationHandler.handleAuthentication(conn);

      try(OutputStream os = conn.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);) {
        
        osw.write(jsonInputString);
        osw.flush();
      }
      
    } catch (IOException e) {
      logger.error("IOException: ", e);
      throw new ServiceException("Error getting CDOGS document", e);
    }
    return conn;
  }

  private String parseResponse(HttpURLConnection conn) throws IOException {
    int httpResponseCode = conn.getResponseCode();
    String response = readResponse(conn);

    if (httpResponseCode != HttpURLConnection.HTTP_OK) {

      throw new IOException("Error getting CDOGS resource. Expected 200 - OK. Actual HTTP code: " + httpResponseCode
          + " - " + conn.getResponseMessage() + ". Response Body: " + response);
    }
    return response;
  }

}
