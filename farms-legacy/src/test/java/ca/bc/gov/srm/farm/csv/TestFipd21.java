package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z21ParticipantSuppl;


/**
 * @author  dzwiers
 */
public final class TestFipd21 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_21_v12_sample.csv");

    FileHandle i = Fipd21.read(f);
    assertNotNull(i);

    Z21ParticipantSuppl o = (Z21ParticipantSuppl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 188);


    assertEquals(new Integer(1801144), o.getInventoryKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(1), o.getInventoryTypeCode());
    assertEquals(new Integer(5195), o.getInventoryCode());
    assertEquals(new Integer(2), o.getCropUnitType());
    assertEquals(new Integer(50), o.getCropOnFarmAcres());
    assertEquals(new Double(87.7), o.getCropQtyProduced());
    assertEquals(new Double(65), o.getQuantityEnd());
    assertEquals(new Double(159), o.getEndOfYearPrice());
    assertEquals(null, o.getEndOfYearAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_21_v12_sample.csv");

    FileHandle i = Fipd21.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_21_v12.csv");

    FileHandle i = Fipd21.read(f);
    assertNotNull(i);

    Z21ParticipantSuppl o = (Z21ParticipantSuppl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 36903);


    assertEquals(new Integer(1801144), o.getInventoryKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(1), o.getInventoryTypeCode());
    assertEquals(new Integer(5195), o.getInventoryCode());
    assertEquals(new Integer(2), o.getCropUnitType());
    assertEquals(new Integer(50), o.getCropOnFarmAcres());
    assertEquals(new Double(87.7), o.getCropQtyProduced());
    assertEquals(new Double(65), o.getQuantityEnd());
    assertEquals(new Double(159), o.getEndOfYearPrice());
    assertEquals(null, o.getEndOfYearAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_21_v12.csv");

    FileHandle i = Fipd21.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
