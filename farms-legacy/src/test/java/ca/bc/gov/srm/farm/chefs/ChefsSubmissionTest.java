package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmListResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class ChefsSubmissionTest {

  private static Logger logger = LoggerFactory.getLogger(ChefsSubmissionTest.class);

  protected CrmConfigurationUtil crmConfig;
  protected ChefsConfigurationUtil chefsConfig;
  protected ChefsFormCredentials formCredentials;
  protected String submissionsUrl;

  protected Connection conn;
  protected ReadDAO readDAO;
  protected CrmRestApiDao crmDao;
  protected ChefsRestApiDao chefsApiDao;
  protected ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
  protected CalculatorDAO calculatorDao = new CalculatorDAO();
  
  protected final String formUserType = ChefsConstants.USER_TYPE_IDIR;
  protected String user = this.getClass().getSimpleName();

  protected final String USER_EMAIL = "ASTWORK@gov.bc.ca";
  protected final String DATE_PATTERN = "yyyy-MM-dd";
  protected final SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);


  @BeforeAll
  public void setUpChefsTest() throws Exception {
    logger.debug("setUpChefsTest ChefsTest");
    TestUtils.standardTestSetUp();
    crmDao = new CrmRestApiDao();

    System.setProperty("generate.cob.enabled", "N");

    chefsConfig = ChefsConfigurationUtil.getInstance();
    formCredentials = chefsConfig.getFormCredentials(getChefsFormType(), formUserType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
    submissionsUrl = chefsConfig.getSubmissionsUrl(formCredentials.getFormId());
    crmConfig = CrmConfigurationUtil.getInstance();
    
    conn = TestUtils.openConnection();
    readDAO = new ReadDAO(conn);
  }

  @AfterAll
  public void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }

  protected Map<String, SubmissionListItemResource> buildSubmissionItemResourceMap(String... submissionGuids) {
    Map<String, SubmissionListItemResource> itemResourceMap = new HashMap<>();

    for (String submissionGuid : submissionGuids) {
      SubmissionListItemResource itemResource = new SubmissionListItemResource();
      itemResource.setSubmissionGuid(submissionGuid);
      itemResource.setDeleted(false);
      itemResourceMap.put(submissionGuid, itemResource);
    }

    return itemResourceMap;
  }

  protected void deleteValidationErrorTasksBySubmissionId(String submissionGuid) {

    try {
      CrmListResource<CrmValidationErrorResource> list = null;
      list = crmDao.getValidationErrorListBySubmissionId(submissionGuid);
      if (list != null) {
        for (CrmValidationErrorResource v : list.getList()) {
          crmDao.deleteValidationErrorTask(v.getActivityId());
        }
      }
    } catch (ServiceException | IOException e) {
      e.printStackTrace();
    }
  }

  protected int getUnusedParticpantPin() {

    CalculatorService service = ServiceFactory.getCalculatorService();
    Integer participantPin = null;
    try {

      boolean pinExists = false;
      // Generate random pins until we find one that doesn't exist.
      // Check both FARM database and CRM.
      do {
        participantPin = generateRandomPin();
        pinExists = service.pinExists(participantPin) || crmDao.getAccountByPin(participantPin) != null;
      } while (pinExists);

    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Error checking if pin exists");
    }
    return participantPin;
  }

  private int generateRandomPin() {
    int participantPin;
    final int min = 99999999;
    final int max = 999999999;
    participantPin = ThreadLocalRandom.current().nextInt(min, max + 1);
    return participantPin;
  }

  protected CrmTaskResource getValidationErrorBySubmissionId(String submissionGuid) throws ServiceException {
    return crmDao.getValidationErrorBySubmissionId(submissionGuid);
  }

  protected CrmTaskResource completeAndGetTask(String validationErrorUrl, String activityId) throws ServiceException {
    return crmDao.completeAndGetTask(validationErrorUrl, activityId);
  }

  protected CrmTaskResource completeAndGetValidationErrorTask(CrmTaskResource validationTask) {
    CrmTaskResource result = null;
    try {
      result = completeAndGetTask(crmConfig.getValidationErrorUrl(), validationTask.getActivityId());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected Exception");
    }
    return result;
  }

  protected List<ScenarioMetaData> getProgramYearMetadata(Integer participantPin, Integer programYear) {
    List<ScenarioMetaData> programYearMetadata = null;
    try {
      programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
    } catch (SQLException e) {
      e.printStackTrace();
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Unexpected Exception");
      }
      fail("Unexpected Exception");
    }
    return programYearMetadata;
  }

  protected void deleteSubmissions(List<String> submissionGuidList) {
    // Delete the submissions if they exist, from a previously failed test run.
    try {
      chefsDatabaseDao.deleteSubmissions(conn, submissionGuidList);
      conn.commit();
    } catch (DataAccessException | SQLException e) {
      e.printStackTrace();
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Unexpected Exception");
      }
      fail("Unexpected Exception");
    }
  }

  protected void deleteSubmission(String submissionGuid) {
    deleteSubmissions(Collections.singletonList(submissionGuid));
  }

  protected void deleteUserScenarios(String submissionGuid, List<ScenarioMetaData> programYearMetadata) {
    List<ScenarioMetaData> scenariosLinkedToSubmission;
    scenariosLinkedToSubmission = ScenarioUtils.findScenariosByChefSubmissionGuid(programYearMetadata, submissionGuid);
    for (ScenarioMetaData scenarioMetadata : scenariosLinkedToSubmission) {
      Integer linkedScenario = scenarioMetadata.getScenarioId();
      assertNotNull(linkedScenario);

      if (scenarioMetadata.getScenarioTypeCode().equals(ScenarioTypeCodes.USER)) {
        try {
          calculatorDao.deleteUserScenario(conn, linkedScenario);
          conn.commit();
        } catch (DataAccessException | SQLException e) {
          e.printStackTrace();
          try {
            conn.rollback();
          } catch (SQLException e1) {
            e1.printStackTrace();
            fail("Unexpected Exception");
          }
          fail("Unexpected Exception");
        }
      } else {
        try {
          chefsDatabaseDao.updateScenarioSubmissionId(conn, linkedScenario, null, user);
          conn.commit();
        } catch (DataAccessException | SQLException e) {
          e.printStackTrace();
          try {
            conn.rollback();
          } catch (SQLException e1) {
            e1.printStackTrace();
            fail("Unexpected Exception");
          }
          fail("Unexpected Exception");
        }
      }
    }
  }

  protected void deleteUserScenario(int scenarioId) {

    try {
      calculatorDao.deleteUserScenario(conn, scenarioId);
      conn.commit();
    } catch (DataAccessException | SQLException e) {
      e.printStackTrace();
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
        fail("Unexpected Exception");
      }
      fail("Unexpected Exception");
    }
  }
  
  protected abstract String getChefsFormType();

}
