package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z50ParticipntBnftCalc;


/**
 * @author  dzwiers
 */
public final class TestFipd50 {

  @Disabled
  @Test
  public void test1() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_50_v12_sample.csv");

    FileHandle i = Fipd50.read(f);
    assertNotNull(i);

    Z50ParticipntBnftCalc o = (Z50ParticipntBnftCalc) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 22);

    assertEquals(new Integer(108951), o.getBenefitCalcKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getAgristabilityStatus());

    assertEquals(new Double(0), o.getUnadjustedReferenceMargin());
    assertEquals(new Double(0), o.getAdjustedReferenceMargin());
    assertEquals(new Double(0), o.getProgramMargin());
    assertEquals(Boolean.FALSE, o.isWholeFarm());
    assertEquals(Boolean.FALSE, o.isStructureChange());
    assertEquals(null, o.getStructureChangeAdjAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_50_v12_sample.csv");

    FileHandle i = Fipd50.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_50_v12.csv");

    FileHandle i = Fipd50.read(f);
    assertNotNull(i);

    Z50ParticipntBnftCalc o = (Z50ParticipntBnftCalc) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 5238);

    assertEquals(new Integer(108951), o.getBenefitCalcKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());
    assertEquals(new Integer(1), o.getAgristabilityStatus());

    assertEquals(new Double(0), o.getUnadjustedReferenceMargin());
    assertEquals(new Double(0), o.getAdjustedReferenceMargin());
    assertEquals(new Double(0), o.getProgramMargin());
    assertEquals(Boolean.FALSE, o.isWholeFarm());
    assertEquals(Boolean.FALSE, o.isStructureChange());
    assertEquals(null, o.getStructureChangeAdjAmount());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_50_v12.csv");

    FileHandle i = Fipd50.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
