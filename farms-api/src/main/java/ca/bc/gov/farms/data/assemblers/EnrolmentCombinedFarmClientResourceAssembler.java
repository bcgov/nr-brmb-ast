package ca.bc.gov.farms.data.assemblers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.EnrolmentCombinedFarmClientEntity;
import ca.bc.gov.farms.data.models.EnrolmentCombinedFarmClientRsrc;
import ca.bc.gov.farms.data.models.EnrolmentPartnerListRsrc;

@Component
public class EnrolmentCombinedFarmClientResourceAssembler extends BaseResourceAssembler {

    public EnrolmentPartnerListRsrc addCombinedFarmClients(
            EnrolmentPartnerListRsrc result,
            List<EnrolmentCombinedFarmClientEntity> entities) {

        List<EnrolmentCombinedFarmClientRsrc> resources = entities.stream()
                .filter(Objects::nonNull)
                .map(entity -> {
                    EnrolmentCombinedFarmClientRsrc resource = new EnrolmentCombinedFarmClientRsrc();
                    BeanUtils.copyProperties(entity, resource);
                    return resource;
                })
                .collect(Collectors.toList());

        result.setCombinedFarmClientList(resources);
        result.setETag(getEtag(result));
        return result;
    }
}
