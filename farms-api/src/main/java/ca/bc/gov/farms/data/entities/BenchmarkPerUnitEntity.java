package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class BenchmarkPerUnitEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long benchmarkPerUnitId;
    private Integer programYear;
    private String unitComment;
    private LocalDate expiryDate;
    private String municipalityCode;
    private String municipalityDesc;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String structureGroupCode;
    private String structureGroupDesc;
    private String inventoryCode;
    private String inventoryDesc;
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
    private Long urlId;
    private String url;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
