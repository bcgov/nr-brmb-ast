package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z42ParticipantRefYear;


/**
 * @author  dzwiers
 */
public final class TestFipd42 {

  @Disabled
  @Test
  public void test1() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_42_v12_sample.csv");

    FileHandle i = Fipd42.read(f);
    assertNotNull(i);

    Z42ParticipantRefYear o = (Z42ParticipantRefYear) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 74);

    assertEquals(new Integer(374431), o.getProductiveCapacityKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getRefOperationNumber());

    assertEquals(new Integer(2), o.getProductiveTypeCode());
    assertEquals(new Integer(104), o.getProductiveCode());
    assertEquals(new Double(24), o.getProductiveCapacityUnits());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_42_v12_sample.csv");

    FileHandle i = Fipd42.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_42_v12.csv");

    FileHandle i = Fipd42.read(f);
    assertNotNull(i);

    Z42ParticipantRefYear o = (Z42ParticipantRefYear) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 14700);

    assertEquals(new Integer(369528), o.getProductiveCapacityKey());
    assertEquals(new Integer(23012693), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getRefOperationNumber());

    assertEquals(new Integer(2), o.getProductiveTypeCode());
    assertEquals(new Integer(104), o.getProductiveCode());
    assertEquals(new Double(32), o.getProductiveCapacityUnits());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_42_v12.csv");

    FileHandle i = Fipd42.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
