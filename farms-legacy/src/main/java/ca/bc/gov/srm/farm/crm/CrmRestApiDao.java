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
package ca.bc.gov.srm.farm.crm;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.crm.resource.CrmAccountAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitUpdateResource;
import ca.bc.gov.srm.farm.crm.resource.CrmCoverageTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentUpdateResource;
import ca.bc.gov.srm.farm.crm.resource.CrmListResource;
import ca.bc.gov.srm.farm.crm.resource.CrmNolTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmNppTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueItemResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmResource;
import ca.bc.gov.srm.farm.crm.resource.CrmRouteToQueueItemResource;
import ca.bc.gov.srm.farm.crm.resource.CrmRouteToQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmRouteToTargetResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.crm.transform.AccountTransformer;
import ca.bc.gov.srm.farm.crm.transform.AccountUpdateTransformer;
import ca.bc.gov.srm.farm.crm.transform.BenefitUpdateTransformer;
import ca.bc.gov.srm.farm.crm.transform.EnrolmentUpdateTransformer;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.rest.RestApiDao;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.IOUtils;

/**
 * @author awilkinson
 */
public class CrmRestApiDao extends RestApiDao {
  
  private final Logger logger = LoggerFactory.getLogger(CrmRestApiDao.class);
  
  private CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
  
  private CdogsService cdogsService = ServiceFactory.getCdogsService();
  
  private AccountTransformer accountTransformer = new AccountTransformer();
  private AccountUpdateTransformer accountUpdateTransformer = new AccountUpdateTransformer();
  private EnrolmentUpdateTransformer enrolmentTransformer = new EnrolmentUpdateTransformer();
  private BenefitUpdateTransformer benefitTransformer = new BenefitUpdateTransformer();
  
  public CrmRestApiDao() {
    super(new CrmAuthenticationHandler());
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

          methodsField.set(null/*static field*/, newMethods);
      } catch (NoSuchFieldException | IllegalAccessException e) {
          throw new IllegalStateException(e);
      }
  }

  public <T extends CrmResource> T getResourceById(String endpointUrl, String resourceId, Class<T> clazz)
      throws ServiceException {
    logMethodStart(logger);
    
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructType(clazz);
    
    String urlWithId = getResourceIdUrl(endpointUrl, resourceId);
    T resource = getResource(urlWithId, parametricType);

    logMethodEnd(logger);
    return resource;
  }

  private String getResourceIdUrl(String endpointUrl, String resourceId) {
    return endpointUrl + "(" + resourceId + ")";
  }
  
  public <T extends CrmResource> T getFirstResource(String endpointUrl, Class<T> clazz)
      throws ServiceException {
    logMethodStart(logger);
    
    T singleResource = null;
    try {
      CrmListResource<T> listResource = getListResource(endpointUrl, clazz);
      
      if( ! listResource.getList().isEmpty() ) {
        singleResource = clazz.cast(listResource.getList().get(0));
      }
    
    } catch(IOException e) {
      logger.error("Error posting to CRM: ", e);
      throw new ServiceException(e);
    }
    
    logMethodEnd(logger);
    return singleResource;
  }


  public <T extends CrmResource> CrmListResource<T> getListResource(String endpointUrl, Class<T> clazz)
      throws IOException, ServiceException {
    logMethodStart(logger);
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_GET);
    
    int httpResponseCode = conn.getResponseCode();
    String response = readResponse(conn);
    
    if(httpResponseCode != HttpURLConnection.HTTP_OK) {
      
      throw new IOException("Error getting CRM resource. Expected 200 - OK. Actual HTTP code: " +
          httpResponseCode + " - " + conn.getResponseMessage() +
          ". Response Body: " + response);
    }
    
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    jsonObjectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    
    CrmListResource<T> listResource = jsonObjectMapper.readValue(response,
        jsonObjectMapper.getTypeFactory().constructParametricType(CrmListResource.class, clazz));
    
    logMethodEnd(logger);
    return listResource;
  }


  public CrmAccountResource getAccountByPin(int participantPin) throws ServiceException {
    
    String url = crmConfig.getAccountLookupUrl(participantPin);
    
    return getFirstResource(url, CrmAccountResource.class);
  }
  
  public CrmListResource<CrmBenefitResource> getBenefitsByAccountId(String accountId) throws ServiceException, IOException {
    
    String url = crmConfig.getBenefitLookupUrl(accountId);

    return getListResource(url, CrmBenefitResource.class);
  }


  public CrmAccountResource createAccount(Client client, FarmingYear farmingYear, String submissionGuid, String formUserType) throws ServiceException {
    
    CrmAccountResource account = accountTransformer.transformToCrmResource(client, farmingYear);

    account = super.post(account, crmConfig.getAccountUrl(), CrmConstants.HEADER_ENTITY_URL);

    if (submissionGuid != null && formUserType != null && account != null) {
      uploadNppFormToCrm(account, submissionGuid, formUserType);
    }

    return account;
  }

  public CrmAccountResource updateAccount(CrmAccountResource account) throws ServiceException {
    
    return super.patch(account, crmConfig.getAccountUpdateUrl(account.getAccountid()), CrmConstants.HEADER_ENTITY_URL);
  }

  public CrmTaskResource createAccountUpdateTask(Client client, CrmAccountResource accountResource, String submissionGuid) throws ServiceException {
    
    String accountId = accountResource.getAccountid();
    
    CrmTaskResource task = accountUpdateTransformer.transformToTask(client, accountId, submissionGuid);
    createTask(task);

    return task;
  }


  public CrmProgramYearResource getProgramYear(int year) throws ServiceException {
    
    String url = crmConfig.getProgramYearLookupUrl(year);
    
    return getFirstResource(url, CrmProgramYearResource.class);
  }
  

  public void createTask(CrmTaskResource task) throws ServiceException {
    post(task, crmConfig.getTaskUrl());
  }

  public CrmTaskResource getTask(String taskId) throws ServiceException {
    return getResourceById(crmConfig.getTaskUrl(), taskId, CrmTaskResource.class);
  }
  
  public CrmTaskResource getValidationErrorTask(String taskId) throws ServiceException {
    return getResourceById(crmConfig.getValidationErrorUrl(), taskId, CrmTaskResource.class);
  }
  
  public CrmTaskResource getNolTask(String taskId) throws ServiceException {
    return getResourceById(crmConfig.getNolTaskUrl(), taskId, CrmTaskResource.class);
  }
  
  public CrmTaskResource createAndGetTask(String subject, String description,
      CrmAccountResource accountResource, String queueId) throws ServiceException {
    
    String accountId = null;
    if(accountResource != null) {
      accountId = accountResource.getAccountid();
    }
    
    CrmTaskResource task = new CrmTaskResource();
    task.setAccountIdParameter(accountId);
    task.setSubject(subject);
    task.setDescription(description);
    
    CrmTaskResource createdTask = post(task, crmConfig.getTaskUrl(), CrmConstants.HEADER_ENTITY_URL);
    
    routeToQueue(queueId, createdTask.getActivityId());
    
    return createdTask;
  }
  
  public CrmCoverageTaskResource createValidCoverageTask(CrmCoverageTaskResource task, String queueId) throws ServiceException {

    CrmCoverageTaskResource createdTask = post(task, crmConfig.getCoverageTaskUrl(), CrmConstants.HEADER_ENTITY_URL);

    routeToQueue(queueId, createdTask.getActivityId());

    return createdTask;
  }
  
  public CrmNolTaskResource createValidNolTask(CrmNolTaskResource task, String queueId) throws ServiceException {

    CrmNolTaskResource createdTask = post(task, crmConfig.getNolTaskUrl(), CrmConstants.HEADER_ENTITY_URL);

    routeToQueue(queueId, createdTask.getActivityId());

    return createdTask;
  }

  public CrmNppTaskResource createValidNppTask(CrmNppTaskResource task, String queueId) throws ServiceException {

    CrmNppTaskResource createdTask = post(task, crmConfig.getNppTaskUrl(), CrmConstants.HEADER_ENTITY_URL);

    routeToQueue(queueId, createdTask.getActivityId());

    return createdTask;
  }
  
  public CrmValidationErrorResource createValidationErrorTask(CrmValidationErrorResource task, String queueId)
      throws ServiceException {

    CrmValidationErrorResource createdTask = post(task, crmConfig.getValidationErrorUrl(),
        CrmConstants.HEADER_ENTITY_URL);

    routeToQueue(queueId, createdTask.getActivityId());

    return createdTask;
  }

  public void completeTask(String endpointUrl, String taskId) throws ServiceException {
    completeTask(endpointUrl, taskId, false);
  }

  public CrmTaskResource completeAndGetTask(String endpointUrl, String taskId) throws ServiceException {
    return completeTask(endpointUrl, taskId, true);
  }


  public CrmTaskResource completeTask(String endpointUrl, String taskId, boolean getCreatedTask)
      throws ServiceException {

    CrmTaskResource task = new CrmTaskResource();
    task.setStateCode(CrmConstants.TASK_STATE_CODE_COMPLETED);
    task.setStatusCode(CrmConstants.STATUS_CODE_COMPLETED);

    String taskUrl = getResourceIdUrl(endpointUrl, taskId);

    String headerEntityUrl = null;
    if (getCreatedTask) {
      headerEntityUrl = CrmConstants.HEADER_ENTITY_URL;
    }

    return patch(task, taskUrl, headerEntityUrl);
  }


  public CrmBenefitUpdateResource createBenefitUpdate(String[] fields) throws ServiceException {
    logMethodStart(logger);
    
    CrmBenefitUpdateResource resource;
    try {
      resource = benefitTransformer.transformCsvToCrmResource(fields);
    } catch (ParseException e) {
      logger.error("Error transforming to CRM resource", e);
      throw new ServiceException(e);
    }

    CrmBenefitUpdateResource crmBenefitResource = post(resource, crmConfig.getBenefitUpdateUrl(), CrmConstants.HEADER_ENTITY_URL);
    logMethodEnd(logger, crmBenefitResource);
    return crmBenefitResource;
  }


  public CrmEnrolmentUpdateResource createEnrolmentUpdate(Enrolment e, Integer importVersionId, String user) throws ServiceException {
    CrmEnrolmentUpdateResource resource = enrolmentTransformer.transformToCrmResource(e, importVersionId, user);

    post(resource, crmConfig.getEnrolmentUpdateUrl());
    return resource;
  }


  public CrmEnrolmentResource getEnrolment(String programYearId, String accountId) throws ServiceException {
    
    String url = crmConfig.getEnrolmentLookupUrl(programYearId, accountId);
    
    return getFirstResource(url, CrmEnrolmentResource.class);
  }


  public CrmQueueResource getQueueByName(String queueName) throws ServiceException {
    
    String url = crmConfig.getQueueLookupUrl(queueName);
    
    return getFirstResource(url, CrmQueueResource.class);
  }
  
  public CrmQueueItemResource getQueueItemByTaskId(String taskId) throws ServiceException {
    
    String url = crmConfig.getQueueItemLookupUrl(taskId);
    
    return getFirstResource(url, CrmQueueItemResource.class);
  }

  public CrmValidationErrorResource getValidationErrorBySubmissionId(String submissionId) throws ServiceException {
    
    String url = crmConfig.getValidationErrorLookupUrl(submissionId);
    
    return getFirstResource(url, CrmValidationErrorResource.class);
  }
  
  public CrmListResource<CrmValidationErrorResource> getValidationErrorListBySubmissionId(String submissionId) throws ServiceException, IOException {
    
    String url = crmConfig.getValidationErrorLookupUrl(submissionId);
    
    return getListResource(url, CrmValidationErrorResource.class);
  }

  public <T> T uploadFileToNote(T accountEntity) throws ServiceException {
    return post(accountEntity, crmConfig.getAnnotationUrl(), CrmConstants.HEADER_ENTITY_URL);
  }

  public void uploadNppFormToCrm(CrmAccountResource account, String submissionGuid, String formUserType) throws ServiceException {

    Map<Integer,File> participantPinFileMap = null;
    participantPinFileMap = cdogsService.createCdogsNppFormDocument(submissionGuid, formUserType);
    Map.Entry<Integer,File> entry = participantPinFileMap.entrySet().iterator().next();
    File file = entry.getValue();
    
    CrmAccountAnnotationResource accountEntity = new CrmAccountAnnotationResource();
    accountEntity.setSubject("NPP Form");
    accountEntity.setNotetext("Upload CHEFS NPP Form to note.");
    accountEntity.setFilename(file.getName());
    accountEntity.setIsdocument(true);
    accountEntity.setEntityId(account.getAccountid());
    try {
      accountEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      logger.error("Error uploading CHEFS NPP form to CRM: ", e);
      throw new ServiceException(e);
    }

    try {
      uploadFileToNote(accountEntity);
    } catch (ServiceException e) {
      throw new ServiceException(e);
    }
  }
  
  public void deleteValidationErrorTask(String activityId) throws ServiceException {
    String endpointUrl = crmConfig.getValidationErrorUrl() + "(" + activityId + ")";
    delete(endpointUrl);
  }
  
  @Override
  public void delete(String endpointUrl) throws ServiceException {
    logMethodStart(logger);
    
    HttpURLConnection conn = getHttpURLConnection(endpointUrl, HTTP_METHOD_DELETE);
    
    try {
    
      int httpResponseCode = conn.getResponseCode();
      logRateLimit(conn);
      
      String response = readResponse(conn);
      
      if(httpResponseCode != HttpURLConnection.HTTP_NO_CONTENT) {
        
        throw new IOException("Error deleting resource. Expected 204 - OK. Actual HTTP code: " +
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
  
  private void routeToQueue(String queueId, String activityId) throws ServiceException {

    CrmQueueItemResource crmQueueItemResource = getQueueItemByTaskId(activityId);
    
    if (queueId != null) {
      CrmRouteToTargetResource target = new CrmRouteToTargetResource(queueId);
      CrmRouteToQueueItemResource queueItem = new CrmRouteToQueueItemResource(crmQueueItemResource.getQueueItemId());
      CrmRouteToQueueResource routeToQueueResource = new CrmRouteToQueueResource(target, queueItem);

      post(routeToQueueResource, crmConfig.getRouteToUrl());
    }
  }

}
