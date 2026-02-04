package ca.bc.gov.srm.farm.crm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.crm.resource.CrmBenefitResource;
import ca.bc.gov.srm.farm.crm.resource.CrmListResource;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.TestUtils;

public class CrmBenefitTest {

  private static Logger logger = LoggerFactory.getLogger(CrmBenefitTest.class);

  private static CrmRestApiDao crmDao;

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();

    crmDao = new CrmRestApiDao();
  }

  @Test
  public final void benefitLookup() {
    String accountId = "07b25a95-e0b2-4af9-95b7-2aec1c15095b";

    try {
      CrmListResource<CrmBenefitResource> listResource = crmDao.getBenefitsByAccountId(accountId);

      List<CrmBenefitResource> benefits = listResource.getList();
      for (CrmBenefitResource benefit : benefits) {
        logger.debug(benefit.toString());
        assertEquals(accountId, benefit.getVsi_participantid_value());
      }
    } catch (ServiceException | IOException e) {
      e.printStackTrace();
      fail("Unexpected ServiceException");
    }
  }

}
