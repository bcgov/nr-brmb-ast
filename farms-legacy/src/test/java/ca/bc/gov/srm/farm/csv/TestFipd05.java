package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z05PartnerInfo;


/**
 * @author  dzwiers
 */
public final class TestFipd05 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_05_v12_sample.csv");

    FileHandle i = Fipd05.read(f);
    assertNotNull(i);

    Z05PartnerInfo o = (Z05PartnerInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 108);


    assertEquals(new Integer(646025), o.getPartnerInfoKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(12024782), o.getPartnershipPin());
    assertEquals("DANIEL L", o.getPartnerFirstName());
    assertEquals("DELICHTE", o.getPartnerLastName());
    assertEquals("", o.getPartnerCorpName().trim());
    assertEquals("624476941", o.getPartnerSinCtnBn());
    assertEquals(new Double(0.5), o.getPartnerPercent());
    assertEquals(new Integer(22339824), o.getPartnerPin());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_05_v12_sample.csv");

    FileHandle i = Fipd05.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_05_v12.csv");

    FileHandle i = Fipd05.read(f);
    assertNotNull(i);

    Z05PartnerInfo o = (Z05PartnerInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 19066);


    assertEquals(new Integer(646025), o.getPartnerInfoKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(12024782), o.getPartnershipPin());
    assertEquals("DANIEL L", o.getPartnerFirstName());
    assertEquals("DELICHTE", o.getPartnerLastName());
    assertEquals("", o.getPartnerCorpName().trim());
    assertEquals("624476941", o.getPartnerSinCtnBn());
    assertEquals(new Double(0.5), o.getPartnerPercent());
    assertEquals(new Integer(22339824), o.getPartnerPin());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_05_v12.csv");

    FileHandle i = Fipd05.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
