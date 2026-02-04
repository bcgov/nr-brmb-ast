package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.bytecode.opencsv.CSVReader;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitUpdateResource;
import ca.bc.gov.srm.farm.crm.transform.BenefitUpdateTransformer;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmBenefitTransferTest {

  private static Logger logger = LoggerFactory.getLogger(CrmBenefitTransferTest.class);

  private static final String[] BENEFIT_FIELDS = {
      "4248530", // PIN
      "2021", // Program Year
      "COMP", // scenario state
      "2023-06-08 10:08:33", // state change date
      "ahopkins@vividsolutions.com", // verifier user ID
      "2023-03-01 0:00:00", // CRA supplemental received date
      "2021-02-17 16:18:57", // file start date
      "Fruit", // sector
      "Blueberries", // sector detail
      "457846.0", // benefit amount
      "4", // scenario number
      "Y", // partnership indicator
      "Y", // BPU set complete indicator
      "N", // FMV set complete indicator
      "Y", // combined farm indicator
      "123;456;567",  // combined farm PINs
      "Fraser Valley", // municipality
      "N", // program year non-participation indicator
      "FIN", // scenario category
      "0.75",  // interim benefit percent
      "252571.33", // allocatedReferenceMargin
      "640782.75", // negativeMarginDecline
      "448547.92", // negativeMarginBenefit
      "Y", // lateParticipant
      "65407.00", // bcFundedBenefitAmount
      "11000.00", // prodInsurDeemedBenefit
      "130813.00", // lateEnrolmentPenaltyAmount
      "2021-02-17 00:00:00", // localSupplementalReceivedDate
      "2021-02-20 00:00:00", // localStatementAReceivedDate
      "2021-02-25 00:00:00", // craStatementAReceivedDate
      "N", // sendCopyToContactPerson
      "I am applying for an interim benefit because bird flu killed all my chickens.", // chefsFormNotes
      "",  // formUserType
      "",  // submissionGuidString
      "Y",  // expectingPayment
      "SUPP",  // CHEFS form type,
      "Y", // Cash Margins Opt In
      "2024-12-15",
      "5001",
      "ZERO_PASS"
  };
  
  private static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  private static CrmRestApiDao crmDao;
  private static BenefitUpdateTransformer benefitTransformer;

  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    
    conn = TestUtils.openConnection();
    crmDao = new CrmRestApiDao();
    benefitTransformer = new BenefitUpdateTransformer();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }


  @Test
  public void transferFromFields() {

    CrmBenefitUpdateResource resource = null;
    try {
      resource = crmDao.createBenefitUpdate(BENEFIT_FIELDS);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    assertNotNull(resource);

    assertEquals("4248530 2021 FIN", resource.getVsi_batchnumber());
    assertEquals("4248530", resource.getVsi_pin());
    assertEquals("2021", resource.getVsi_programyear());
    assertEquals("COMP", resource.getVsi_farmsfilestate());
    assertEquals("2023-06-08T17:08:33Z", resource.getVsi_statechangedate());
    assertEquals("ahopkins@vividsolutions.com", resource.getVsi_verifieruserid());
    assertEquals("2023-03-01T08:00:00Z", resource.getVsi_supplementalreceiveddate());
    assertEquals("2021-02-18T00:18:57Z", resource.getVsi_filestartdate());
    assertEquals("Fruit", resource.getVsi_farmtype());
    assertEquals("Blueberries", resource.getVsi_farmtypedetailed());
    assertEquals(Double.valueOf(457846.0), resource.getVsi_benefitamount());
    assertEquals("4", resource.getVsi_scenarionumber());
    assertEquals(Boolean.TRUE, resource.getVsi_partnership());
    assertEquals(Boolean.TRUE, resource.getVsi_bpusetcomplete());
    assertEquals(Boolean.FALSE, resource.getVsi_fmvsetcomplete());
    assertEquals(Boolean.TRUE, resource.getVsi_combinedfarm());
    assertEquals("123\n456\n567", resource.getVsi_combinedfarmpins());
    assertEquals("Fraser Valley", resource.getVsi_municipality());
    assertEquals(Boolean.FALSE, resource.getVsi_isprogramyearnonparticipant());
    assertEquals("FIN", resource.getVsi_benefitcategory());
    assertEquals(Double.valueOf(0.75), resource.getVsi_interimbenefitpercent());
    assertEquals(Double.valueOf(252571.33), resource.getVsi_referencemarginforbenefitcalculation());
    assertEquals(Double.valueOf(640782.75), resource.getVsi_negativemarginmargindeclineamount());
    assertEquals(Double.valueOf(448547.92), resource.getVsi_negativemarginbenefitamount());
    assertEquals(Boolean.TRUE, resource.getVsi_fullyprovinciallyfunded());
    assertEquals(Double.valueOf(65407.0), resource.getVsi_provinciallyfundedamount());
    assertEquals(Double.valueOf(11000.0), resource.getVsi_deemedproductioninsurancemargindeclineamo());
    assertEquals(Double.valueOf(130813.0), resource.getVsi_lateparticipationpenaltyamount());
    assertEquals("2021-02-17T08:00:00Z", resource.getVsi_provincialsupplementalreceiveddate());
    assertEquals(Boolean.TRUE, resource.getVsi_expectingpayment());
    assertEquals(Boolean.TRUE, resource.getVsi_fifozeropass());

    String json = null;

    try {
      json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resource);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }

    logger.debug("Benefit JSON: \n" + json);
  }
  

  @Test
  public void verifiedBenefitTransferFromScenario() {
    int pin = 3693470;
    int year = 2019;
    Integer scenarioNumber = 4;
    String chefsFormNotes = "I am applying for an interim benefit because a dragon ate my cattle.";
    
    CalculatorService service = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    
    try {
      scenario = service.loadScenario(pin, year, scenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    assertNotNull(scenario);
    
    Date stateChangeDate = new Date(); // now
    String stateChangeDateString = CrmTransferFormatUtil.formatDate(stateChangeDate);
    
    String csvLine = null;
    try {
      String formUserType = null;
      String fifoResultType = null;
      
      csvLine = benefitTransformer.generateCsv(scenario, stateChangeDate, "test@test.com", chefsFormNotes, formUserType,
          ChefsFormTypeCodes.INTERIM, fifoResultType);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    
    CSVReader reader = new CSVReader(new StringReader(csvLine));
    String[] fields = null;
    try {
      fields = reader.readNext();
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected IOException");
    }
    assertNotNull(fields);
    
    CrmBenefitUpdateResource resource = null;
    try {
      resource = benefitTransformer.transformCsvToCrmResource(fields);
    } catch (ParseException e) {
      e.printStackTrace();
      fail("Unexpected ParseException");
    }

    assertNotNull(resource);
    assertEquals("3693470 2019 FIN", resource.getVsi_batchnumber());
    assertEquals("3693470", resource.getVsi_pin());
    assertEquals("2019", resource.getVsi_programyear());
    assertEquals("COMP", resource.getVsi_farmsfilestate());
    assertEquals(stateChangeDateString, resource.getVsi_statechangedate());
    assertEquals("test@test.com", resource.getVsi_verifieruserid());
    assertNull(resource.getVsi_supplementalreceiveddate());
    assertNotNull(resource.getVsi_filestartdate());
    assertTrue(resource.getVsi_filestartdate().startsWith("2020-02-13"));
    assertEquals("Tree Fruits", resource.getVsi_farmtype());
    assertEquals("", resource.getVsi_farmtypedetailed());
    assertEquals(Double.valueOf(55353.00), resource.getVsi_benefitamount());
    assertEquals("4", resource.getVsi_scenarionumber());
    assertEquals(Boolean.TRUE, resource.getVsi_partnership());
    assertEquals(Boolean.TRUE, resource.getVsi_bpusetcomplete());
    assertEquals(Boolean.FALSE, resource.getVsi_fmvsetcomplete());
    assertEquals(Boolean.FALSE, resource.getVsi_combinedfarm());
    assertNull(resource.getVsi_combinedfarmpins());
    assertEquals("Central Okanagan", resource.getVsi_municipality());
    assertEquals(Boolean.FALSE, resource.getVsi_isprogramyearnonparticipant());
    assertEquals("FIN", resource.getVsi_benefitcategory());
    assertNull(resource.getVsi_interimbenefitpercent());
    assertEquals(Double.valueOf(49992.46), resource.getVsi_referencemarginforbenefitcalculation());
    assertEquals(Double.valueOf(44081.11), resource.getVsi_negativemarginmargindeclineamount());
    assertEquals(Double.valueOf(30856.78), resource.getVsi_negativemarginbenefitamount());
    assertEquals(Boolean.FALSE, resource.getVsi_fullyprovinciallyfunded());
    assertEquals(Double.valueOf(7908.0), resource.getVsi_provinciallyfundedamount());
    assertNull(resource.getVsi_deemedproductioninsurancemargindeclineamo());
    assertNull(resource.getVsi_lateparticipationpenaltyamount());
    assertTrue(resource.getVsi_provincialsupplementalreceiveddate().startsWith("2020-09-01"));
    assertEquals(Boolean.FALSE, resource.getVsi_expectingpayment());
    assertEquals("4866,5054,4816,4826,4815", resource.getVsi_farmtypedetailedexpanded());
    assertEquals(Boolean.FALSE, resource.getVsi_fifozeropass());

    String json = null;

    try {
      json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resource);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }
    
    logger.debug("Benefit JSON: \n" + json);
  }


  @Disabled 
  @Test
 public void benefitTransferFromScenario() {
    int pin = 98765714;
    int year = 2023;
    Integer scenarioNumber = 2;
    
    CalculatorService service = ServiceFactory.getCalculatorService();
    Scenario scenario = null;
    
    try {
      scenario = service.loadScenario(pin, year, scenarioNumber);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    assertNotNull(scenario);
    
    Date stateChangeDate = new Date(); // now
    
    String chefsFormNotes = null;
    String formUserType = null;
    String chefsFormType = null;
//    chefsFormNotes = "I am applying for an interim benefit because locusts ate my crops.";
//    formUserType = "IDIR";
    
    String csvLine = null;
    try {
      String fifoResultType = null;
      csvLine = benefitTransformer.generateCsv(scenario, stateChangeDate, "test@test.com", chefsFormNotes, formUserType,
          chefsFormType, fifoResultType);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    
    CSVReader reader = new CSVReader(new StringReader(csvLine));
    String[] fields = null;
    try {
      fields = reader.readNext();
    } catch (IOException e) {
      e.printStackTrace();
      fail("Unexpected IOException");
    }
    assertNotNull(fields);
    
    CrmBenefitUpdateResource resource = null;
    try {
      resource = crmDao.createBenefitUpdate(fields);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    assertNotNull(resource);

    String json = null;

    try {
      json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resource);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }
    
    logger.debug("Benefit JSON: \n" + json);
  }
  
}
