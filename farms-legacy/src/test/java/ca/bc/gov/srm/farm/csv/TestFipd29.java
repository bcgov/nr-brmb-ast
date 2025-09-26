package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z29InventoryRef;


/**
 * @author  dzwiers
 */
public final class TestFipd29 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_29_v12.csv");

    FileHandle i = Fipd29.read(f);
    assertNotNull(i);

    Z29InventoryRef o = (Z29InventoryRef) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 2399);

    assertEquals(new Integer(2), o.getInventoryCode());
    assertEquals("Canadian Wheat Board Payments", o.getInventoryDesc());
    assertEquals(new Integer(5), o.getInventoryTypeCode());
    assertEquals("Accounts Payable", o.getInventoryTypeDescription());
    assertEquals(null, o.getInventoryGroupCode());
    assertEquals("", o.getInventoryGroupDescription());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_29_v12.csv");

    FileHandle i = Fipd29.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
