package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ConversionUnit;
import ca.bc.gov.farms.model.v1.CropUnitConversion;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_UNIT_CONVERSION_NAME)
@XmlSeeAlso({ CropUnitConversionRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class CropUnitConversionRsrc extends BaseResource implements CropUnitConversion {

    private static final long serialVersionUID = 1L;

    private Long cropUnitDefaultId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    @JsonDeserialize(as = ConversionUnitRsrc.class)
    private List<ConversionUnit> conversionUnits = new ArrayList<>();
    private String userEmail;

    @Override
    public Long getCropUnitDefaultId() {
        return cropUnitDefaultId;
    }

    @Override
    public void setCropUnitDefaultId(Long cropUnitDefaultId) {
        this.cropUnitDefaultId = cropUnitDefaultId;
    }

    @Override
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    @Override
    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    @Override
    public String getInventoryItemDesc() {
        return inventoryItemDesc;
    }

    @Override
    public void setInventoryItemDesc(String inventoryItemDesc) {
        this.inventoryItemDesc = inventoryItemDesc;
    }

    @Override
    public String getCropUnitCode() {
        return cropUnitCode;
    }

    @Override
    public void setCropUnitCode(String cropUnitCode) {
        this.cropUnitCode = cropUnitCode;
    }

    @Override
    public String getCropUnitDesc() {
        return cropUnitDesc;
    }

    @Override
    public void setCropUnitDesc(String cropUnitDesc) {
        this.cropUnitDesc = cropUnitDesc;
    }

    @Override
    public List<ConversionUnit> getConversionUnits() {
        return conversionUnits;
    }

    @Override
    public void setConversionUnits(List<ConversionUnit> conversionUnits) {
        this.conversionUnits = conversionUnits;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
