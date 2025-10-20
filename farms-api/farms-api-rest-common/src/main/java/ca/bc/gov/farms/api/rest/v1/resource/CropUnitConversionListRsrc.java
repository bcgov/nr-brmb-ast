package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.CropUnitConversionList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_UNIT_CONVERSION_LIST_NAME)
@XmlSeeAlso({ CropUnitConversionListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class CropUnitConversionListRsrc extends BaseResource implements CropUnitConversionList<CropUnitConversionRsrc> {

    private static final long serialVersionUID = 1L;

    private List<CropUnitConversionRsrc> cropUnitConversionList;

    public CropUnitConversionListRsrc() {
        this.cropUnitConversionList = new ArrayList<>();
    }

    @Override
    public List<CropUnitConversionRsrc> getCropUnitConversionList() {
        return cropUnitConversionList;
    }

    @Override
    public void setCropUnitConversionList(List<CropUnitConversionRsrc> cropUnitConversionList) {
        this.cropUnitConversionList = cropUnitConversionList;
    }

}
