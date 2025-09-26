package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentUpdateResource;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentCombinedFarmOwner;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentPartner;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmEnrolmentTransferTest {

  private static Logger logger = LoggerFactory.getLogger(CrmEnrolmentTransferTest.class);

  private static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  private static CrmRestApiDao crmDao;
  
  private static Connection conn;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
    
    conn = TestUtils.openConnection();
    crmDao = new CrmRestApiDao();
  }

  @AfterAll
  protected static void tearDown() throws Exception {
    if (conn != null) {
      conn.close();
    }
  }


  @Test
  public void enrolmentTransfer() {
    
    Date generateDate = null;
    try {
      generateDate = DataParseUtils.parseDate("2023-07-31");
    } catch (ParseException e1) {
      fail("Unexpected ParseException");
    }
    assertNotNull(generateDate);
    
    Integer importVersionId = 1234;
    
    Enrolment e = new Enrolment();

    List<EnrolmentCombinedFarmOwner> combinedFarmOwners = new ArrayList<>();
    {
      EnrolmentCombinedFarmOwner owner1 = new EnrolmentCombinedFarmOwner();
      owner1.setEnrolment(e);
      owner1.setParticipantPin(12345678);
      owner1.setCombinedFarmPercent(BigDecimal.valueOf(0.3));
      combinedFarmOwners.add(owner1);
    }
    {
      EnrolmentCombinedFarmOwner owner2 = new EnrolmentCombinedFarmOwner();
      owner2.setEnrolment(e);
      owner2.setParticipantPin(12344444);
      owner2.setCombinedFarmPercent(BigDecimal.valueOf(0.7));
      combinedFarmOwners.add(owner2);
    }
    
    List<EnrolmentPartner> partners = new ArrayList<>();
    {
      EnrolmentPartner partner = new EnrolmentPartner();
      partner.setEnrolment(e);
      partner.setPartnershipName("VENTURE BROTHERS");
      partner.setPartnershipPercent(BigDecimal.valueOf(0.6));
      partner.setPartnershipPin(888888888);
      partners.add(partner);
    }
    {
      EnrolmentPartner partner = new EnrolmentPartner();
      partner.setEnrolment(e);
      partner.setPartnershipName("VENTURE BROTHERS");
      partner.setPartnershipPercent(BigDecimal.valueOf(0.4));
      partner.setPartnershipPin(777777777);
      partners.add(partner);
    }
    
    e.setPin(12345678);
    e.setProducerName("BOLD VENTURES");
    e.setScenarioState("COMP");
    e.setFailedToGenerate(false);
    e.setFailedReason(null);
    e.setEnrolmentYear(2012);
    e.setEnrolmentFee(49.99);
    e.setGeneratedDate(generateDate);
    e.setIsGeneratedFromCra(false);
    e.setIsGeneratedFromEnw(false);
    e.setCombinedFarmPercent(0.34);
    e.setContributionMarginAverage(1000.0);
    e.setIsCreateTaskInBarn(true);
    e.setSectorCodeDescription("Fruit");
    e.setSectorDetailCodeDescription("Apples");
    e.setIsLateParticipant(false);
    e.setIsInCombinedFarm(true);
    e.setMarginYearMinus2(800.0);
    e.setMarginYearMinus3(900.0);
    e.setMarginYearMinus4(1000.0);
    e.setMarginYearMinus5(1100.0);
    e.setMarginYearMinus6(1200.0);
    e.setIsMarginYearMinus2Used(false);
    e.setIsMarginYearMinus3Used(true);
    e.setIsMarginYearMinus4Used(true);
    e.setIsMarginYearMinus5Used(true);
    e.setIsMarginYearMinus6Used(false);
    e.setCombinedFarmOwners(combinedFarmOwners);
    e.setEnrolmentPartners(partners);
    
    String feeModifiedByUser = "msargeant@vividsolutions.com";
    
    CrmEnrolmentUpdateResource resource = null;
    try {
      resource = crmDao.createEnrolmentUpdate(e, importVersionId, feeModifiedByUser);
    } catch (ServiceException e1) {
      e1.printStackTrace();
      fail("Unexpected ServiceException");
    }

    assertNotNull(resource);
    assertEquals("0.3\n0.7", resource.getVsi_combinedfarmpercents());
    assertEquals("12345678\n12344444", resource.getVsi_combinedfarmpins());
    assertEquals("VENTURE BROTHERS\nVENTURE BROTHERS", resource.getVsi_partnershipnames());
    assertEquals("0.6\n0.4", resource.getVsi_partnershippercents());
    assertEquals("888888888\n777777777", resource.getVsi_partnershippins());
    assertEquals(importVersionId.toString(), resource.getVsi_batchnumber());
    assertEquals("12345678", resource.getVsi_pin());
    assertEquals("2012", resource.getVsi_programyear());
    assertEquals(Double.valueOf(49.99), resource.getVsi_fee());
    assertEquals("2023-07-31 0:00:00-0700", resource.getVsi_generateddate());
    assertEquals(Boolean.FALSE, resource.getVsi_generatedfromenwscenario());
    assertEquals(Double.valueOf(1000.0), resource.getVsi_contributionmarginaverage());
    assertEquals(Boolean.TRUE, resource.getVsi_createtaskinbarn());
    assertEquals("Fruit", resource.getVsi_farmtype());
    assertEquals("Apples", resource.getVsi_farmtypedetailed());
    assertEquals(Boolean.FALSE, resource.getVsi_fullyprovinciallyfunded());
    assertEquals(Boolean.TRUE, resource.getVsi_incombinedfarm());
    assertEquals(Double.valueOf(800.0), resource.getVsi_marginyearminus2());
    assertEquals(Double.valueOf(900.0), resource.getVsi_marginyearminus3());
    assertEquals(Double.valueOf(1000.0), resource.getVsi_marginyearminus4());
    assertEquals(Double.valueOf(1100.0), resource.getVsi_marginyearminus5());
    assertEquals(Double.valueOf(1200.0), resource.getVsi_marginyearminus6());
    assertEquals(Boolean.FALSE, resource.getVsi_marginyearminus2usedincalc());
    assertEquals(Boolean.TRUE, resource.getVsi_marginyearminus3usedincalc());
    assertEquals(Boolean.TRUE, resource.getVsi_marginyearminus4usedincalc());
    assertEquals(Boolean.TRUE, resource.getVsi_marginyearminus5usedincalc());
    assertEquals(Boolean.FALSE, resource.getVsi_marginyearminus6usedincalc());
    
    String json = null;

    try {
      json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resource);
    } catch (JsonProcessingException ex) {
      ex.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }
    
    logger.debug("Enrolment JSON: \n" + json);
  }
}
