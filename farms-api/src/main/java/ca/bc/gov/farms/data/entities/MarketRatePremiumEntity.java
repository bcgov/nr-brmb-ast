package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketRatePremiumEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long marketRatePremiumId;
    private BigDecimal minTotalPremiumAmount;
    private BigDecimal maxTotalPremiumAmount;
    private BigDecimal riskChargeFlatAmount;
    private BigDecimal riskChargePctPremium;
    private BigDecimal adjustChargeFlatAmount;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
