/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.transform;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentUpdateResource;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentCombinedFarmOwner;
import ca.bc.gov.srm.farm.domain.enrolment.EnrolmentPartner;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class EnrolmentUpdateTransformer {

  public static final char DELIMITER = '\n';
  public static final String PERCENT_FORMAT_STRING = "#0.####";


  public CrmEnrolmentUpdateResource transformToCrmResource(Enrolment e, Integer importVersionId, String user) {
    CrmEnrolmentUpdateResource resource = new CrmEnrolmentUpdateResource();
    
    DecimalFormat percentFormat = new DecimalFormat(PERCENT_FORMAT_STRING);
    
    String feeModifiedByUser = getFeeModifiedByUser(user);
    
    List<EnrolmentCombinedFarmOwner> combinedFarmOwners = e.getCombinedFarmOwners();
    List<BigDecimal> combinedFarmPercents = new ArrayList<>();
    List<Integer> combinedFarmPins = new ArrayList<>();
    for (EnrolmentCombinedFarmOwner owner : combinedFarmOwners) {
      combinedFarmPercents.add(owner.getCombinedFarmPercent());
      combinedFarmPins.add(owner.getParticipantPin());
    }
    
    List<EnrolmentPartner> partners = e.getEnrolmentPartners();
    List<String> partnershipNames = new ArrayList<>();
    List<BigDecimal> partnershipPercents = new ArrayList<>();
    List<Integer> partnershipPins = new ArrayList<>();
    for(EnrolmentPartner partner : partners) {
      partnershipNames.add(partner.getPartnershipName());
      partnershipPercents.add(partner.getPartnershipPercent());
      partnershipPins.add(partner.getPartnershipPin());
    }
    
    resource.setVsi_batchnumber(importVersionId.toString());
    resource.setVsi_combinedfarmpercents(StringUtils.listToDelimitedString(DELIMITER, percentFormat, combinedFarmPercents));
    resource.setVsi_combinedfarmpins(StringUtils.listToDelimitedString(DELIMITER, null, combinedFarmPins));
    resource.setVsi_contributionmarginaverage(e.getContributionMarginAverage());
    resource.setVsi_createtaskinbarn(e.getIsCreateTaskInBarn());
    resource.setVsi_farmtype(e.getSectorCodeDescription());
    resource.setVsi_farmtypedetailed(e.getSectorDetailCodeDescription());
    resource.setVsi_fee(e.getEnrolmentFee());
    resource.setVsi_feemodifiedby(feeModifiedByUser);
    resource.setVsi_fullyprovinciallyfunded(e.getIsLateParticipant());
    resource.setVsi_generateddate(CrmTransferFormatUtil.formatDate(e.getGeneratedDate()));
    resource.setVsi_generatedfromenwscenario(e.getIsGeneratedFromEnw());
    resource.setVsi_haspartners(e.isPartnership());
    resource.setVsi_incombinedfarm(e.getIsInCombinedFarm());
    resource.setVsi_marginyearminus2(e.getMarginYearMinus2());
    resource.setVsi_marginyearminus3(e.getMarginYearMinus3());
    resource.setVsi_marginyearminus4(e.getMarginYearMinus4());
    resource.setVsi_marginyearminus5(e.getMarginYearMinus5());
    resource.setVsi_marginyearminus6(e.getMarginYearMinus6());
    resource.setVsi_marginyearminus2usedincalc(e.getIsMarginYearMinus2Used());
    resource.setVsi_marginyearminus3usedincalc(e.getIsMarginYearMinus3Used());
    resource.setVsi_marginyearminus4usedincalc(e.getIsMarginYearMinus4Used());
    resource.setVsi_marginyearminus5usedincalc(e.getIsMarginYearMinus5Used());
    resource.setVsi_marginyearminus6usedincalc(e.getIsMarginYearMinus6Used());
    resource.setVsi_partnershipnames(StringUtils.listToDelimitedString(DELIMITER, null, partnershipNames));
    resource.setVsi_partnershippercents(StringUtils.listToDelimitedString(DELIMITER, percentFormat, partnershipPercents));
    resource.setVsi_partnershippins(StringUtils.listToDelimitedString(DELIMITER, null, partnershipPins));
    resource.setVsi_pin(e.getPin().toString());
    resource.setVsi_programyear(e.getEnrolmentYear().toString());
    
    return resource;
  }


  private String getFeeModifiedByUser(String user) {
    ConfigurationUtility configUtil = ConfigurationUtility.getInstance();
    String feeModifiedByUserOverride = configUtil.getValue(ConfigurationKeys.ENROLMENT_FEE_MODIFIED_BY_USER_OVERRIDE);
    
    String feeModifiedByUser = user;
    if(feeModifiedByUserOverride != null && ! feeModifiedByUserOverride.equalsIgnoreCase("none")) {
      feeModifiedByUser = feeModifiedByUserOverride; // only for use in vivid test environments
    }
    return feeModifiedByUser;
  }

}
