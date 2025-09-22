package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z02PartpntFarmInfo;


/**
 * @author  dzwiers
 */
public final class TestFipd02 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_02_v12_sample.csv");

    FileHandle i = Fipd02.read(f);
    assertNotNull(i);

    Z02PartpntFarmInfo o = (Z02PartpntFarmInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 79);


    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2005), o.getProgramYear());
    assertEquals(new Integer(2), o.getFormVersionNumber());

    assertEquals("BC", o.getProvinceOfResidence());
    assertEquals("BC", o.getProvinceOfMainFarmstead());
    assertEquals("20060525", o.getPostmarkDate());
    assertEquals("20060525", o.getReceivedDate());

    assertEquals(Boolean.FALSE, o.isSoleProprietor());
    assertEquals(Boolean.TRUE, o.isPartnershipMember());
    assertEquals(Boolean.FALSE, o.isCorporateShareholder());
    assertEquals(Boolean.FALSE, o.isCoopMember());

    assertEquals(null, o.getCommonShareTotal());
    assertEquals(new Integer(30), o.getFarmYears());
    assertEquals(Boolean.FALSE, o.isLastYearFarming());
    assertEquals(null, o.getFormOriginCode());
    assertEquals(new Integer(0), o.getIndustryCode());
    assertEquals(new Integer(3), o.getParticipantProfileCode());

    assertEquals(Boolean.FALSE, o.isAccrualCashConversion());
    assertEquals(Boolean.FALSE, o.isPerishableCommodities());
    assertEquals(Boolean.FALSE, o.isReceipts());
    assertEquals(Boolean.FALSE, o.isOtherText());
    assertEquals(" ", o.getOtherText());

    assertEquals(Boolean.FALSE, o.isAccrualWorksheet());
    assertEquals(Boolean.FALSE, o.isCwbWorksheet());
    assertEquals(Boolean.FALSE, o.isCombinedThisYear());
    assertEquals(Boolean.TRUE, o.isCompletedProdCycle());
    assertEquals(Boolean.FALSE, o.isDisaster());
    assertEquals(Boolean.FALSE, o.isCopyCobToContact());

    assertEquals(new Integer(0), o.getMunicipalityCode());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_02_v12_sample.csv");

    FileHandle i = Fipd02.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_02_v12.csv");

    FileHandle i = Fipd02.read(f);
    assertNotNull(i);

    Z02PartpntFarmInfo o = (Z02PartpntFarmInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 20159);


    assertEquals(new Integer(1976547), o.getParticipantPin());
    assertEquals(new Integer(2005), o.getProgramYear());
    assertEquals(new Integer(2), o.getFormVersionNumber());

    assertEquals("BC", o.getProvinceOfResidence());
    assertEquals("BC", o.getProvinceOfMainFarmstead());
    assertEquals("20060615", o.getPostmarkDate());
    assertEquals("20060615", o.getReceivedDate());

    assertEquals(Boolean.FALSE, o.isSoleProprietor());
    assertEquals(Boolean.TRUE, o.isPartnershipMember());
    assertEquals(Boolean.FALSE, o.isCorporateShareholder());
    assertEquals(Boolean.FALSE, o.isCoopMember());

    assertEquals(null, o.getCommonShareTotal());
    assertEquals(new Integer(34), o.getFarmYears());
    assertEquals(Boolean.FALSE, o.isLastYearFarming());
    assertEquals(null, o.getFormOriginCode());
    assertEquals(new Integer(0), o.getIndustryCode());
    assertEquals(new Integer(3), o.getParticipantProfileCode());

    assertEquals(Boolean.FALSE, o.isAccrualCashConversion());
    assertEquals(Boolean.FALSE, o.isPerishableCommodities());
    assertEquals(Boolean.FALSE, o.isReceipts());
    assertEquals(Boolean.FALSE, o.isOtherText());
    assertEquals(" ", o.getOtherText());

    assertEquals(Boolean.FALSE, o.isAccrualWorksheet());
    assertEquals(Boolean.FALSE, o.isCwbWorksheet());
    assertEquals(Boolean.FALSE, o.isCombinedThisYear());
    assertEquals(Boolean.TRUE, o.isCompletedProdCycle());
    assertEquals(Boolean.FALSE, o.isDisaster());
    assertEquals(Boolean.FALSE, o.isCopyCobToContact());

    assertEquals(new Integer(0), o.getMunicipalityCode());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_02_v12.csv");

    FileHandle i = Fipd02.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
