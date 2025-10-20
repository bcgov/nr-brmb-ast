package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface MarketRatePremiumRsrcConstraints {

    @NotNull(message = Errors.MIN_TOTAL_PREMIUM_AMOUNT_NOTNULL, groups = MarketRatePremiumRsrcConstraints.class)
    public BigDecimal getMinTotalPremiumAmount();

    @NotNull(message = Errors.MAX_TOTAL_PREMIUM_AMOUNT_NOTNULL, groups = MarketRatePremiumRsrcConstraints.class)
    public BigDecimal getMaxTotalPremiumAmount();

    @NotNull(message = Errors.RISK_CHARGE_FLAT_AMOUNT_NOTNULL, groups = MarketRatePremiumRsrcConstraints.class)
    public BigDecimal getRiskChargeFlatAmount();

    @NotNull(message = Errors.RISK_CHARGE_PCT_PREMIUM_NOTNULL, groups = MarketRatePremiumRsrcConstraints.class)
    public BigDecimal getRiskChargePctPremium();

    @NotNull(message = Errors.ADJUST_CHARGE_FLAT_AMOUNT_NOTNULL, groups = MarketRatePremiumRsrcConstraints.class)
    public BigDecimal getAdjustChargeFlatAmount();
}
