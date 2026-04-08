package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface MarketRatePremium extends Serializable {

    public Long getMarketRatePremiumId();
    public void setMarketRatePremiumId(Long marketRatePremiumId);

    public BigDecimal getMinTotalPremiumAmount();
    public void setMinTotalPremiumAmount(BigDecimal minTotalPremiumAmount);

    public BigDecimal getMaxTotalPremiumAmount();
    public void setMaxTotalPremiumAmount(BigDecimal maxTotalPremiumAmount);

    public BigDecimal getRiskChargeFlatAmount();
    public void setRiskChargeFlatAmount(BigDecimal riskChargeFlatAmount);

    public BigDecimal getRiskChargePctPremium();
    public void setRiskChargePctPremium(BigDecimal riskChargePctPremium);

    public BigDecimal getAdjustChargeFlatAmount();
    public void setAdjustChargeFlatAmount(BigDecimal adjustChargeFlatAmount);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
