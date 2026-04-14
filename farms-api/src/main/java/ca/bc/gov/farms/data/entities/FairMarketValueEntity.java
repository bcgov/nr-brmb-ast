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
public class FairMarketValueEntity implements Serializable {
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
    private Long urlId;
    private String url;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
