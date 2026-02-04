package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z28ProdInsuranceRef;


/**
 * @author  dzwiers
 */
public final class TestFipd28 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_28_v12.csv");

    FileHandle i = Fipd28.read(f);
    assertNotNull(i);

    Z28ProdInsuranceRef o = (Z28ProdInsuranceRef) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 19);

    assertEquals(new Integer(1), o.getProductionUnit());
    assertEquals("Pounds", o.getProductionUnitDescription());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_28_v12.csv");

    FileHandle i = Fipd28.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
