package ca.bc.gov.farms.data.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
public class ScenarioBenefitListRsrc extends BaseResource {
    private List<ScenarioBenefitRsrc> scenarioBenefitList;
}
