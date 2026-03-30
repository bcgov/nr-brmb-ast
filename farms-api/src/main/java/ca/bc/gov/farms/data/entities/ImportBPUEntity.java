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
public class ImportBPUEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer programYear;
    private String municipalityCode;
    private String inventoryItemCode;
    private String unitComment;
    private BigDecimal yearMinus6Margin;
    private BigDecimal yearMinus5Margin;
    private BigDecimal yearMinus4Margin;
    private BigDecimal yearMinus3Margin;
    private BigDecimal yearMinus2Margin;
    private BigDecimal yearMinus1Margin;
    private BigDecimal yearMinus6Expense;
    private BigDecimal yearMinus5Expense;
    private BigDecimal yearMinus4Expense;
    private BigDecimal yearMinus3Expense;
    private BigDecimal yearMinus2Expense;
    private BigDecimal yearMinus1Expense;
    private String fileLocation;
}
