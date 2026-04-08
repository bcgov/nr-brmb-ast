package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketRatePremiumModel extends BaseResource {

    private Long marketRatePremiumId;

    @NotNull(message = "MarketRatePremium minTotalPremiumAmount must not be null")
    private BigDecimal minTotalPremiumAmount;

    @NotNull(message = "MarketRatePremium maxTotalPremiumAmount must not be null")
    private BigDecimal maxTotalPremiumAmount;

    @NotNull(message = "MarketRatePremium riskChargeFlatAmount must not be null")
    private BigDecimal riskChargeFlatAmount;

    @NotNull(message = "MarketRatePremium riskChargePctPremium must not be null")
    private BigDecimal riskChargePctPremium;

    @NotNull(message = "MarketRatePremium adjustChargeFlatAmount must not be null")
    private BigDecimal adjustChargeFlatAmount;
    private String userEmail;
}
