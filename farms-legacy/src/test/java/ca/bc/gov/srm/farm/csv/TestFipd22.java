package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z22ProductionInsurance;


/**
 * @author  dzwiers
 */
public final class TestFipd22 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_22_v12_sample.csv");

    FileHandle i = Fipd22.read(f);
    assertNotNull(i);

    Z22ProductionInsurance o = (Z22ProductionInsurance) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 3);

    assertEquals(new Integer(52786), o.getProductionInsuranceKey());
    assertEquals(new Integer(2351005), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());
    assertEquals("51062705", o.getProductionInsuranceNumber());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_22_v12_sample.csv");

    FileHandle i = Fipd22.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_22_v12.csv");

    FileHandle i = Fipd22.read(f);
    assertNotNull(i);

    Z22ProductionInsurance o = (Z22ProductionInsurance) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 747);

    assertEquals(new Integer(52786), o.getProductionInsuranceKey());
    assertEquals(new Integer(3112786), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());
    assertEquals("51062705", o.getProductionInsuranceNumber());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_22_v12.csv");

    FileHandle i = Fipd22.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
