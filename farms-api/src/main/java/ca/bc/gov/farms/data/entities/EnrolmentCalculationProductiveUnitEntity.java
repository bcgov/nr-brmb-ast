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
public class EnrolmentCalculationProductiveUnitEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private BigDecimal productiveCapacity;
    private BigDecimal bpuMarginYearMinus4;
    private BigDecimal bpuMarginYearMinus3;
    private BigDecimal bpuMarginYearMinus2;
    private BigDecimal productiveValueYearMinus4;
    private BigDecimal productiveValueYearMinus3;
    private BigDecimal productiveValueYearMinus2;
}
