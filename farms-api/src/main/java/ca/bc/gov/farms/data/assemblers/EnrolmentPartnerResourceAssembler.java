package ca.bc.gov.farms.data.assemblers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.farms.data.entities.EnrolmentPartnerEntity;
import ca.bc.gov.farms.data.entities.EnrolmentPartnerSummaryEntity;
import ca.bc.gov.farms.data.models.EnrolmentPartnerListRsrc;
import ca.bc.gov.farms.data.models.EnrolmentPartnerRsrc;

@Component
public class EnrolmentPartnerResourceAssembler extends BaseResourceAssembler {

    public EnrolmentPartnerListRsrc getEnrolmentPartnerList(
            Integer participantPin,
            Integer programYear,
            EnrolmentPartnerSummaryEntity summary,
            List<EnrolmentPartnerEntity> entities) {

        @SuppressWarnings("null")
        List<EnrolmentPartnerRsrc> resources = entities.stream()
                .filter(Objects::nonNull)
                .map(entity -> {
                    EnrolmentPartnerRsrc resource = new EnrolmentPartnerRsrc();
                    BeanUtils.copyProperties(entity, resource);
                    return resource;
                })
                .collect(Collectors.toList());

        EnrolmentPartnerListRsrc result = new EnrolmentPartnerListRsrc();
        result.setParticipantPin(participantPin);
        result.setProgramYear(programYear);
        if (summary != null) {
            result.setAgristabilityScenarioId(summary.getAgristabilityScenarioId());
            result.setScenarioNumber(summary.getScenarioNumber());
            result.setInCombinedFarm(summary.getInCombinedFarm());
            result.setCombinedFarmNumber(summary.getCombinedFarmNumber());
            result.setCombinedFarmPercent(summary.getCombinedFarmPercent());
        }
        result.setEnrolmentPartnerList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setCalculationSelfLink(result);

        return result;
    }

    private void setCalculationSelfLink(EnrolmentPartnerListRsrc resource) {
        String selfUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }
}
