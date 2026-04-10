package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
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
public class LineItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long lineItemId;
    private Integer programYear;
    private Integer lineItem;
    private String description;
    private String province;
    private String eligibilityInd;
    private String eligibilityForRefYearsInd;
    private String yardageInd;
    private String programPaymentInd;
    private String contractWorkInd;
    private String supplyManagedCommodityInd;
    private String excludeFromRevenueCalcInd;
    private String industryAverageExpenseInd;
    private String commodityTypeCode;
    private String fruitVegTypeCode;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
