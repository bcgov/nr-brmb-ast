package ca.bc.gov.srm.farm.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.domain.staging.Z03StatementInfo;


/**
 * @author  dzwiers
 */
public final class TestFipd03 {

  @Disabled
  @Test
  public void test1() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_03_v12_sample.csv");

    FileHandle i = Fipd03.read(f);
    assertNotNull(i);

    Z03StatementInfo o = (Z03StatementInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 112);


    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(12024782), o.getPartnershipPin());
    assertEquals("", o.getPartnershipName());
    assertEquals(new Double(0.5), o.getPartnershipPercent());
    assertEquals("20030101", o.getFiscalYearStart());
    assertEquals("20031231", o.getFiscalYearEnd());
    assertEquals(new Integer(2), o.getAccountingCode());

    assertEquals(Boolean.FALSE, o.isLandlord());
    assertEquals(Boolean.FALSE, o.isCropShare());
    assertEquals(Boolean.FALSE, o.isFeederMember());
    assertEquals(new Double(104410), o.getGrossIncome());
    assertEquals(new Double(144067), o.getExpenses());
    assertEquals(new Double(-39657), o.getNetIncomeBeforeAdj());
    assertEquals(new Double(0), o.getOtherDeductions());
    assertEquals(new Double(30000), o.getInventoryAdjustments());
    assertEquals(new Double(-4829), o.getNetIncomeAfterAdj());
    assertEquals(new Double(0), o.getBusinessUseOfHomeExpenses());
    assertEquals(new Double(-4829), o.getNetFarmIncome());
    assertEquals(Boolean.FALSE, o.isCropDisaster());
    assertEquals(Boolean.FALSE, o.isLivestockDisaster());

    i.close();
  }


  @Disabled
  @Test
  public void test2() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_03_v12_sample.csv");

    FileHandle i = Fipd03.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }

  @Disabled
  @Test
  public void test3() throws IOException, CSVParserException {
    File f = new File(".\\java\\test\\data\\fipd_as_03_v12.csv");

    FileHandle i = Fipd03.read(f);
    assertNotNull(i);

    Z03StatementInfo o = (Z03StatementInfo) i.next();
    assertNotNull(o);
    assertNotNull(i.next());

    int c = 2;

    while (i.hasNext()) {
      i.next();
      c++;
    }

    assertTrue(c == 24099);


    assertEquals(new Integer(1467067), o.getParticipantPin());
    assertEquals(new Integer(2003), o.getProgramYear());
    assertEquals(new Integer(1), o.getOperationNumber());

    assertEquals(new Integer(12024782), o.getPartnershipPin());
    assertEquals("", o.getPartnershipName());
    assertEquals(new Double(0.5), o.getPartnershipPercent());
    assertEquals("20030101", o.getFiscalYearStart());
    assertEquals("20031231", o.getFiscalYearEnd());
    assertEquals(new Integer(2), o.getAccountingCode());

    assertEquals(Boolean.FALSE, o.isLandlord());
    assertEquals(Boolean.FALSE, o.isCropShare());
    assertEquals(Boolean.FALSE, o.isFeederMember());
    assertEquals(new Double(104410), o.getGrossIncome());
    assertEquals(new Double(144067), o.getExpenses());
    assertEquals(new Double(-39657), o.getNetIncomeBeforeAdj());
    assertEquals(new Double(0), o.getOtherDeductions());
    assertEquals(new Double(30000), o.getInventoryAdjustments());
    assertEquals(new Double(-4829), o.getNetIncomeAfterAdj());
    assertEquals(new Double(0), o.getBusinessUseOfHomeExpenses());
    assertEquals(new Double(-4829), o.getNetFarmIncome());
    assertEquals(Boolean.FALSE, o.isCropDisaster());
    assertEquals(Boolean.FALSE, o.isLivestockDisaster());

    i.close();
  }


  @Disabled
  @Test
  public void test4() throws CSVParserException {

    File f = new File(".\\java\\test\\data\\fipd_as_03_v12.csv");

    FileHandle i = Fipd03.read(f);
    assertNotNull(i);

    assertNull(i.validate());
  }
}
