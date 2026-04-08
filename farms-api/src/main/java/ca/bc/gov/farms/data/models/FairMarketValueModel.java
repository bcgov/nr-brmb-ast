package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
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
public class FairMarketValueModel extends BaseResource {

    private String fairMarketValueId;

    @NotNull(message = "FairMarketValue programYear must not be null")
    private Integer programYear;

    @NotBlank(message = "FairMarketValue inventoryItemCode must not be blank")
    @Size(min = 0, max = 10, message = "FairMarketValue inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @NotBlank(message = "FairMarketValue municipalityCode must not be blank")
    @Size(min = 0, max = 10, message = "FairMarketValue municipalityCode must be between 0 and 10 characters")
    private String municipalityCode;
    private String municipalityDesc;

    @NotBlank(message = "FairMarketValue cropUnitCode must not be blank")
    @Size(min = 0, max = 10, message = "FairMarketValue cropUnitCode must be between 0 and 10 characters")
    private String cropUnitCode;
    private String cropUnitDesc;
    private String defaultCropUnitCode;
    private String defaultCropUnitDesc;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period01Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period01Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period02Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period02Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period03Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period03Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period04Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period04Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period05Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period05Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period06Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period06Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period07Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period07Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period08Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period08Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period09Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period09Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period10Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period10Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period11Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period11Price;

    @Digits(integer = 11, fraction = 2, message = "FairMarketValue period12Price must be a number with up to 11 integer digits and 2 fraction digits")
    private BigDecimal period12Price;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period01Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period01Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period02Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period02Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period03Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period03Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period04Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period04Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period05Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period05Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period06Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period06Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period07Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period07Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period08Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period08Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period09Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period09Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period10Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period10Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period11Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period11Variance;

    @Digits(integer = 3, fraction = 2, message = "FairMarketValue period12Variance must be a number with up to 3 integer digits and 2 fraction digits")
    private BigDecimal period12Variance;
    private Long urlId;
    private String url;
    private String userEmail;
}
