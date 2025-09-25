package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.CropUnitConversion;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CROP_UNIT_CONVERSION_NAME)
@XmlSeeAlso({ CropUnitConversionRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class CropUnitConversionRsrc extends BaseResource implements CropUnitConversion {

    private static final long serialVersionUID = 1L;

    private Long cropUnitConversionFactorId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private BigDecimal conversionFactor;
    private String targetCropUnitCode;
    private String targetCropUnitDesc;
    private String userEmail;

    @Override
    public Long getCropUnitConversionFactorId() {
        return cropUnitConversionFactorId;
    }

    @Override
    public void setCropUnitConversionFactorId(Long cropUnitConversionFactorId) {
        this.cropUnitConversionFactorId = cropUnitConversionFactorId;
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
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public String getTargetCropUnitCode() {
        return targetCropUnitCode;
    }

    @Override
    public void setTargetCropUnitCode(String targetCropUnitCode) {
        this.targetCropUnitCode = targetCropUnitCode;
    }

    @Override
    public String getTargetCropUnitDesc() {
        return targetCropUnitDesc;
    }

    @Override
    public void setTargetCropUnitDesc(String targetCropUnitDesc) {
        this.targetCropUnitDesc = targetCropUnitDesc;
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
