package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z23LivestockProdCpct;


/**
 * @author  dzwiers
 */
public final class TestFipd23 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_23_v12_sample.csv");

    FileHandle i = Fipd23.read(f);
    assertNotNull(i);

    Z23LivestockProdCpct o = (Z23LivestockProdCpct) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 22);

    assertEquals(new Integer(99673), o.getProductiveCapacityKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());
    assertEquals(new Integer(104), o.getInventoryCode());
    assertEquals(new Double(77), o.getProductiveCapacityAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_23_v12_sample.csv");

    FileHandle i = Fipd23.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_23_v12.csv");

    FileHandle i = Fipd23.read(f);
    assertNotNull(i);

    Z23LivestockProdCpct o = (Z23LivestockProdCpct) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 2981);

    assertEquals(new Integer(99673), o.getProductiveCapacityKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());
    assertEquals(new Integer(104), o.getInventoryCode());
    assertEquals(new Double(77), o.getProductiveCapacityAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_23_v12.csv");

    FileHandle i = Fipd23.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
