package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z40PrtcpntRefSuplDtl;


/**
 * @author  dzwiers
 */
public final class TestFipd40 {

  @Disabled
  @Test
  public void test1() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_40_v12_sample.csv");

    FileHandle i = Fipd40.read(f);
    assertNotNull(i);

    Z40PrtcpntRefSuplDtl o = (Z40PrtcpntRefSuplDtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 660);

    assertEquals(new Integer(10534809), o.getPriorYearSupplementalKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(1), o.getInventoryTypeCode());
    assertEquals(new Integer(6445), o.getInventoryCode());
    assertEquals(new Integer(2), o.getProductionUnit());

    assertEquals(new Double(0), o.getQuantityStart());
    assertEquals(null, o.getStartingPrice());
    assertEquals(new Double(145), o.getCropOnFarmAcres());
    assertEquals(new Double(197.31), o.getCropQtyProduced());
    assertEquals(new Double(0), o.getQuantityEnd());
    assertEquals(new Double(0), o.getEndYearProducerPrice());
    assertEquals(Boolean.FALSE, o.isAcceptProducerPrice());
    assertEquals(new Double(83.93), o.getEndYearPrice());
    assertEquals(null, o.getAarmReferenceP1Price());
    assertEquals(null, o.getAarmReferenceP2Price());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_40_v12_sample.csv");

    FileHandle i = Fipd40.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws CSVParserException, IOException {
    File f = new File(".\\java\\test\\data\\fipd_as_40_v12.csv");

    FileHandle i = Fipd40.read(f);
    assertNotNull(i);

    Z40PrtcpntRefSuplDtl o = (Z40PrtcpntRefSuplDtl) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 139490);

    assertEquals(new Integer(10534809), o.getPriorYearSupplementalKey());
    assertEquals(new Integer(1531227), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(1), o.getInventoryTypeCode());
    assertEquals(new Integer(6445), o.getInventoryCode());
    assertEquals(new Integer(2), o.getProductionUnit());

    assertEquals(new Double(0), o.getQuantityStart());
    assertEquals(null, o.getStartingPrice());
    assertEquals(new Double(145), o.getCropOnFarmAcres());
    assertEquals(new Double(197.31), o.getCropQtyProduced());
    assertEquals(new Double(0), o.getQuantityEnd());
    assertEquals(new Double(0), o.getEndYearProducerPrice());
    assertEquals(Boolean.FALSE, o.isAcceptProducerPrice());
    assertEquals(new Double(83.93), o.getEndYearPrice());
    assertEquals(null, o.getAarmReferenceP1Price());
    assertEquals(null, o.getAarmReferenceP2Price());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_40_v12.csv");

    FileHandle i = Fipd40.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
