package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z04IncomeExpsDtl;


/**
 * @author  dzwiers
 */
public final class TestFipd04 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_04_v12_sample.csv");

    FileHandle i = Fipd04.read(f);
    assertNotNull(i);

    Z04IncomeExpsDtl o = (Z04IncomeExpsDtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 2477);


    assertEquals(new Integer(6810), o.getIncomeExpenseKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(702), o.getLineCode());
    assertEquals("I", o.getIe());
    assertEquals(new Double(5414), o.getAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_04_v12_sample.csv");

    FileHandle i = Fipd04.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_04_v12.csv");

    FileHandle i = Fipd04.read(f);
    assertNotNull(i);

    Z04IncomeExpsDtl o = (Z04IncomeExpsDtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 604760);


    assertEquals(new Integer(6810), o.getIncomeExpenseKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(702), o.getLineCode());
    assertEquals("I", o.getIe());
    assertEquals(new Double(5414), o.getAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_04_v12.csv");

    FileHandle i = Fipd04.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
