package ca.bc.gov.farms.data.assemblers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.farms.data.entities.ScenarioBenefitEntity;
import ca.bc.gov.farms.data.models.ScenarioBenefitListRsrc;
import ca.bc.gov.farms.data.models.ScenarioBenefitRsrc;

@Component
public class ScenarioBenefitResourceAssembler extends BaseResourceAssembler {

    public ScenarioBenefitListRsrc assemble(List<ScenarioBenefitEntity> entities) {
        ScenarioBenefitListRsrc result = new ScenarioBenefitListRsrc();
        result.setScenarioBenefitList(entities.stream().map(entity -> {
            ScenarioBenefitRsrc resource = new ScenarioBenefitRsrc();
            BeanUtils.copyProperties(entity, resource);
            return resource;
        }).toList());
        result.setETag(getEtag(result));
        String selfUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        result.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
        return result;
    }
}
