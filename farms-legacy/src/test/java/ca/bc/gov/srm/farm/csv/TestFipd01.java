package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z01ParticipantInfo;


/**
 * @author  dzwiers
 */
public final class TestFipd01 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_01_v12_sample.csv");

    FileHandle i = Fipd01.read(f);
    assertNotNull(i);

    Z01ParticipantInfo o = (Z01ParticipantInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 22);


    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals("627314149", o.getSinCtnBn());
    assertEquals("JO ANNE", o.getFirstName());
    assertEquals("DELICHTE", o.getLastName());
    assertEquals("", o.getCorpName());
    assertEquals("7001 HIGHWAY 6", o.getAddress1());
    assertEquals("", o.getAddress2());
    assertEquals("COLDSTREAM", o.getCity());
    assertEquals("BC", o.getProvince());
    assertEquals("V1B3H1", o.getPostalCode());
    assertEquals("CAN", o.getCountry());
    assertEquals(new Integer(1), o.getParticipantTypeCode());
    assertEquals(new Integer(1), o.getParticipantLanguage());
    assertEquals("", o.getParticipantFax());
    assertEquals("2502605299", o.getParticipantPhoneDay());
    assertEquals("2502605299", o.getParticipantPhoneEvening());
    assertEquals("", o.getContactFirstName());
    assertEquals("DELICHTE", o.getContactLastName());
    assertEquals("", o.getContactBusinessName());
    assertEquals("", o.getContactAddress1());
    assertEquals("", o.getContactAddress2());
    assertEquals("", o.getContactCity());
    assertEquals("", o.getContactProvince());
    assertEquals("", o.getContactPostalCode());
    assertEquals("", o.getContactPhoneDay());
    assertEquals("", o.getContactFaxNumber());
    assertEquals(new Integer(2), o.getPublicOffice());

    i.close();
  }

  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_01_v12_sample.csv");

    FileHandle i = Fipd01.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_01_v12.csv");

    FileHandle i = Fipd01.read(f);
    assertNotNull(i);

    Z01ParticipantInfo o = (Z01ParticipantInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 5238);


    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals("627314149", o.getSinCtnBn());
    assertEquals("JO ANNE", o.getFirstName());
    assertEquals("DELICHTE", o.getLastName());
    assertEquals("", o.getCorpName());
    assertEquals("7001 HIGHWAY 6", o.getAddress1());
    assertEquals("", o.getAddress2());
    assertEquals("COLDSTREAM", o.getCity());
    assertEquals("BC", o.getProvince());
    assertEquals("V1B3H1", o.getPostalCode());
    assertEquals("CAN", o.getCountry());
    assertEquals(new Integer(1), o.getParticipantTypeCode());
    assertEquals(new Integer(1), o.getParticipantLanguage());
    assertEquals("", o.getParticipantFax());
    assertEquals("2502605299", o.getParticipantPhoneDay());
    assertEquals("2502605299", o.getParticipantPhoneEvening());
    assertEquals("", o.getContactFirstName());
    assertEquals("DELICHTE", o.getContactLastName());
    assertEquals("", o.getContactBusinessName());
    assertEquals("", o.getContactAddress1());
    assertEquals("", o.getContactAddress2());
    assertEquals("", o.getContactCity());
    assertEquals("", o.getContactProvince());
    assertEquals("", o.getContactPostalCode());
    assertEquals("", o.getContactPhoneDay());
    assertEquals("", o.getContactFaxNumber());
    assertEquals(new Integer(2), o.getPublicOffice());

    i.close();
  }

  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_01_v12.csv");

    FileHandle i = Fipd01.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

}
