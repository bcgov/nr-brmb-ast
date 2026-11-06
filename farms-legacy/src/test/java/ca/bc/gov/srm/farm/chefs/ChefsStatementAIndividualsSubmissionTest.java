package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.StatementAIndividualsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAIndividualsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ServiceFactory;

/** Processor tests using the same Oracle/CORE DEV setup as the Corporate class.
 * Submissions are synthetic: these tests do not post forms to CHEFS or run the
 * shared import queue. They cancel only jobs created with this invocation's user. */
public class ChefsStatementAIndividualsSubmissionTest extends ChefsSubmissionTest {

  // Existing Individual DEV participant; use 2024 so its Supplemental/NOL 2023
  // calculation fixtures and their income/expense history remain unchanged.
  private static final int PARTICIPANT_PIN = 3778842;
  private static final int PROGRAM_YEAR = 2024;
  private static final String SIN = "999999999";
  private final ObjectMapper mapper = new ObjectMapper();
  private ConfigurationUtility originalConfiguration;
  private boolean bceidFormsEnabled = true;

  @BeforeAll
  public void configureBceidForThisTestProcess() throws Exception {
    // Override only this JVM's flag; never enable every BCeID form in shared DEV.
    originalConfiguration = ConfigurationUtility.getInstance();
    Field instance = ConfigurationUtility.class.getDeclaredField("instance");
    instance.setAccessible(true);
    instance.set(null, new ConfigurationUtility() {
      @Override public void initialize(Object resource) { }
      @Override public String getEnvironment() { return originalConfiguration.getEnvironment(); }
      @Override public String getValue(String key) { return originalConfiguration.getValue(key); }
      @Override public boolean getBoolean(String key) {
        return ConfigurationKeys.CHEFS_BASIC_BCEID_FORMS_ENABLED.equals(key)
            ? bceidFormsEnabled : originalConfiguration.getBoolean(key);
      }
    });
  }

  @AfterAll
  public void restoreConfiguration() throws Exception {
    if (originalConfiguration != null) {
      Field instance = ConfigurationUtility.class.getDeclaredField("instance");
      instance.setAccessible(true);
      instance.set(null, originalConfiguration);
    }
  }

  @Override
  protected String getChefsFormType() {
    return ChefsFormTypeCodes.STA_IND;
  }

  @Test
  public void individualHappyPath() throws Exception {
    assertHappyPath(ChefsConstants.USER_TYPE_IDIR);
  }

  @Test
  public void bceidHappyPath() throws Exception {
    assertHappyPath(ChefsConstants.USER_TYPE_BASIC_BCEID);
  }

  private void assertHappyPath(String identity) throws Exception {
    String guid = newTestGuid();
    IndividualsProcessor processor = newProcessor(guid, identity);
    try {
      SubmissionParentResource<? extends StatementASubmissionDataResource> metadata =
          processor.parse(submissionJson(guid, SIN, identity));
      StatementASubmissionDataResource data = metadata.getSubmission().getData();
      assertInstanceOf(StatementAIndividualsSubmissionDataResource.class, data);
      assertNull(data.getFarmType());
      assertNull(data.getCorporationName());
      assertNull(data.getBusinessTaxNumber());
      assertNull(data.getTrustNumber());
      assertTrue(processor.checkEnvironment(metadata));
      assertNull(processor.processSubmission(metadata), "Valid Individual must not create a validation task");
      if (ChefsConstants.USER_TYPE_BASIC_BCEID.equals(identity)) {
        assertEquals("external", data.getOrigin());
        assertEquals("chefsForm", data.getExternalMethod());
        assertEquals("DEV", data.getEnvironment());
      }

      ChefsSubmission record = chefsDatabaseDao.readSubmissionByGuid(conn, guid);
      assertNotNull(record);
      assertEquals(ChefsFormTypeCodes.STA_IND, record.getFormTypeCode());
      assertEquals(ChefsConstants.USER_TYPE_BASIC_BCEID.equals(identity) ? "Y" : "N", record.getBceidFormInd());
      assertEquals(ChefsSubmissionStatusCodes.PROCESSED, record.getSubmissionStatusCode());
      assertNull(record.getValidationTaskGuid());

      List<ScenarioMetaData> linked = new ArrayList<>();
      for (ScenarioMetaData scenario : getProgramYearMetadata(PARTICIPANT_PIN, PROGRAM_YEAR)) {
        if (record.getSubmissionId().equals(scenario.getChefsFormSubmissionId())
            && ScenarioTypeCodes.CHEF.equals(scenario.getScenarioTypeCode())) {
          linked.add(scenario);
        }
      }
      assertEquals(1, linked.size(), "Select this submission's scenario, not another Statement A scenario");
      Scenario scenario = ServiceFactory.getCalculatorService().loadScenario(
          PARTICIPANT_PIN, PROGRAM_YEAR, linked.get(0).getScenarioNumber());
      assertEquals(ScenarioCategoryCodes.CHEF_STA, scenario.getScenarioCategoryCode());
      assertEquals(ScenarioStateCodes.RECEIVED, scenario.getScenarioStateCode());
      assertEquals(guid, scenario.getChefsSubmissionGuid());
      assertNotNull(scenario.getLocalStatementAReceivedDate());
      assertNotNull(scenario.getLocalSupplementalReceivedDate());
      assertEquals(1, scenario.getFarmingYear().getFarmingOperations().size());
      assertTrue(scenario.getFarmingYear().getIsSoleProprietor());
      assertFalse(scenario.getFarmingYear().getIsCorporateShareholder());

      FarmingOperation operation = scenario.getFarmingYear().getFarmingOperations().get(0);
      assertEquals("2", operation.getAccountingCode());
      assertEquals("2024-01-01", formatter.format(operation.getFiscalYearStart()));
      assertEquals("2024-12-31", formatter.format(operation.getFiscalYearEnd()));
      assertEquals(1000.0, operation.getNetFarmIncome());
      assertEquals(1000.0, amount(operation, 92));
      assertEquals(200.0, amount(operation, 9662));
      assertEquals(10.0, buildProductiveUnitsMap(operation.getProductiveUnitCapacities()).get("5392"));
      assertEquals(1, operation.getCropItems().size());
      assertEquals(20.0, operation.getCropItems().get(0).getReportedQuantityProduced());
      assertNotNull(processor.getTriageImportVersionId(), "Successful import must queue triage");
    } finally {
      cleanUp(guid);
    }
  }

  @Test
  public void individualValidationErrors() throws Exception {
    assertValidationErrors(ChefsConstants.USER_TYPE_IDIR);
  }

  @Test
  public void bceidValidationErrors() throws Exception {
    assertValidationErrors(ChefsConstants.USER_TYPE_BASIC_BCEID);
  }

  private void assertValidationErrors(String identity) throws Exception {
    // Unlike Corporate, Individuals require a SIN without requiring farm type,
    // business number or trust number. Both missing and mismatched SINs matter.
    for (String sin : new String[] {null, "123456789"}) {
      String guid = newTestGuid();
      IndividualsProcessor processor = newProcessor(guid, identity);
      try {
        SubmissionParentResource<? extends StatementASubmissionDataResource> metadata =
            processor.parse(submissionJson(guid, sin, identity));
        assertTrue(processor.checkEnvironment(metadata));
        CrmTaskResource task = processor.processSubmission(metadata);
        assertNotNull(task);
        assertNotNull(task.getAccountId());
        assertEquals("2024 Statement A Individuals " + PARTICIPANT_PIN, task.getSubject());
        assertEquals(Integer.valueOf(CrmConstants.TASK_STATE_CODE_OPEN), task.getStateCode());
        String error = sin == null ? "Required field is blank: SIN Number"
            : "Field \"SIN Number\" with value \"123456789\" does not match BCFARMS: \"999999999\".";
        assertEquals(identity + " Statement A - Individuals form was submitted but has validation errors:\n\n"
            + "- " + error + "\n\nParticipant Name: SAMPLE ONLY - Alex Farmer\n"
            + "Telephone: (250) 555-5555\nEmail: sample@example.com\n", task.getDescription());
        ChefsSubmission record = chefsDatabaseDao.readSubmissionByGuid(conn, guid);
        assertNotNull(record);
        assertEquals(ChefsFormTypeCodes.STA_IND, record.getFormTypeCode());
        assertEquals(ChefsConstants.USER_TYPE_BASIC_BCEID.equals(identity) ? "Y" : "N", record.getBceidFormInd());
        assertEquals(ChefsSubmissionStatusCodes.INVALID, record.getSubmissionStatusCode());
        assertEquals(task.getActivityId(), record.getValidationTaskGuid());
        assertNull(processor.getTriageImportVersionId());
        for (ScenarioMetaData scenario : getProgramYearMetadata(PARTICIPANT_PIN, PROGRAM_YEAR)) {
          assertNotEquals(record.getSubmissionId(), scenario.getChefsFormSubmissionId(),
              "An invalid submission must not create a scenario");
        }
      } finally {
        cleanUp(guid);
      }
    }
  }

  @Test
  public void bceidRespectsDisabledFlag() throws Exception {
    String guid = newTestGuid();
    bceidFormsEnabled = false;
    try {
      IndividualsProcessor processor = newProcessor(guid, ChefsConstants.USER_TYPE_BASIC_BCEID);
      SubmissionParentResource<? extends StatementASubmissionDataResource> metadata =
          processor.parse(submissionJson(guid, SIN, ChefsConstants.USER_TYPE_BASIC_BCEID));
      assertFalse(processor.checkEnvironment(metadata));
      ChefsSubmission record = chefsDatabaseDao.readSubmissionByGuid(conn, guid);
      assertNotNull(record);
      assertEquals(ChefsFormTypeCodes.STA_IND, record.getFormTypeCode());
      assertEquals("Y", record.getBceidFormInd());
      assertEquals(ChefsSubmissionStatusCodes.OTHER_ENV, record.getSubmissionStatusCode());
      assertNull(record.getValidationTaskGuid());
      assertNull(processor.getTriageImportVersionId());
      for (ScenarioMetaData scenario : getProgramYearMetadata(PARTICIPANT_PIN, PROGRAM_YEAR)) {
        assertNotEquals(record.getSubmissionId(), scenario.getChefsFormSubmissionId());
      }
    } finally {
      bceidFormsEnabled = true;
      cleanUp(guid);
    }
  }

  private String newTestGuid() {
    String guid = UUID.randomUUID().toString();
    // Audit columns are VARCHAR2(30); the full test class name is longer.
    user = "STA_IND_TEST_" + guid.substring(0, 8);
    return guid;
  }

  private IndividualsProcessor newProcessor(String guid, String identity) throws Exception {
    Client client = readDAO.readClient(PARTICIPANT_PIN);
    assertNotNull(client, "Expected existing Individual DEV test participant");
    assertEquals(SIN, client.getSin(), "Individual fixture SIN changed; restore the fixture before running");
    assertNotNull(crmDao.getAccountByPin(PARTICIPANT_PIN), "Expected existing Individual CORE DEV account");
    IndividualsProcessor processor = new IndividualsProcessor(identity);
    processor.setUser(user);
    processor.setItemResourceMap(buildSubmissionItemResourceMap(guid));
    processor.loadSubmissionsFromDatabase();
    return processor;
  }

  private String submissionJson(String guid, String sin, String identity) throws Exception {
    ObjectNode data;
    try (InputStream input = getClass().getResourceAsStream("/chefs/statement-a-individuals-test-1.json")) {
      assertNotNull(input);
      data = (ObjectNode) mapper.readTree(input);
    }
    data.put("agriStabilityAgriInvestPin", PARTICIPANT_PIN);
    if (sin == null) data.putNull("sinNumber"); else data.put("sinNumber", sin);
    data.put("fiscalYearStart", "2024-01-01T00:00:00-08:00");
    data.put("fiscalYearEnd", "2024-12-31T00:00:00-08:00");
    data.put("origin", "internal");
    data.put("internalMethod", "chefsForm");
    data.put("environment", "DEV");
    if (ChefsConstants.USER_TYPE_BASIC_BCEID.equals(identity)) {
      // Public users cannot fill staff-only fields; the processor supplies them.
      data.remove("origin");
      data.remove("internalMethod");
      data.remove("externalMethod");
      data.remove("environment");
    }
    data.put("province", "BC");
    data.put("provinceTerritoryOfMainFarmstead", "BC");
    data.put("telephone", "(250) 555-5555");
    data.put("email", "sample@example.com");
    data.put("copyOfCOB", "no");
    data.put("was2024FinalYearOfFarming", "no");
    data.put("doYouHaveMultipleOperations", "no");
    data.put("totalIncome", 1000);
    data.put("totalExpenses", 200);
    data.put("netIncomeBeforeAdjustments", 800);
    data.put("totalAdjustments", 200);
    data.put("netFarmingIncome", 1000);
    ((ObjectNode) data.withArray("allowableIncomeGrid").get(0)).with("category").put("label", "Cherries").put("value", "92");
    ((ObjectNode) data.withArray("allowableExpensesGrid").get(0)).with("category").put("label", "Fertilizers").put("value", "9662");
    ObjectNode grain = (ObjectNode) data.withArray("grainGrid").get(0);
    grain.with("commodity").put("label", "Beans, cranberry, organic").put("value", "5392");
    grain.putObject("units").put("label", "Kilograms").put("value", "5");
    data.putArray("vegetableGrid");
    data.putArray("cattleGrid");
    ObjectNode metadata = mapper.createObjectNode();
    metadata.put("id", guid).put("deleted", false).put("draft", false);
    metadata.putObject("submission").set("data", data);
    ObjectNode wrapper = mapper.createObjectNode();
    wrapper.set("submission", metadata);
    return mapper.writeValueAsString(wrapper);
  }

  private Double amount(FarmingOperation operation, int code) {
    for (IncomeExpense entry : operation.getIncomeExpenses()) {
      if (Integer.valueOf(code).equals(entry.getLineItem().getLineItem())) return entry.getReportedAmount();
    }
    fail("Imported income/expense line missing: " + code);
    return null;
  }

  private void cleanUp(String guid) throws Exception {
    // Never drain/cancel other developers' queued work. Each test uses a unique
    // audit user, so these are exactly this invocation's jobs.
    List<Integer> ownJobs = new ArrayList<>();
    try (PreparedStatement query = conn.prepareStatement(
        "SELECT import_version_id FROM farm_import_versions WHERE who_created = ? "
        + "AND import_state_code IN ('SS','SC','SI')")) {
      query.setString(1, user);
      try (ResultSet result = query.executeQuery()) { while (result.next()) ownJobs.add(result.getInt(1)); }
    }
    for (Integer id : ownJobs) ServiceFactory.getImportService().cancelImport(id);
    // Like the existing CHEFS processor tests, retain base-data history while
    // removing this synthetic submission link; never delete another submission.
    chefsDatabaseDao.deleteSubmissions(conn, guid);
    conn.commit();
    CrmTaskResource task = crmDao.getValidationErrorBySubmissionGuid(guid);
    if (task != null) crmDao.deleteValidationErrorTask(task.getActivityId());
    assertNull(chefsDatabaseDao.readSubmissionByGuid(conn, guid));
  }

  /** Expose the normal batch decoder without changing its behavior or fetching
   * real submissions. This exercises the Individuals resource selection. */
  private class IndividualsProcessor extends StatementAIndividualsSubmissionProcessor {
    IndividualsProcessor(String identity) {
      super(ChefsStatementAIndividualsSubmissionTest.this.conn,
          identity);
    }
    SubmissionParentResource<? extends StatementASubmissionDataResource> parse(String json) throws ServiceException {
      return getSubmissionMetaData(json, getSubmissionDataClass());
    }
  }
}
