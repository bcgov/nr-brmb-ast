package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmEnrolmentLookupTest {

  private static Logger logger = LoggerFactory.getLogger(CrmEnrolmentLookupTest.class);

  private static CrmRestApiDao crmDao;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();

    crmDao = new CrmRestApiDao();
  }

  @Test
  public final void checkEnrolledForYear() {

    Integer participantPin = 4375675;
    Integer year = 2025;

    CrmProgramYearResource crmProgramYear = null;
    try {
      crmProgramYear = crmDao.getProgramYear(year);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    logger.debug("programYearResource: " + crmProgramYear);

    assertNotNull(crmProgramYear);
    String vsi_programyearid = crmProgramYear.getVsi_programyearid();
    assertNotNull(vsi_programyearid);
    assertEquals(year, crmProgramYear.getVsiYear());

    CrmAccountResource crmAccount = null;
    try {
      crmAccount = crmDao.getAccountByPin(participantPin);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected IOException");
    }
    logger.debug("accountResource: " + crmAccount);

    assertNotNull(crmAccount);
    String accountId = crmAccount.getAccountid();
    assertNotNull(accountId);
    assertEquals(participantPin.toString(), crmAccount.getVsi_pin());

    CrmEnrolmentResource crmEnrolment = null;
    try {
      crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
    } catch (ServiceException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
    logger.debug("participantProgramYearResource: " + crmEnrolment);

    assertNotNull(crmEnrolment);
    String vsi_participantprogramyearid = crmEnrolment.getVsi_participantprogramyearid();
    assertNotNull(vsi_participantprogramyearid);
    assertEquals(accountId, crmEnrolment.get_vsi_participantid_value());
    assertNotNull(crmEnrolment.getEnrolmentStatusCode());
    assertEquals(CrmConstants.ENROLMENT_STATUS_CODE_ENROLLED, crmEnrolment.getEnrolmentStatusCode().intValue());
    CrmProgramYearResource vsiProgramYear = crmEnrolment.getVsiProgramYear();
    assertNull(vsiProgramYear);
    // assertEquals(year, vsiProgramYear.getVsiYear());

  }

}
