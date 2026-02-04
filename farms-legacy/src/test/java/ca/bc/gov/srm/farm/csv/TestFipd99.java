package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z99ExtractFileCtl;


/**
 * @author  dzwiers
 */
public final class TestFipd99 {

  @Disabled
  @Test
  public void test1() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_99_v12_sample.csv");

    FileHandle i = Fipd99.read(f);
    assertNotNull(i);

    Z99ExtractFileCtl o = (Z99ExtractFileCtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 16);

    assertEquals("20090618", o.getExtractDate());
    assertEquals(new Integer(1), o.getExtractFileNumber());
    assertEquals(new Integer(22), o.getRowCount());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_99_v12_sample.csv");

    FileHandle i = Fipd99.read(f);
    assertNotNull(i);

    String[] s = i.validate();
    assertNull(s);
    System.out.println(s);
  }

  @Disabled
  @Test
  public void test3() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_99_v12.csv");

    FileHandle i = Fipd99.read(f);
    assertNotNull(i);

    Z99ExtractFileCtl o = (Z99ExtractFileCtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 16);

    assertEquals("20090618", o.getExtractDate());
    assertEquals(new Integer(1), o.getExtractFileNumber());
    assertEquals(new Integer(5238), o.getRowCount());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_99_v12.csv");

    FileHandle i = Fipd99.read(f);
    assertNotNull(i);


    String[] s = i.validate();
    assertNull(s);
    System.out.println(s);
  }
}
