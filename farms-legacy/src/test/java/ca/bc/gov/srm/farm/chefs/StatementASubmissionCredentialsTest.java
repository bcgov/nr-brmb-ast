package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.StatementACorporateSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.StatementAIndividualsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.StatementASubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAIndividualsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.impl.CdogsServiceImpl;
import ca.bc.gov.srm.farm.service.impl.ChefsFormSubmissionServiceImpl;
import ca.bc.gov.srm.farm.service.impl.CrmTransferServiceImpl;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsResultAction;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsSearchAction;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsSubmissionResultForm;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsSubmissionSearchForm;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsUpdateFormAction;
import ca.bc.gov.srm.farm.ui.struts.chefs.ChefsUploadFormAction;

/** Real REST clients against a loopback fixture; no Oracle, CHEFS or CORE writes. */
public class StatementASubmissionCredentialsTest {
  @Test
  void formTypesSelectCredentialsTemplatesAndManagementRoutes() throws Exception {
    // The legacy services retain static configuration. Isolate it from other integration tests.
    File log = File.createTempFile("statement-a-routes-", ".log");
    Process process = null;
    try {
      String executable = new File(System.getProperty("java.home"), "bin/java").getPath();
      process = new ProcessBuilder(executable, "-cp", System.getProperty("java.class.path"), getClass().getName())
          .redirectErrorStream(true).redirectOutput(log).start();
      assertTrue(process.waitFor(60, TimeUnit.SECONDS), "Routing checks timed out");
      String output = new String(Files.readAllBytes(log.toPath()), StandardCharsets.UTF_8);
      assertEquals(0, process.exitValue(), output);
      assertTrue(output.contains("PASS: Statement A routing checks"), output);
    } finally {
      if (process != null && process.isAlive()) process.destroyForcibly();
      Files.deleteIfExists(log.toPath());
    }
  }

  private static final Map<String, String> settings = new HashMap<>();
  private static final List<String> requests = new ArrayList<>();
  private static HttpServer server;
  private static String lastTemplate;
  private static String renderedBody;
  private static ChefsFormSubmissionServiceImpl service;

  public static void main(String[] args) throws Exception {
    try {
      setUp();
      for (String type : Arrays.asList(ChefsFormTypeCodes.STA, ChefsFormTypeCodes.STA_IND)) {
        for (String identity : Arrays.asList(ChefsConstants.USER_TYPE_IDIR, ChefsConstants.USER_TYPE_BASIC_BCEID)) {
          verifyProcessor(type, identity);
          verifyRead(type, identity);
          verifyPdf(type, identity);
          verifyManagementAction(new ChefsResultAction(), type, identity);
          verifyManagementAction(new ChefsUpdateFormAction(), type, identity);
          verifyUpload(type, identity);
          verifyCoreAttachment(type, identity);
        }
        verifySearch(type);
      }
      verifyIndividualsWithoutFarmType();
      verifyFiscalDatesForBothForms();
      // A missing new configuration must not affect reading existing Corporate submissions.
      settings.put(ConfigurationKeys.CHEFS_STATEMENT_A_INDIVIDUALS_IDIR_FORM_API_KEY, "placeholder");
      assertFalse(ChefsConfigurationUtil.getInstance().getFormCredentials(ChefsFormTypeCodes.STA_IND,
          ChefsConstants.USER_TYPE_IDIR).isConfigured());
      verifyRead(ChefsFormTypeCodes.STA, ChefsConstants.USER_TYPE_IDIR);
      requests.clear();
      service.getSubmissionWrapperResource("sample", ChefsFormTypeCodes.SUPP, ChefsConstants.USER_TYPE_IDIR);
      assertEquals(Collections.singletonList(authentication("supplemental")), requests);
      System.out.println("PASS: Statement A routing checks: 4 credential reads, 4 PDFs, 4 active processors, "
          + "8 management reads/updates, 4 PDF retries, 4 CORE attachment routes, 2 search/template selections, legacy and unrelated reads");
    } finally {
      if (server != null) server.stop(0);
    }
  }

  private static void setUp() throws Exception {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/", exchange -> {
      String path = exchange.getRequestURI().getPath();
      String body;
      if (path.equals("/token")) {
        body = "{\"access_token\":\"test-token\"}";
      } else if (path.startsWith("/api/v1/template/")) {
        lastTemplate = path;
        body = path.endsWith("/render") ? "%PDF-test-fixture" : "OK";
      } else if (path.equals("/api/submissions/sample")) {
        requests.add(exchange.getRequestHeaders().getFirst("Authorization"));
        body = "{\"submission\":{\"confirmationId\":\"TEST\",\"createdAt\":1704067200000,\"submission\":{\"data\":"
            + "{\"agriStabilityAgriInvestPin\":1234567,\"fiscalYearStart\":1704067200000,\"fiscalYearEnd\":1735603200000,"
            + "\"netFarmingIncome\":125}}}}";
        String authorization = exchange.getRequestHeaders().getFirst("Authorization");
        if (authentication("individual-idir").equals(authorization)
            || authentication("individual-bceid").equals(authorization)) {
          body = body.replace("\"netFarmingIncome\":125", "\"netIncomeAfterAdjustments\":125,\"netFarmingIncome\":125")
              .replace("\"agriStabilityAgriInvestPin\":", "\"participantName\":\"SAMPLE ONLY - Alex Farmer\",\"agriStabilityAgriInvestPin\":");
        }
      } else {
        exchange.sendResponseHeaders(404, -1); exchange.close(); return;
      }
      // Drain local request bodies before responding, including PDF-generation requests.
      byte[] buffer = new byte[4096];
      java.io.ByteArrayOutputStream requestBody = new java.io.ByteArrayOutputStream();
      int count;
      while ((count = exchange.getRequestBody().read(buffer)) != -1) requestBody.write(buffer, 0, count);
      if (path.endsWith("/render")) renderedBody = new String(requestBody.toByteArray(), StandardCharsets.UTF_8);
      byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().set("Content-Type", "application/json");
      exchange.sendResponseHeaders(200, bytes.length);
      try (java.io.OutputStream output = exchange.getResponseBody()) { output.write(bytes); }
    });
    server.start();
    String base = "http://127.0.0.1:" + server.getAddress().getPort();
    settings.put(ConfigurationKeys.CHEFS_URL, base);
    settings.put(ConfigurationKeys.CHEFS_API_VERSION, "api");
    settings.put(ConfigurationKeys.CDOGS_BASE_URL, base);
    settings.put(ConfigurationKeys.CDOGS_API_VERSION, "1");
    settings.put(ConfigurationKeys.CDOGS_AUTHORITY_URL, base + "/token");
    settings.put(ConfigurationKeys.CDOGS_STATEMENT_A_TEMPLATE_GUID, "corporate-template");
    settings.put(ConfigurationKeys.CDOGS_STATEMENT_A_INDIVIDUALS_TEMPLATE_GUID, "individual-template");
    putCredentials(ConfigurationKeys.CHEFS_STATEMENT_A_IDIR_FORM_GUID, ConfigurationKeys.CHEFS_STATEMENT_A_IDIR_FORM_API_KEY, "corporate-idir");
    putCredentials(ConfigurationKeys.CHEFS_STATEMENT_A_BASIC_BCEID_FORM_GUID, ConfigurationKeys.CHEFS_STATEMENT_A_BASIC_BCEID_FORM_API_KEY, "corporate-bceid");
    putCredentials(ConfigurationKeys.CHEFS_STATEMENT_A_INDIVIDUALS_IDIR_FORM_GUID, ConfigurationKeys.CHEFS_STATEMENT_A_INDIVIDUALS_IDIR_FORM_API_KEY, "individual-idir");
    putCredentials(ConfigurationKeys.CHEFS_STATEMENT_A_INDIVIDUALS_BASIC_BCEID_FORM_GUID, ConfigurationKeys.CHEFS_STATEMENT_A_INDIVIDUALS_BASIC_BCEID_FORM_API_KEY, "individual-bceid");
    putCredentials(ConfigurationKeys.CHEFS_SUPPLEMENTAL_IDIR_FORM_GUID, ConfigurationKeys.CHEFS_SUPPLEMENTAL_IDIR_FORM_API_KEY, "supplemental");
    setField(ConfigurationUtility.class, "instance", new ConfigurationUtility() {
      @Override public void initialize(Object resource) { }
      @Override public String getEnvironment() { return "DEV"; }
      @Override public String getValue(String key) { return settings.getOrDefault(key, "local-test"); }
      @Override public boolean getBoolean(String key) { return false; }
    });
    setField(ServiceFactory.class, "cdogsService", proxy(CdogsService.class,
        (p,m,a) -> { throw new AssertionError("Unexpected CDOGS service call: " + m); }));
    service = new ChefsFormSubmissionServiceImpl();
  }

  private static void verifyRead(String type, String identity) throws Exception {
    requests.clear();
    StatementASubmissionDataResource data = (StatementASubmissionDataResource) service
        .getSubmissionWrapperResource("sample", type, identity).getSubmissionMetaData().getSubmission().getData();
    if (ChefsFormTypeCodes.STA_IND.equals(type)) {
      assertEquals(Double.valueOf(125), data.getNetFarmingIncome());
      assertTrue(data instanceof StatementAIndividualsSubmissionDataResource);
      assertEquals("SAMPLE ONLY - Alex Farmer", ((StatementAIndividualsSubmissionDataResource)data).getParticipantName());
    } else {
      assertEquals(Double.valueOf(125), data.getNetFarmingIncome());
      assertEquals(StatementASubmissionDataResource.class, data.getClass());
    }
    assertEquals(Collections.singletonList(authentication(owner(type, identity))), requests);
  }

  @SuppressWarnings("unchecked")
  private static void verifyIndividualsWithoutFarmType() throws Exception {
    StatementAIndividualsSubmissionDataResource data = new StatementAIndividualsSubmissionDataResource();
    data.setParticipantName("SAMPLE ONLY - Alex Farmer");
    data.setAgriStabilityAgriInvestPin(1234567);
    data.setSinNumber("123456789");
    data.setBusinessStructure("soleProprietorship");
    data.setFiscalYearStart(new java.text.SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"));
    data.setFiscalYearEnd(new java.text.SimpleDateFormat("yyyy-MM-dd").parse("2024-12-31"));
    data.setCompletedProductionCycle("yes");
    data.setNumberOfCowsThatCalved(1.0);
    Client client = new Client(); client.setSin("123456789");
    StatementASubmissionProcessor individual = new StatementAIndividualsSubmissionProcessor(null, ChefsConstants.USER_TYPE_IDIR);
    Method validate = StatementASubmissionProcessor.class.getDeclaredMethod("validate", StatementASubmissionDataResource.class, Client.class, CrmAccountResource.class);
    validate.setAccessible(true);
    assertTrue(((List<String>)validate.invoke(individual, data, client, new CrmAccountResource())).isEmpty());
    data.setSinNumber("987654321");
    assertFalse(((List<String>)validate.invoke(individual, data, client, new CrmAccountResource())).isEmpty());
    data.setSinNumber(null);
    assertFalse(((List<String>)validate.invoke(individual, data, client, new CrmAccountResource())).isEmpty());
    data.setSinNumber("123456789");
    data.setFiscalYearStart(null);
    assertTrue(((List<String>)validate.invoke(individual, data, client, new CrmAccountResource())).stream().anyMatch(e -> e.contains("Fiscal year start and end are required")));
    data.setFiscalYearStart(new java.text.SimpleDateFormat("yyyy-MM-dd").parse("2024-01-01"));
    Method name = StatementASubmissionProcessor.class.getDeclaredMethod("getApplicantName", StatementASubmissionDataResource.class);
    name.setAccessible(true);
    assertEquals(data.getParticipantName(), name.invoke(individual, data));
    // Exercise the batch processor's real JSON entry point, stopping before database work.
    final StatementASubmissionDataResource[] parsed = new StatementASubmissionDataResource[1];
    StatementASubmissionProcessor parser = new StatementAIndividualsSubmissionProcessor(null, ChefsConstants.USER_TYPE_IDIR) {
      @Override public ca.bc.gov.srm.farm.crm.resource.CrmTaskResource processSubmission(
          ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource<? extends StatementASubmissionDataResource> metadata) {
        parsed[0] = metadata.getSubmission().getData(); return null;
      }
    };
    Method processJson = StatementASubmissionProcessor.class.getDeclaredMethod("processSubmission", String.class, String.class);
    processJson.setAccessible(true);
    processJson.invoke(parser, "sample", "{\"submission\":{\"draft\":false,\"submission\":{\"data\":{"
        + "\"participantName\":\"SAMPLE ONLY - Alex Farmer\",\"netIncomeAfterAdjustments\":1000,\"netFarmingIncome\":1000}}}}");
    assertTrue(parsed[0] instanceof StatementAIndividualsSubmissionDataResource);
    assertEquals(data.getParticipantName(), ((StatementAIndividualsSubmissionDataResource)parsed[0]).getParticipantName());
    assertEquals(1000.0, parsed[0].getNetFarmingIncome());
    Method dataClass = StatementASubmissionProcessor.class.getDeclaredMethod("getSubmissionDataClass");
    dataClass.setAccessible(true);
    assertEquals(StatementAIndividualsSubmissionDataResource.class, dataClass.invoke(individual));
    Method populate = StatementASubmissionProcessor.class.getDeclaredMethod("populateFarmYear", StatementASubmissionDataResource.class, Scenario.class, java.util.Date.class);
    populate.setAccessible(true);
    Scenario scenario = new Scenario(); scenario.setFarmingYear(new FarmingYear());
    FarmingYear year = (FarmingYear)populate.invoke(individual, data, scenario, new java.util.Date());
    assertTrue(year.getIsSoleProprietor());
    data.setBusinessStructure("partnership");
    scenario.setFarmingYear(new FarmingYear());
    year = (FarmingYear)populate.invoke(individual, data, scenario, new java.util.Date());
    assertTrue(year.getIsPartnershipMember());
    StatementASubmissionProcessor corporate = new StatementACorporateSubmissionProcessor(null, ChefsConstants.USER_TYPE_IDIR);
    assertTrue(((List<String>)validate.invoke(corporate, data, client, new CrmAccountResource())).stream().anyMatch(e -> e.contains("Farm Type")));
    data.setFarmType(new LabelValue("Corporation", "corporation"));
    data.setBusinessStructure("corporation"); data.setBusinessTaxNumber("123456789");
    client.setBusinessNumber("123456789RC0001");
    assertTrue(((List<String>)validate.invoke(corporate, data, client, new CrmAccountResource())).isEmpty());
    scenario.setFarmingYear(new FarmingYear());
    year = (FarmingYear)populate.invoke(corporate, data, scenario, new java.util.Date());
    assertTrue(year.getIsCorporateShareholder());
    System.out.println("PASS: Individuals without farmType, SIN/date validation, participant name, sole proprietor/partnership, Corporate farmType rules");
  }

  @SuppressWarnings("unchecked")
  private static void verifyFiscalDatesForBothForms() throws Exception {
    Method validate = StatementASubmissionProcessor.class.getDeclaredMethod("validate",
        StatementASubmissionDataResource.class, Client.class, CrmAccountResource.class);
    validate.setAccessible(true);
    java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.util.Date start = format.parse("2024-01-01");
    java.util.Date end = format.parse("2024-12-31");
    for (String type : Arrays.asList(ChefsFormTypeCodes.STA, ChefsFormTypeCodes.STA_IND)) {
      for (String identity : Arrays.asList(ChefsConstants.USER_TYPE_IDIR, ChefsConstants.USER_TYPE_BASIC_BCEID)) {
        boolean individual = ChefsFormTypeCodes.STA_IND.equals(type);
        StatementASubmissionProcessor processor = individual
            ? new StatementAIndividualsSubmissionProcessor(null, identity)
            : new StatementACorporateSubmissionProcessor(null, identity);
        StatementASubmissionDataResource data = individual
            ? new StatementAIndividualsSubmissionDataResource() : new StatementASubmissionDataResource();
        data.setAgriStabilityAgriInvestPin(1234567);
        data.setSinNumber("123456789");
        data.setBusinessTaxNumber("123456789");
        data.setBusinessStructure(individual ? "soleProprietorship" : "corporation");
        if (!individual) data.setFarmType(new LabelValue("Corporation", "corporation"));
        data.setCompletedProductionCycle("yes");
        data.setNumberOfCowsThatCalved(1.0);
        Client client = new Client();
        client.setSin("123456789");
        client.setBusinessNumber("123456789RC0001");
        CrmAccountResource account = new CrmAccountResource();
        String route = type + " / " + identity;
        List<String> required = Collections.singletonList("Fiscal year start and end are required.");
        assertEquals(required, (List<String>)validate.invoke(processor, data, client, account), route + ": both missing");
        data.setFiscalYearEnd(end);
        assertEquals(required, (List<String>)validate.invoke(processor, data, client, account), route + ": start missing");
        data.setFiscalYearStart(start);
        data.setFiscalYearEnd(null);
        assertEquals(required, (List<String>)validate.invoke(processor, data, client, account), route + ": end missing");
        data.setFiscalYearStart(end);
        data.setFiscalYearEnd(start);
        assertEquals(Collections.singletonList("Fiscal year start must be on or before fiscal year end."),
            (List<String>)validate.invoke(processor, data, client, account), route + ": reversed dates");
        data.setFiscalYearStart(start);
        assertTrue(((List<String>)validate.invoke(processor, data, client, account)).isEmpty(), route + ": equal dates");
        data.setFiscalYearEnd(end);
        assertTrue(((List<String>)validate.invoke(processor, data, client, account)).isEmpty(), route + ": valid dates");
      }
    }
    System.out.println("PASS: Shared fiscal date validation: 6 date cases across Corporate and Individuals IDIR/BCeID");
  }

  private static void verifyProcessor(String type, String identity) {
    StatementASubmissionProcessor processor = ChefsFormTypeCodes.STA_IND.equals(type)
        ? new StatementAIndividualsSubmissionProcessor(null, identity) : new StatementACorporateSubmissionProcessor(null, identity);
    assertEquals(type, processor.getFormTypeCode());
    assertEquals(owner(type, identity), processor.getFormCredentials().getFormId());
    assertEquals(owner(type, identity) + "-key", processor.getFormCredentials().getApiKey());
  }

  private static void verifyPdf(String type, String identity) throws Exception {
    requests.clear(); lastTemplate = null; renderedBody = null;
    Map<Integer, File> files = new CdogsServiceImpl().createCdogsDocumentByFormType("sample", identity, type);
    File output = files.get(1234567);
    try {
      assertEquals(Collections.singletonList(authentication(owner(type, identity))), requests);
      assertEquals("/api/v1/template/" + template(type) + "/render", lastTemplate);
      JsonNode renderData = new ObjectMapper().readTree(renderedBody).get("data");
      assertEquals(125.0, renderData.get("netFarmingIncome").asDouble());
      if (ChefsFormTypeCodes.STA_IND.equals(type)) {
        assertEquals("SAMPLE ONLY - Alex Farmer", renderData.get("participantName").asText());
      }
      assertTrue(new String(Files.readAllBytes(output.toPath()), StandardCharsets.UTF_8).startsWith("%PDF-test-fixture"));
    } finally { if (output != null) Files.deleteIfExists(output.toPath()); }
  }

  private static void verifyManagementAction(Object action, String type, String identity) throws Exception {
    ChefsSubmission submission = new ChefsSubmission();
    submission.setFormTypeCode(type);
    submission.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
    submission.setBceidFormInd(ChefsConstants.USER_TYPE_IDIR.equals(identity) ? "N" : "Y");
    List<String> reads = new ArrayList<>();
    setField(ServiceFactory.class, "chefsFormSubmissionService", proxy(ChefsFormSubmissionService.class, (p,m,a) -> {
      switch (m.getName()) {
      case "getSubmissionByGuid": return submission;
      case "getUserFormType": case "getUserFormTypeFromPreflight": return identity;
      case "updateSubmission": return null;
      case "getSubmissionWrapperResource":
        assertArrayEquals(new Object[] {"sample", type, identity}, a);
        reads.add(type); return null;
      default: throw new AssertionError("Unexpected management call: " + m);
      }
    }));
    ChefsSubmissionResultForm form = new ChefsSubmissionResultForm();
    form.setSubmissionGuid("sample"); form.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
    // Stale request values must not override the persisted submission's type.
    form.setFormType(ChefsFormTypeCodes.STA.equals(type) ? ChefsFormTypeCodes.STA_IND : ChefsFormTypeCodes.STA);
    invokeAction(action, form);
    assertEquals(type, form.getFormType());
    assertEquals(Collections.singletonList(type), reads);
  }

  private static void verifySearch(String type) throws Exception {
    setField(ServiceFactory.class, "chefsFormSubmissionService", proxy(ChefsFormSubmissionService.class, (p,m,a) -> {
      assertEquals("getFormSubmissionsByType", m.getName()); assertEquals(type, a[0]); return Collections.emptyList();
    }));
    ChefsSubmissionSearchForm form = new ChefsSubmissionSearchForm(); form.setFormType(type);
    invokeAction(new ChefsSearchAction(), form);
    assertTrue(form.getFormTypes().stream().anyMatch(v -> ChefsFormTypeCodes.STA_IND.equals(v.getValue())));
    assertEquals(type, form.getFormType());
    assertEquals(template(type), form.getCdogsTemplates().get(0).getTemplateGuid());
    assertTrue(form.getCdogsTemplates().get(0).getIsCached());
  }

  private static void verifyUpload(String type, String identity) throws Exception {
    List<String> uploads = new ArrayList<>();
    File file = new File("synthetic-pdf.pdf");
    setField(ServiceFactory.class, "cdogsService", proxy(CdogsService.class, (p,m,a) -> {
      assertEquals("createCdogsDocumentByFormType", m.getName());
      assertArrayEquals(new Object[] {"sample", identity, type}, a);
      return Collections.singletonMap(1234567, file);
    }));
    setField(ServiceFactory.class, "crmTransferService", proxy(CrmTransferService.class, (p,m,a) -> {
      assertEquals("uploadFileToNote", m.getName()); assertArrayEquals(new Object[] {file, "note", type}, a);
      uploads.add(type); return null;
    }));
    setField(ServiceFactory.class, "chefsFormSubmissionService", proxy(ChefsFormSubmissionService.class, (p,m,a) -> {
      assertEquals("getSubmissionCrmEntityGuidsBySubmissionGuid", m.getName());
      ChefsSubmssnCrmEntity entity = new ChefsSubmssnCrmEntity(); entity.setCrmEntityGuid("note");
      return Collections.singletonList(entity);
    }));
    ChefsSubmissionResultForm form = new ChefsSubmissionResultForm();
    form.setSubmissionGuid("sample"); form.setFormType(type); form.setUserFormType(identity);
    invokeAction(new ChefsUploadFormAction(), form);
    assertEquals(Collections.singletonList(type), uploads);
  }

  private static void invokeAction(Object action, ActionForm form) throws Exception {
    ActionMapping mapping = new ActionMapping(); mapping.addForwardConfig(new ActionForward("success", "/success", false));
    Method method = action.getClass().getDeclaredMethod("doExecute", ActionMapping.class, ActionForm.class,
        javax.servlet.http.HttpServletRequest.class, javax.servlet.http.HttpServletResponse.class);
    method.setAccessible(true);
    assertEquals("/success", ((ActionForward)method.invoke(action, mapping, form, null, null)).getPath());
  }

  private static void verifyCoreAttachment(String type, String identity) throws Exception {
    ServiceException sentinel = new ServiceException("Stop before uploading to CORE");
    String expected = ChefsFormTypeCodes.STA_IND.equals(type)
        ? "createCdogsStatementAIndividualsFormDocument" : "createCdogsStatementAFormDocument";
    setField(ServiceFactory.class, "cdogsService", proxy(CdogsService.class, (p,m,a) -> {
      assertEquals(expected, m.getName()); assertArrayEquals(new Object[] {"sample", identity}, a); throw sentinel;
    }));
    CrmTransferServiceImpl transfer = new CrmTransferServiceImpl();
    Field dao = CrmTransferServiceImpl.class.getDeclaredField("chefsDatabaseDao"); dao.setAccessible(true);
    dao.set(transfer, new ChefsDatabaseDAO() {
      @Override public ChefsSubmission readSubmissionByGuid(java.sql.Connection connection, String guid) {
        ChefsSubmission record = new ChefsSubmission(); record.setFormTypeCode(type); record.setSubmissionId(1); return record;
      }
      @Override public void createCrmEntity(java.sql.Connection connection, ChefsSubmssnCrmEntity entity, String user) { }
    });
    Method method = CrmTransferServiceImpl.class.getDeclaredMethod("uploadSubmissionFormToCrmUpdateBenefitNote",
        java.sql.Connection.class, String.class, String.class, String.class, String.class, String.class, String.class);
    method.setAccessible(true);
    java.lang.reflect.InvocationTargetException thrown = assertThrows(java.lang.reflect.InvocationTargetException.class,
        () -> method.invoke(transfer, null, "sample", identity, "benefit", ScenarioCategoryCodes.CHEF_STA, null, "test"));
    assertSame(sentinel, thrown.getCause());
  }

  private static String owner(String type, String identity) {
    return (ChefsFormTypeCodes.STA_IND.equals(type) ? "individual" : "corporate")
        + (ChefsConstants.USER_TYPE_IDIR.equals(identity) ? "-idir" : "-bceid");
  }
  private static String template(String type) { return ChefsFormTypeCodes.STA_IND.equals(type) ? "individual-template" : "corporate-template"; }
  private static String authentication(String owner) { return "Basic " + Base64.getEncoder().encodeToString((owner + ":" + owner + "-key").getBytes(StandardCharsets.UTF_8)); }
  private static void putCredentials(String guidKey, String keyKey, String owner) { settings.put(guidKey, owner); settings.put(keyKey, owner + "-key"); }
  private static Object proxy(Class<?> type, java.lang.reflect.InvocationHandler handler) { return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, handler); }
  private static void setField(Class<?> type, String name, Object value) throws Exception { Field field=type.getDeclaredField(name); field.setAccessible(true); field.set(null,value); }
}
