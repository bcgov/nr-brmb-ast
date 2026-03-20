package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class BenchmarkPerUnitModel {

    private Long benchmarkPerUnitId;

    @NotNull(message = "BenchmarkPerUnit programYear must not be null")
    private Integer programYear;

    @Size(min = 0, max = 2000, message = "BenchmarkPerUnit unitComment must be between 0 and 2000 characters")
    private String unitComment;
    private Date expiryDate;

    @NotBlank(message = "BenchmarkPerUnit municipalityCode must not be blank")
    @Size(min = 0, max = 10, message = "BenchmarkPerUnit municipalityCode must be between 0 and 10 characters")
    private String municipalityCode;
    private String municipalityDesc;

    @Size(min = 0, max = 10, message = "BenchmarkPerUnit inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @Size(min = 0, max = 10, message = "BenchmarkPerUnit structureGroupCode must be between 0 and 10 characters")
    private String structureGroupCode;
    private String structureGroupDesc;

    @Size(min = 0, max = 10, message = "BenchmarkPerUnit inventoryCode must be between 0 and 10 characters")
    private String inventoryCode;

    @Size(min = 0, max = 256, message = "BenchmarkPerUnit inventoryDesc must be between 0 and 256 characters")
    private String inventoryDesc;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus6Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus6Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus5Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus5Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus4Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus4Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus3Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus3Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus2Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus2Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus1Margin must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus1Margin;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus6Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus6Expense;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus5Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus5Expense;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus4Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus4Expense;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus3Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus3Expense;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus2Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus2Expense;

    @Digits(integer = 11, fraction = 2, message = "BenchmarkPerUnit yearMinus1Expense must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal yearMinus1Expense;
    private Long urlId;
    private String url;
    private String userEmail;
}
