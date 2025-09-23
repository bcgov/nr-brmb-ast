package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.MarketRatePremium;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.MARKET_RATE_PREMIUM_NAME)
@XmlSeeAlso({ MarketRatePremiumRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class MarketRatePremiumRsrc extends BaseResource implements MarketRatePremium {

    private static final long serialVersionUID = 1L;

    private Long marketRatePremiumId;
    private BigDecimal minTotalPremiumAmount;
    private BigDecimal maxTotalPremiumAmount;
    private BigDecimal riskChargeFlatAmount;
    private BigDecimal riskChargePctPremium;
    private BigDecimal adjustChargeFlatAmount;
    private String userEmail;

    @Override
    public Long getMarketRatePremiumId() {
        return marketRatePremiumId;
    }

    @Override
    public void setMarketRatePremiumId(Long marketRatePremiumId) {
        this.marketRatePremiumId = marketRatePremiumId;
    }

    @Override
    public BigDecimal getMinTotalPremiumAmount() {
        return minTotalPremiumAmount;
    }

    @Override
    public void setMinTotalPremiumAmount(BigDecimal minTotalPremiumAmount) {
        this.minTotalPremiumAmount = minTotalPremiumAmount;
    }

    @Override
    public BigDecimal getMaxTotalPremiumAmount() {
        return maxTotalPremiumAmount;
    }

    @Override
    public void setMaxTotalPremiumAmount(BigDecimal maxTotalPremiumAmount) {
        this.maxTotalPremiumAmount = maxTotalPremiumAmount;
    }

    @Override
    public BigDecimal getRiskChargeFlatAmount() {
        return riskChargeFlatAmount;
    }

    @Override
    public void setRiskChargeFlatAmount(BigDecimal riskChargeFlatAmount) {
        this.riskChargeFlatAmount = riskChargeFlatAmount;
    }

    @Override
    public BigDecimal getRiskChargePctPremium() {
        return riskChargePctPremium;
    }

    @Override
    public void setRiskChargePctPremium(BigDecimal riskChargePctPremium) {
        this.riskChargePctPremium = riskChargePctPremium;
    }

    @Override
    public BigDecimal getAdjustChargeFlatAmount() {
        return adjustChargeFlatAmount;
    }

    @Override
    public void setAdjustChargeFlatAmount(BigDecimal adjustChargeFlatAmount) {
        this.adjustChargeFlatAmount = adjustChargeFlatAmount;
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
