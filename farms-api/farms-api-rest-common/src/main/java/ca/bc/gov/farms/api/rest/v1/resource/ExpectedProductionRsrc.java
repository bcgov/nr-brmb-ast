package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ExpectedProduction;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.EXPECTED_PRODUCTION_NAME)
@XmlSeeAlso({ ExpectedProductionRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ExpectedProductionRsrc extends BaseResource implements ExpectedProduction {

    private static final long serialVersionUID = 1L;

    private Long expectedProductionId;
    private BigDecimal expectedProductionPerProdUnit;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private String userEmail;

    @Override
    public Long getExpectedProductionId() {
        return expectedProductionId;
    }

    @Override
    public void setExpectedProductionId(Long expectedProductionId) {
        this.expectedProductionId = expectedProductionId;
    }

    @Override
    public BigDecimal getExpectedProductionPerProdUnit() {
        return expectedProductionPerProdUnit;
    }

    @Override
    public void setExpectedProductionPerProdUnit(BigDecimal expectedProductionPerProdUnit) {
        this.expectedProductionPerProdUnit = expectedProductionPerProdUnit;
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
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
