package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z51ParticipantContrib;


/**
 * @author  dzwiers
 */
public final class TestFipd51 {

  @Disabled
  @Test
  public void test1() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_51_v12_sample.csv");

    FileHandle i = Fipd51.read(f);
    assertNotNull(i);

    Z51ParticipantContrib o = (Z51ParticipantContrib) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 22);

    assertEquals(new Integer(130462), o.getContributionKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());

    assertEquals(new Double(0), o.getProvincialContributions());
    assertEquals(new Double(0), o.getFederalContributions());
    assertEquals(new Double(0), o.getInterimContributions());
    assertEquals(new Double(0), o.getProducerShare());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_51_v12_sample.csv");

    FileHandle i = Fipd51.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_51_v12.csv");

    FileHandle i = Fipd51.read(f);
    assertNotNull(i);

    Z51ParticipantContrib o = (Z51ParticipantContrib) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 5238);

    assertEquals(new Integer(130462), o.getContributionKey());
    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2007), o.getProgramYear());

    assertEquals(new Double(0), o.getProvincialContributions());
    assertEquals(new Double(0), o.getFederalContributions());
    assertEquals(new Double(0), o.getInterimContributions());
    assertEquals(new Double(0), o.getProducerShare());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_51_v12.csv");

    FileHandle i = Fipd51.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
