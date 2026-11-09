package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;
import static ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.processor.OptOutSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.OptOutSubmissionValidator;
import ca.bc.gov.srm.farm.chefs.resource.optOut.OptOutSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.*;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.*;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.OptOutSubmissionDAO;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.ServiceException;

/** Local tests only: no Oracle, CHEFS or CORE connections or credentials. */
public class OptOutSubmissionTest {
  private static final String ENROLMENT_ID = "11111111-1111-4111-8111-111111111111";
  private static final Map<Field, Object> ORIGINALS = new HashMap<>();
  private static boolean optOutEnabled = true;

  @BeforeAll
  static void configureLocally() throws Exception {
    ConfigurationUtility configuration = new ConfigurationUtility() {
      @Override public void initialize(Object resource) { }
      @Override public String getEnvironment() { return "DEV"; }
      @Override public boolean getBoolean(String key) { return !ConfigurationKeys.CHEFS_OPT_OUT_ENABLED.equals(key) || optOutEnabled; }
      @Override public String getValue(String key) {
        if (key == null) return null;
        if (ConfigurationKeys.ENVIRONMENT_NAME.equals(key)) return "DEV";
        return key.contains("GUID") ? "11111111-1111-4111-8111-111111111111" : key;
      }
    };
    replace(ConfigurationUtility.class, "instance", configuration);
    replace(ChefsConfigurationUtil.class, "configUtil", configuration);
  }

  private static void replace(Class<?> type, String name, Object value) throws Exception {
    Field field = type.getDeclaredField(name);
    field.setAccessible(true);
    ORIGINALS.put(field, field.get(null));
    field.set(null, value);
  }

  @AfterAll
  static void restoreConfiguration() throws Exception {
    for (Map.Entry<Field, Object> entry : ORIGINALS.entrySet()) entry.getKey().set(null, entry.getValue());
  }

  private static OptOutSubmissionDataResource validData() throws Exception {
    return new ObjectMapper().readValue("{\"participantName\":\"SAMPLE Farmer\",\"agriStabilityAgriInvestPin\":12345678,"
        + "\"farmType\":{\"label\":\"Individual\",\"value\":\"individual\"},\"sinNumber\":\"123456789\","
        + "\"provinceTerritoryOfMainFarmstead\":\"BC\",\"reasonForCancelling\":\"noLongerFarming\","
        + "\"iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear\":\"2027\","
        + "\"authorizationSignature\":\"data:image/png;base64,SAMPLE\",\"signatureFullName\":\"SAMPLE Farmer\","
        + "\"authorizationSignatureDate\":\"2026-10-29T00:00:00-07:00\"}", OptOutSubmissionDataResource.class);
  }

  private static Client client() {
    Client client = new Client();
    client.setParticipantPin(12345678);
    client.setSin("123456789");
    client.setBusinessNumber("123456789RC0001");
    client.setTrustNumber("T12345678");
    return client;
  }

  private static SubmissionParentResource<OptOutSubmissionDataResource> submission(String guid, OptOutSubmissionDataResource data) {
    SubmissionResource<OptOutSubmissionDataResource> submission = new SubmissionResource<>();
    submission.setData(data);
    SubmissionParentResource<OptOutSubmissionDataResource> metadata = new SubmissionParentResource<>();
    metadata.setSubmissionGuid(guid);
    metadata.setDraft(false);
    metadata.setSubmission(submission);
    return metadata;
  }

  @Test void mapsTheExactSchemaKeysAndPreservesUploadedEvidence() throws Exception {
    OptOutSubmissionDataResource data = validData();
    assertEquals(2027, data.getYear());
    assertEquals(12345678, data.getParticipantPin());
    data.setSimplefile(new ObjectMapper().readTree("[{\"name\":\"certificate.pdf\",\"url\":\"SAMPLE\"}]"));
    JsonNode output = new ObjectMapper().valueToTree(data);
    assertEquals("2027", output.path("iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear").asText());
    assertEquals("certificate.pdf", output.path("simplefile").get(0).path("name").asText());
    assertFalse(output.has("year"));
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
  }

  @Test void requiresOneAllowedYearAndRejectsMissingOrMalformedYears() throws Exception {
    for (String year : new String[] { null, "", "2026,2027", "2027.0", "2025", "99999999999999999" }) {
      OptOutSubmissionDataResource data = validData();
      data.setIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear(year);
      assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty(), "year=" + year);
    }
  }

  @Test void acceptsBothYearsFromTheForm() throws Exception {
    for (String year : new String[] { "2026", "2027" }) {
      OptOutSubmissionDataResource data = validData();
      data.setIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear(year);
      assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    }
  }

  @Test void doesNotTruncateFractionalOrOversizedPinsToAnotherParticipant() throws Exception {
    for (String pin : new String[] { "12345678.5", "99999999999999999" }) {
      OptOutSubmissionDataResource data = new ObjectMapper().readValue(
          "{\"agriStabilityAgriInvestPin\":" + pin + "}", OptOutSubmissionDataResource.class);
      assertNull(data.getParticipantPin());
    }
  }

  @Test void validatesIndividualsAndStatusIndianSin() throws Exception {
    for (String type : new String[] { "individual", "statusIndianFarmingOnAReserve" }) {
      OptOutSubmissionDataResource data = validData();
      data.setFarmType(new LabelValue(type, type));
      data.setSinNumber("123 456 789");
      assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
      data.setSinNumber("987654321");
      assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty());
      data.setSinNumber("T123456789");
      assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    }
  }

  @Test void validatesCorporationAndCooperativeBusinessNumbers() throws Exception {
    for (String type : new String[] { "corporation", "coOperative" }) {
      OptOutSubmissionDataResource data = validData();
      data.setFarmType(new LabelValue(type, type));
      data.setBusinessTaxNumberBn("1234 56789");
      data.setSinNumber(null);
      assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
      data.setBusinessTaxNumberBn("987654321");
      assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    }
  }

  @Test void validatesTrustAndCommunalOrganizationNumbers() throws Exception {
    for (String type : new String[] { "trust", "communalOrganization" }) {
      OptOutSubmissionDataResource data = validData();
      data.setFarmType(new LabelValue(type, type));
      data.setTrustNumber("12345678");
      assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
      data.setTrustNumber("99999999");
      assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    }
  }

  @Test void routesUnverifiableBandIdentityForStaffReview() throws Exception {
    OptOutSubmissionDataResource data = validData();
    data.setFarmType(new LabelValue("Band farm", "bandFarm"));
    data.setBandNumber("123");
    assertTrue(OptOutSubmissionValidator.validate(data, client()).stream().anyMatch(e -> e.contains("staff verification")));
  }

  @Test void rejectsMissingClientFarmTypeSignatureAndReason() throws Exception {
    OptOutSubmissionDataResource data = validData();
    data.setFarmType(null);
    data.setAuthorizationSignature(null);
    data.setSignatureFullName("");
    data.setReasonForCancelling("unknown");
    List<String> errors = OptOutSubmissionValidator.validate(data, null);
    assertTrue(errors.size() >= 5);
  }

  @Test void requiresRepresentativeFieldsOnlyForTheDeathSection() throws Exception {
    OptOutSubmissionDataResource data = validData();
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    data.setReasonForCancelling("deathOfParticipant");
    data.setOnBehalfOfParticipant("yes");
    assertFalse(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    data.setRepresentativeSignature("SAMPLE");
    data.setSignatureFirstName("Sample");
    data.setSignatureLastName("Executor");
    data.setRepresentativeSignatureDate("2026/10/29");
    data.setHowDoYouKnowTheParticipant("Executor");
    data.setSimplefile(new ObjectMapper().readTree("[{\"name\":\"certificate.pdf\",\"url\":\"SAMPLE\"}]"));
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
  }

  @Test void requiresTheDeathCertificateOnlyForTheDeathReason() throws Exception {
    OptOutSubmissionDataResource data = validData();
    data.setReasonForCancelling("deathOfParticipant");
    data.setOnBehalfOfParticipant("yes");
    data.setRepresentativeSignature("SAMPLE");
    data.setSignatureFirstName("Sample");
    data.setSignatureLastName("Executor");
    data.setRepresentativeSignatureDate("2026/10/29");
    data.setHowDoYouKnowTheParticipant("Executor");
    for (String files : new String[] { null, "null", "[]", "[{}]", "{\"name\":\"certificate.pdf\"}" }) {
      data.setSimplefile(files == null ? null : new ObjectMapper().readTree(files));
      List<String> errors = OptOutSubmissionValidator.validate(data, client());
      assertTrue(errors.contains("A copy of the death certificate is required."), "files=" + files);
    }
    data.setSimplefile(new ObjectMapper().readTree("[{\"name\":\"certificate.pdf\",\"url\":\"SAMPLE\"}]"));
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
    data.setReasonForCancelling("noLongerFarming");
    data.setSimplefile(null);
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
  }

  @Test void rejectsDeathWhenTheFormWasNotCompletedOnTheParticipantsBehalf() throws Exception {
    String error = "Reason for cancelling is Death of participant, but the form was not completed on the participant's behalf.";
    OptOutSubmissionDataResource data = validData();
    data.setReasonForCancelling("deathOfParticipant");
    data.setOnBehalfOfParticipant("no");
    data.setSimplefile(new ObjectMapper().readTree("[{\"name\":\"certificate.pdf\",\"url\":\"SAMPLE\"}]"));
    assertEquals(java.util.Collections.singletonList(error), OptOutSubmissionValidator.validate(data, client()));
    data.setReasonForCancelling("noLongerFarming");
    assertTrue(OptOutSubmissionValidator.validate(data, client()).isEmpty());
  }

  @Test void readsTheSignatureKeys() throws Exception {
    String json = "{\"authorizationSignature\":\"P\",\"authorizationSignatureDate\":\"PD\","
        + "\"representativeSignature\":\"R\",\"representativeSignatureDate\":\"RD\"}";
    OptOutSubmissionDataResource data = new ObjectMapper().readValue(json, OptOutSubmissionDataResource.class);
    assertEquals("P", data.getAuthorizationSignature());
    assertEquals("PD", data.getAuthorizationSignatureDate());
    assertEquals("R", data.getRepresentativeSignature());
    assertEquals("RD", data.getRepresentativeSignatureDate());
  }

  @Test void corePatchContainsOnlyTheEnrolmentStatus() {
    CrmEnrolmentStatusUpdateResource patch = new CrmEnrolmentStatusUpdateResource();
    patch.setEnrolmentStatusCode(CrmConstants.ENROLMENT_STATUS_CODE_OPTED_OUT);
    JsonNode json = new ObjectMapper().valueToTree(patch);
    assertEquals(1, json.size());
    assertEquals(865520001, json.path("vsi_enrolmentstatus").asInt());
  }

  @Test void updatesTheSelectedEnrolmentAndRecordsProcessed() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.processSubmission(submission("request-1", validData()));
    assertEquals(2027, processor.crm.requestedYear);
    assertEquals(ENROLMENT_ID, processor.crm.updatedId);
    assertEquals(1, processor.crm.updates);
    assertEquals(PROCESSED, processor.getSubmissionRecordMap().get("request-1").getSubmissionStatusCode());
    assertEquals("Y", processor.getSubmissionRecordMap().get("request-1").getBceidFormInd());
    assertEquals("request-1", processor.reservations.owners.get(ENROLMENT_ID));
    assertEquals(0, processor.crm.validationTasks);
  }

  @Test void preventsAnotherFormFromOptingOutTheSameParticipantYear() throws Exception {
    Reservations reservations = new Reservations();
    Harness first = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, reservations);
    first.processSubmission(submission("request-1", validData()));
    Harness second = new Harness(ChefsConstants.USER_TYPE_IDIR, reservations);
    OptOutSubmissionDataResource data = validData();
    data.setOrigin("internal"); data.setInternalMethod("chefsForm"); data.setEnvironment("DEV");
    second.processSubmission(submission("request-2", data));
    assertEquals(DUPLICATE, second.getSubmissionRecordMap().get("request-2").getSubmissionStatusCode());
    assertEquals(0, second.crm.updates);
    assertEquals(1, second.crm.validationTasks);
  }

  @Test void allowsAnotherYearOnTheSameParticipant() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.processSubmission(submission("request-1", validData()));
    processor.crm.enrolment.setVsi_participantprogramyearid("22222222-2222-4222-8222-222222222222");
    processor.crm.enrolment.setEnrolmentStatusCode(0);
    OptOutSubmissionDataResource nextYear = validData();
    nextYear.setIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear("2026");
    processor.processSubmission(submission("request-2", nextYear));
    assertEquals(2, processor.crm.updates);
    assertEquals(2, processor.reservations.owners.size());
  }

  @Test void rejectsInvalidIdentityBeforeAnyCoreWriteOrReservation() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    OptOutSubmissionDataResource data = validData(); data.setSinNumber("999999999");
    processor.processSubmission(submission("request", data));
    assertEquals(INVALID, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
    assertEquals(0, processor.crm.updates);
    assertTrue(processor.reservations.owners.isEmpty());
  }

  @Test void routesMissingCoreAccountAndEnrolmentToValidation() throws Exception {
    for (boolean missingAccount : new boolean[] { true, false }) {
      Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
      if (missingAccount) processor.crm.account = null; else processor.crm.enrolment = null;
      processor.processSubmission(submission("request", validData()));
      assertEquals(INVALID, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
      assertEquals(0, processor.crm.updates);
      assertTrue(processor.reservations.owners.isEmpty());
    }
  }

  @Test void retriesAfterCoreAppliedAnUpdateButTheResponseWasLost() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.crm.failAfterApplying = true;
    assertThrows(ServiceException.class, () -> processor.processSubmission(submission("request", validData())));
    assertEquals("request", processor.reservations.owners.get(ENROLMENT_ID));
    processor.crm.failAfterApplying = false;
    processor.processSubmission(submission("request", validData()));
    assertEquals(PROCESSED, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
    assertEquals(1, processor.crm.updates);
  }

  @Test void retriesAfterCoreFailureBeforeItAppliedTheUpdate() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.crm.failBeforeApplying = true;
    assertThrows(ServiceException.class, () -> processor.processSubmission(submission("request", validData())));
    processor.crm.failBeforeApplying = false;
    processor.processSubmission(submission("request", validData()));
    assertEquals(PROCESSED, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
    assertEquals(1, processor.crm.updates);
  }

  @Test void anAlreadyOptedOutEnrolmentDoesNotNeedAnotherPatch() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.crm.enrolment.setEnrolmentStatusCode(CrmConstants.ENROLMENT_STATUS_CODE_OPTED_OUT);
    processor.processSubmission(submission("request", validData()));
    assertEquals(PROCESSED, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
    assertEquals(0, processor.crm.updates);
  }

  @Test void doesNotProcessDraftsDeletedOrPreviouslyProcessedSubmissions() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    SubmissionParentResource<OptOutSubmissionDataResource> metadata = submission("request", validData());
    metadata.setDraft(true); processor.processSubmission(metadata);
    metadata.setDraft(false); metadata.setDeleted(true); processor.processSubmission(metadata);
    assertTrue(processor.getSubmissionRecordMap().isEmpty());
    metadata.setDeleted(false); processor.processSubmission(metadata); processor.processSubmission(metadata);
    assertEquals(1, processor.crm.updates);
  }

  @Test void refusesToChangeTheTargetEnrolmentAfterAnUncertainCoreUpdate() throws Exception {
    Harness processor = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    processor.crm.failAfterApplying = true;
    assertThrows(ServiceException.class, () -> processor.processSubmission(submission("request", validData())));
    processor.crm.failAfterApplying = false;
    processor.crm.enrolment.setVsi_participantprogramyearid("22222222-2222-4222-8222-222222222222");
    OptOutSubmissionDataResource changed = validData(); changed.setIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear("2026");
    processor.processSubmission(submission("request", changed));
    assertEquals(INVALID, processor.getSubmissionRecordMap().get("request").getSubmissionStatusCode());
    assertEquals(1, processor.crm.updates);
  }

  @Test void credentialsUseSeparateIdentitySettings() {
    ChefsFormCredentials idir = ChefsConfigurationUtil.getInstance().getFormCredentials(ChefsFormTypeCodes.OPT_OUT, ChefsConstants.USER_TYPE_IDIR);
    ChefsFormCredentials bceid = ChefsConfigurationUtil.getInstance().getFormCredentials(ChefsFormTypeCodes.OPT_OUT, ChefsConstants.USER_TYPE_BASIC_BCEID);
    assertEquals(ConfigurationKeys.CHEFS_OPT_OUT_IDIR_FORM_API_KEY, idir.getApiKey());
    assertEquals(ConfigurationKeys.CHEFS_OPT_OUT_BASIC_BCEID_FORM_API_KEY, bceid.getApiKey());
  }

  @Test void retainsTheExistingEnvironmentAndDisabledBceidRules() throws Exception {
    Harness bceid = new Harness(ChefsConstants.USER_TYPE_BASIC_BCEID, new Reservations());
    bceid.setBasicBCeIDFormsEnabled(false);
    assertFalse(bceid.checkEnvironment(submission("disabled", validData())));
    assertEquals(OTHER_ENV, bceid.getSubmissionRecordMap().get("disabled").getSubmissionStatusCode());
    Harness idir = new Harness(ChefsConstants.USER_TYPE_IDIR, new Reservations());
    assertTrue(idir.checkEnvironment(submission("no-admin", validData())));
    assertTrue(idir.getSubmissionRecordMap().isEmpty());
    OptOutSubmissionDataResource staff = validData();
    staff.setOrigin("internal"); staff.setInternalMethod("chefsForm"); staff.setEnvironment("DEV");
    assertTrue(idir.checkEnvironment(submission("staff", staff)));
    staff.setEnvironment("DLVR");
    assertFalse(idir.checkEnvironment(submission("other-env", staff)));
  }

  @Test void theSuppliedSchemaWorksForIdirWithoutStaffOnlyFields() throws Exception {
    Harness idir = new Harness(ChefsConstants.USER_TYPE_IDIR, new Reservations());
    idir.processSubmission(submission("idir", validData()));
    assertEquals(PROCESSED, idir.getSubmissionRecordMap().get("idir").getSubmissionStatusCode());
    assertEquals("N", idir.getSubmissionRecordMap().get("idir").getBceidFormInd());
  }

  @Test void unactivatedFormsAreLeftUnconsumed() throws Exception {
    optOutEnabled = false;
    try {
      Harness processor = new Harness(ChefsConstants.USER_TYPE_IDIR, new Reservations());
      processor.processSubmission(submission("request", validData()));
      assertTrue(processor.getSubmissionRecordMap().isEmpty());
      assertEquals(0, processor.crm.updates);
    } finally { optOutEnabled = true; }
  }

  private static class Reservations extends OptOutSubmissionDAO {
    final Map<String, String> owners = new HashMap<>();
    final Map<Integer, String> targets = new HashMap<>();
    @Override public String readEnrolmentForSubmission(Connection c, Integer id) { return targets.get(id); }
    @Override public String claimEnrolment(Connection c, String enrolment, Integer id, String guid, String user) {
      if (!owners.containsKey(enrolment)) { owners.put(enrolment, guid); targets.put(id, enrolment); }
      return owners.get(enrolment);
    }
  }

  private static class Harness extends OptOutSubmissionProcessor {
    final FakeCore crm = new FakeCore();
    final Reservations reservations;
    Harness(String identity, Reservations reservations) {
      super(null, identity);
      this.reservations = reservations; optOutDao = reservations; crmDao = crm;
      submissionRecordMap = new HashMap<>();
      chefsDatabaseDao = new ChefsDatabaseDAO() {
        @Override public Collection<String> readSubmissionGuidsForPinAndProgramYear(Connection c, String type, Integer pin, Integer year, String guid) {
          return new ArrayList<>();
        }
      };
    }
    @Override protected Client readClient(Integer pin) { return client(); }
    @Override protected String getValidationQueueId() { return "local-validation"; }
    @Override protected ChefsSubmission createOrUpdateSubmission(ChefsSubmission record) {
      if (record.getSubmissionId() == null) record.setSubmissionId(Math.abs(record.getSubmissionGuid().hashCode()));
      submissionRecordMap.put(record.getSubmissionGuid(), record);
      return record;
    }
  }

  private static class FakeCore extends CrmRestApiDao {
    CrmAccountResource account = new CrmAccountResource();
    CrmEnrolmentResource enrolment = new CrmEnrolmentResource();
    int updates; int validationTasks; Integer requestedYear; String updatedId;
    boolean failAfterApplying; boolean failBeforeApplying;
    FakeCore() { account.setAccountid("sample-account"); enrolment.setVsi_participantprogramyearid(ENROLMENT_ID); }
    @Override public CrmAccountResource getAccountByPin(int pin) { return account; }
    @Override public CrmProgramYearResource getProgramYear(int year) {
      requestedYear = year;
      CrmProgramYearResource resource = new CrmProgramYearResource(); resource.setVsi_programyearid("year-" + year); return resource;
    }
    @Override public CrmEnrolmentResource getEnrolment(String yearId, String accountId) { return enrolment; }
    @Override public void updateEnrolmentStatus(String id, Integer status) throws ServiceException {
      if (failBeforeApplying) throw new ServiceException("Simulated connection failure");
      updates++; updatedId = id; enrolment.setEnrolmentStatusCode(status);
      if (failAfterApplying) throw new ServiceException("Simulated lost response");
    }
    @Override public CrmValidationErrorResource getValidationErrorBySubmissionGuid(String guid) { return null; }
    @Override public CrmValidationErrorResource createValidationErrorTask(CrmValidationErrorResource task, String queue) {
      validationTasks++; task.setActivityId("sample-validation-" + validationTasks); return task;
    }
  }
}
