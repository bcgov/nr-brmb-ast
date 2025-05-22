package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.FairMarketValue;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FAIR_MARKET_VALUE_NAME)
@XmlSeeAlso({ FairMarketValueRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FairMarketValueRsrc extends BaseResource implements FairMarketValue {

    private static final long serialVersionUID = 1L;

    private String fairMarketValueId;
    private Integer programYear;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String municipalityCode;
    private String municipalityDesc;
    private String cropUnitCode;
    private String cropUnitDesc;
    private String defaultCropUnitCode;
    private String defaultCropUnitDesc;
    private BigDecimal period01Price;
    private BigDecimal period02Price;
    private BigDecimal period03Price;
    private BigDecimal period04Price;
    private BigDecimal period05Price;
    private BigDecimal period06Price;
    private BigDecimal period07Price;
    private BigDecimal period08Price;
    private BigDecimal period09Price;
    private BigDecimal period10Price;
    private BigDecimal period11Price;
    private BigDecimal period12Price;
    private BigDecimal period01Variance;
    private BigDecimal period02Variance;
    private BigDecimal period03Variance;
    private BigDecimal period04Variance;
    private BigDecimal period05Variance;
    private BigDecimal period06Variance;
    private BigDecimal period07Variance;
    private BigDecimal period08Variance;
    private BigDecimal period09Variance;
    private BigDecimal period10Variance;
    private BigDecimal period11Variance;
    private BigDecimal period12Variance;

    @Override
    public String getFairMarketValueId() {
        return fairMarketValueId;
    }

    @Override
    public void setFairMarketValueId(String fairMarketValueId) {
        this.fairMarketValueId = fairMarketValueId;
    }

    @Override
    public Integer getProgramYear() {
        return programYear;
    }

    @Override
    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
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
    public String getMunicipalityCode() {
        return municipalityCode;
    }

    @Override
    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    @Override
    public String getMunicipalityDesc() {
        return municipalityDesc;
    }

    @Override
    public void setMunicipalityDesc(String municipalityDesc) {
        this.municipalityDesc = municipalityDesc;
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
    public String getDefaultCropUnitCode() {
        return defaultCropUnitCode;
    }

    @Override
    public void setDefaultCropUnitCode(String defaultCropUnitCode) {
        this.defaultCropUnitCode = defaultCropUnitCode;
    }

    @Override
    public String getDefaultCropUnitDesc() {
        return defaultCropUnitDesc;
    }

    @Override
    public void setDefaultCropUnitDesc(String defaultCropUnitDesc) {
        this.defaultCropUnitDesc = defaultCropUnitDesc;
    }

    @Override
    public BigDecimal getPeriod01Price() {
        return period01Price;
    }

    @Override
    public void setPeriod01Price(BigDecimal period01Price) {
        this.period01Price = period01Price;
    }

    @Override
    public BigDecimal getPeriod02Price() {
        return period02Price;
    }

    @Override
    public void setPeriod02Price(BigDecimal period02Price) {
        this.period02Price = period02Price;
    }

    @Override
    public BigDecimal getPeriod03Price() {
        return period03Price;
    }

    @Override
    public void setPeriod03Price(BigDecimal period03Price) {
        this.period03Price = period03Price;
    }

    @Override
    public BigDecimal getPeriod04Price() {
        return period04Price;
    }

    @Override
    public void setPeriod04Price(BigDecimal period04Price) {
        this.period04Price = period04Price;
    }

    @Override
    public BigDecimal getPeriod05Price() {
        return period05Price;
    }

    @Override
    public void setPeriod05Price(BigDecimal period05Price) {
        this.period05Price = period05Price;
    }

    @Override
    public BigDecimal getPeriod06Price() {
        return period06Price;
    }

    @Override
    public void setPeriod06Price(BigDecimal period06Price) {
        this.period06Price = period06Price;
    }

    @Override
    public BigDecimal getPeriod07Price() {
        return period07Price;
    }

    @Override
    public void setPeriod07Price(BigDecimal period07Price) {
        this.period07Price = period07Price;
    }

    @Override
    public BigDecimal getPeriod08Price() {
        return period08Price;
    }

    @Override
    public void setPeriod08Price(BigDecimal period08Price) {
        this.period08Price = period08Price;
    }

    @Override
    public BigDecimal getPeriod09Price() {
        return period09Price;
    }

    @Override
    public void setPeriod09Price(BigDecimal period09Price) {
        this.period09Price = period09Price;
    }

    @Override
    public BigDecimal getPeriod10Price() {
        return period10Price;
    }

    @Override
    public void setPeriod10Price(BigDecimal period10Price) {
        this.period10Price = period10Price;
    }

    @Override
    public BigDecimal getPeriod11Price() {
        return period11Price;
    }

    @Override
    public void setPeriod11Price(BigDecimal period11Price) {
        this.period11Price = period11Price;
    }

    @Override
    public BigDecimal getPeriod12Price() {
        return period12Price;
    }

    @Override
    public void setPeriod12Price(BigDecimal period12Price) {
        this.period12Price = period12Price;
    }

    @Override
    public BigDecimal getPeriod01Variance() {
        return period01Variance;
    }

    @Override
    public void setPeriod01Variance(BigDecimal period01Variance) {
        this.period01Variance = period01Variance;
    }

    @Override
    public BigDecimal getPeriod02Variance() {
        return period02Variance;
    }

    @Override
    public void setPeriod02Variance(BigDecimal period02Variance) {
        this.period02Variance = period02Variance;
    }

    @Override
    public BigDecimal getPeriod03Variance() {
        return period03Variance;
    }

    @Override
    public void setPeriod03Variance(BigDecimal period03Variance) {
        this.period03Variance = period03Variance;
    }

    @Override
    public BigDecimal getPeriod04Variance() {
        return period04Variance;
    }

    @Override
    public void setPeriod04Variance(BigDecimal period04Variance) {
        this.period04Variance = period04Variance;
    }

    @Override
    public BigDecimal getPeriod05Variance() {
        return period05Variance;
    }

    @Override
    public void setPeriod05Variance(BigDecimal period05Variance) {
        this.period05Variance = period05Variance;
    }

    @Override
    public BigDecimal getPeriod06Variance() {
        return period06Variance;
    }

    @Override
    public void setPeriod06Variance(BigDecimal period06Variance) {
        this.period06Variance = period06Variance;
    }

    @Override
    public BigDecimal getPeriod07Variance() {
        return period07Variance;
    }

    @Override
    public void setPeriod07Variance(BigDecimal period07Variance) {
        this.period07Variance = period07Variance;
    }

    @Override
    public BigDecimal getPeriod08Variance() {
        return period08Variance;
    }

    @Override
    public void setPeriod08Variance(BigDecimal period08Variance) {
        this.period08Variance = period08Variance;
    }

    @Override
    public BigDecimal getPeriod09Variance() {
        return period09Variance;
    }

    @Override
    public void setPeriod09Variance(BigDecimal period09Variance) {
        this.period09Variance = period09Variance;
    }

    @Override
    public BigDecimal getPeriod10Variance() {
        return period10Variance;
    }

    @Override
    public void setPeriod10Variance(BigDecimal period10Variance) {
        this.period10Variance = period10Variance;
    }

    @Override
    public BigDecimal getPeriod11Variance() {
        return period11Variance;
    }

    @Override
    public void setPeriod11Variance(BigDecimal period11Variance) {
        this.period11Variance = period11Variance;
    }

    @Override
    public BigDecimal getPeriod12Variance() {
        return period12Variance;
    }

    @Override
    public void setPeriod12Variance(BigDecimal period12Variance) {
        this.period12Variance = period12Variance;
    }

}
