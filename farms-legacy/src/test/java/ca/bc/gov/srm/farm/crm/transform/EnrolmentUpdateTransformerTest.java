package ca.bc.gov.srm.farm.crm.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentUpdateResource;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentPartner;
import ca.bc.gov.srm.farm.util.TestUtils;

public class EnrolmentUpdateTransformerTest {

  private EnrolmentUpdateTransformer transformer = new EnrolmentUpdateTransformer();

  @BeforeAll
  protected static void setUp() throws Exception {
    TestUtils.standardTestSetUp();
  }

  @Test
  public void transformToCrmResourceDoesNotCountInvalidPartnerRows() {
    Enrolment enrolment = createBaseEnrolment();
    enrolment.setEnrolmentPartners(Arrays.asList(
        createPartner(null, null, null),
        createPartner("", null, 0),
        createPartner("null", null, null),
        createPartner("null, null", null, null)));

    CrmEnrolmentUpdateResource resource = transformer.transformToCrmResource(enrolment, 1234, "UNIT_TEST");

    assertEquals(Boolean.FALSE, resource.getVsi_haspartners());
    assertNull(resource.getVsi_partnershipnames());
    assertNull(resource.getVsi_partnershippercents());
    assertNull(resource.getVsi_partnershippins());
  }

  @Test
  public void transformToCrmResourceOnlySendsDisplayablePartnerRows() {
    Enrolment enrolment = createBaseEnrolment();
    enrolment.setEnrolmentPartners(Arrays.asList(
        createPartner("null", BigDecimal.valueOf(0.5), null),
        createPartner("PARTNER FARM", BigDecimal.valueOf(0.4), null),
        createPartner(null, BigDecimal.valueOf(0.6), 123456789)));

    CrmEnrolmentUpdateResource resource = transformer.transformToCrmResource(enrolment, 1234, "UNIT_TEST");

    assertEquals(Boolean.TRUE, resource.getVsi_haspartners());
    assertEquals("PARTNER FARM\n", resource.getVsi_partnershipnames());
    assertEquals("0.4\n0.6", resource.getVsi_partnershippercents());
    assertEquals("\n123456789", resource.getVsi_partnershippins());
  }

  private Enrolment createBaseEnrolment() {
    Enrolment enrolment = new Enrolment();
    enrolment.setPin(999999999);
    enrolment.setEnrolmentYear(2024);
    return enrolment;
  }

  private EnrolmentPartner createPartner(String name, BigDecimal percent, Integer pin) {
    EnrolmentPartner partner = new EnrolmentPartner();
    partner.setPartnershipName(name);
    partner.setPartnershipPercent(percent);
    partner.setPartnershipPin(pin);
    return partner;
  }
}