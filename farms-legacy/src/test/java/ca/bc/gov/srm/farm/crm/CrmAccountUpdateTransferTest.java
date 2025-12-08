package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.text.ParseException;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmAccountUpdateTransferTest {

  private static Logger logger = LoggerFactory.getLogger(CrmAccountUpdateTransferTest.class);

  private static CrmRestApiDao crmDao;
  
  private static ObjectMapper jsonObjectMapper = new ObjectMapper();

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
  public final void accountLookup() {
    
    int participantPin = 54832575;
    
    CrmAccountResource accountResource = null;
    try {
      accountResource = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected IOException");
    }
    assertNotNull(accountResource);
    
    logger.debug("accountResource: " + accountResource);
  }  

  @Test
  public final void accountUpdateTask() {
    
    // set up test data
    Client client = new Client();
    {
      Person owner = new Person();
      Person contact = new Person();
      client.setOwner(owner);
      client.setContact(contact);
      
      try {
        client.setIdentEffectiveDate(DataParseUtils.parseDate("07/31/2023"));
      } catch (ParseException e1) {
        fail("Error parsing identEffectiveDate");
      }
      assertNotNull(client.getIdentEffectiveDate());
      
      client.setParticipantClassCode(ParticipantClassCodes.INDIVIDUAL);
      client.setParticipantClassCodeDescription("Individual");
      client.setParticipantPin(4248530);
      client.setSin("123456789");
      
      owner.setAddressLine1("1234 Some Street");
      owner.setAddressLine2("#301");
      owner.setCity("Victoria");
      owner.setCorpName(null);
      owner.setCountry("CAN");
      owner.setDaytimePhone("250-888-2222");
      owner.setEveningPhone("250-777-1111");
      owner.setFaxNumber("250-555-3333");
      owner.setFirstName("Joe");
      owner.setLastName("Smith");
      owner.setPostalCode("V8T 5G5");
      owner.setProvinceState("BC");
      owner.setCellNumber("250-444-5555");
      owner.setEmailAddress("joe@smith.com");
      
      contact.setAddressLine1("1111 Venture Rd");
      contact.setAddressLine2("#101");
      contact.setCity("Steinbach");
      contact.setCorpName(null);
      contact.setCountry("CAN");
      contact.setDaytimePhone("250-444-9999");
      contact.setEveningPhone("250-222-0000");
      contact.setFaxNumber("250-111-6666");
      contact.setFirstName("Tom");
      contact.setLastName("Accountant");
      contact.setPostalCode("H3G 7D3");
      contact.setProvinceState("MB");
      contact.setCellNumber("250-333-6666");
      contact.setEmailAddress("tom@accountant.com");
    }
    
    CrmAccountResource accountResource = null;
    try {
      accountResource = crmDao.getAccountByPin(client.getParticipantPin());
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected IOException");
    }
    assertNotNull(accountResource);
    
    CrmTaskResource task = null;
    try {
      task = crmDao.createAccountUpdateTask(client, accountResource, null);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    
    assertNotNull(task);
    assertEquals("Account or contact update", task.getSubject());
    assertEquals("/accounts(54adafdb-717b-4249-b2b7-843e19fc421b)", task.getObjectIdTaskDataBind());
    assertEquals(
        "The CRA has received updated information for this account or its CRA contact.\n\n"
        + "ACCOUNT INFO\n"
        + "Identify Effective Date: 2023-07-31\n"
        + "Participant Type: Individual\n"
        + "Corporation Name: \n"
        + "First Name: Joe\n"
        + "Last Name: Smith\n"
        + "SIN: 123456789\n"
        + "Business Number: \n"
        + "Trust Number: \n"
        + "Address 1: 1234 Some Street\n"
        + "Address 2: #301\n"
        + "City: Victoria\n"
        + "Province: BC\n"
        + "Postal Code: V8T 5G5\n"
        + "Country: CAN\n"
        + "Phone (Fax): 250-555-3333\n"
        + "Phone (Day): 250-888-2222\n"
        + "Phone (Evening): 250-777-1111\n"
        + "Phone (Cell): 250-444-5555\n"
        + "Email Address: joe@smith.com\n"
        + "\n"
        + "CONTACT INFO\n"
        + "Business Name: \n"
        + "First Name: Tom\n"
        + "Last Name: Accountant\n"
        + "Address 1: 1111 Venture Rd\n"
        + "Address 2: #101\n"
        + "City: Steinbach\n"
        + "Province: MB\n"
        + "Postal Code: H3G 7D3\n"
        + "Country: CAN\n"
        + "Phone: 250-444-9999\n"
        + "Phone (Fax): 250-111-6666\n"
        + "Phone (Cell): 250-333-6666\n"
        + "Email Address: tom@accountant.com\n",
        task.getDescription());
    
    try {
      String json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(task);
      logger.debug("Task JSON: \n" + json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }
  }
  

  @Test
  public final void createAccount() {
    
    CalculatorService service = ServiceFactory.getCalculatorService();
    
    // set up test data
    Client client = new Client();
    {
      Person owner = new Person();
      Person contact = new Person();
      client.setOwner(owner);
      client.setContact(contact);
      
      try {
        client.setIdentEffectiveDate(DataParseUtils.parseDate("08/31/2023"));
      } catch (ParseException e) {
        e.printStackTrace();
        fail("Error parsing identEffectiveDate");
      }
      assertNotNull(client.getIdentEffectiveDate());
      
      client.setParticipantClassCode(ParticipantClassCodes.INDIVIDUAL);
      client.setParticipantClassCodeDescription("Individual");
      client.setSin("123456789");
      
      owner.setAddressLine1("4321 Other Street");
      owner.setAddressLine2("#301");
      owner.setCity("Victoria");
      owner.setCorpName(null);
      owner.setCountry("CAN");
      owner.setDaytimePhone("250-888-2222");
      owner.setEveningPhone("250-777-1111");
      owner.setFaxNumber("250-555-3333");
      owner.setFirstName("Dave");
      owner.setLastName("Chappelle");
      owner.setPostalCode("V8T 5G5");
      owner.setProvinceState("BC");
      owner.setCellNumber("250-444-5555");
      owner.setEmailAddress("dave@chappelle.com");
      
      contact.setAddressLine1("1111 Venture Rd");
      contact.setAddressLine2("#101");
      contact.setCity("Steinbach");
      contact.setCorpName(null);
      contact.setCountry("CAN");
      contact.setDaytimePhone("250-444-9999");
      contact.setEveningPhone("250-222-0000");
      contact.setFaxNumber("250-111-6666");
      contact.setFirstName("Jim");
      contact.setLastName("Bookkeeper");
      contact.setPostalCode("H3G 7D3");
      contact.setProvinceState("MB");
      contact.setCellNumber("250-333-6666");
      contact.setEmailAddress("jim@bookkeeper.com");
    }
    
    FarmingYear farmingYear = new FarmingYear();
    farmingYear.setMunicipalityCode("21");
    farmingYear.setMunicipalityCodeDescription("Nanaimo");
    
    // Generate random pins until we find one that doesn't exist.
    // Check both FARM database and CRM.
    Integer participantPin = null;
    try {
      
      boolean pinExists = false;
      do {
        final int min = 10000000;
        final int max = 99999999;
        
        participantPin = generateRandomPin(min, max);
        pinExists = service.pinExists(participantPin)
            || crmDao.getAccountByPin(participantPin) != null;
      }
      while(pinExists);
      
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Error checking if pin exists");
    }
    assertNotNull(participantPin);
    
    client.setParticipantPin(participantPin);
    
    try {
      CrmAccountResource account = crmDao.createAccount(client, farmingYear, null, null);
      assertNotNull(account);
   } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    
    CrmAccountResource createdAccount = null;
    try {
      createdAccount = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    
    assertNotNull(createdAccount);
    assertEquals(participantPin.toString(), createdAccount.getVsi_pin());
    assertEquals("Chappelle, Dave", createdAccount.getName());
    assertEquals("2023-08-31T00:00:00Z", createdAccount.getVsi_identityeffectivedate());
    assertEquals("Individual", createdAccount.getNew_participanttype());
    assertEquals("123456789", createdAccount.getVsi_socialinsurancenumber());
    assertNull(createdAccount.getVsi_businessnumber());
    assertEquals("Nanaimo", createdAccount.getNew_farmsmunicipality());
    assertEquals("250-888-2222", createdAccount.getTelephone1()); // Day Phone
    assertEquals("250-444-5555", createdAccount.getTelephone2()); // Cell Phone
    assertEquals( "dave@chappelle.com", createdAccount.getEmailaddress1());
    assertEquals("250-555-3333", createdAccount.getFax());
    assertEquals("4321 Other Street", createdAccount.getAddress1_line1());
    assertEquals("#301", createdAccount.getAddress1_line2());
    assertEquals("Victoria", createdAccount.getAddress1_city());
    assertEquals("BC", createdAccount.getAddress1_stateorprovince());
    assertEquals( "CAN", createdAccount.getAddress1_country());
    assertEquals( "V8T 5G5", createdAccount.getAddress1_postalcode());
    
    assertEquals( "Jim", createdAccount.getAddress2_name()); // First Name
    assertEquals("Bookkeeper", createdAccount.getAddress1_primarycontactname()); // Last Name
    assertNull(createdAccount.getVsi_company()); // Corporation Name
    assertEquals("250-444-9999", createdAccount.getAddress2_telephone1()); // Day Phone Number
    assertEquals("250-222-0000", createdAccount.getAddress2_telephone2()); // Evening Phone Number
    assertEquals("jim@bookkeeper.com", createdAccount.getEmailaddress2());
    assertEquals("250-111-6666", createdAccount.getAddress2_fax());
    assertEquals("1111 Venture Rd", createdAccount.getAddress2_line1());
    assertEquals("#101", createdAccount.getAddress2_line2());
    assertEquals("Steinbach", createdAccount.getAddress2_city());
    assertEquals("MB", createdAccount.getAddress2_stateorprovince());
    assertEquals("CAN", createdAccount.getAddress2_country());
    assertEquals("H3G 7D3", createdAccount.getAddress2_postalcode());
    
    try {
      String json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createdAccount);
      logger.debug("Account JSON: \n" + json);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      fail("Unexpected JsonProcessingException");
    }
  }
  

  @Test
  public final void findNonExistingPinInCRM() {
    
    CalculatorService service = ServiceFactory.getCalculatorService();
    
    // Generate random pins until we find one that doesn't exist.
    // Check both FARM database and CRM.
    Integer participantPin = null;
    try {
      
      boolean found = false;
      do {
        final int min = 3700000;
        final int max = 4000000;
        
        participantPin = generateRandomPin(min, max);
        found = service.pinExists(participantPin)
            && crmDao.getAccountByPin(participantPin) == null;
      }
      while(!found);
      
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Error checking if pin exists");
    }
    
    logger.debug("PIN exists in FARM but not in CRM: " + participantPin);
  }


  private int generateRandomPin(int min, int max) {
    int participantPin;
    participantPin = ThreadLocalRandom.current().nextInt(min, max + 1);
    return participantPin;
  }

}
