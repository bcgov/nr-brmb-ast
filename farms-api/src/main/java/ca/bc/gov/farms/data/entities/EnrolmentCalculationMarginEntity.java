package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class EnrolmentCalculationMarginEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer referenceYear;
    private BigDecimal productionMarginAccrualAdjustments;
    private BigDecimal productionMarginAfterStructuralChanges;
    private BigDecimal totalAllowableIncome;
    private BigDecimal totalUnallowableExpenses;
    private String hasProductiveUnitsInd;
}
