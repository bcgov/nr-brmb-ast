package ca.bc.gov.farms.api.rest.v1.resource;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ProductiveUnitCode;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.PRODUCTIVE_UNIT_CODE_NAME)
@XmlSeeAlso({ ProductiveUnitCodeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ProductiveUnitCodeRsrc extends BaseResource implements ProductiveUnitCode {

    private static final long serialVersionUID = 1L;

    private String code;
    private String description;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
