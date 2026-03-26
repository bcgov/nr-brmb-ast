package ca.bc.gov.farms.data.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
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
public class LineItemModel extends BaseResource {

    private Long lineItemId;

    @NotNull(message = "LineItem programYear must not be null")
    private Integer programYear;

    @NotNull(message = "LineItem lineItem must not be null")
    private Integer lineItem;

    @NotBlank(message = "LineItem description must not be blank")
    @Size(min = 0, max = 256, message = "LineItem description must be between 0 and 256 characters")
    private String description;

    @Size(min = 0, max = 2, message = "LineItem province must be between 0 and 2 characters")
    private String province;

    @NotBlank(message = "LineItem eligibilityInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem eligibilityInd must be 1 character")
    private String eligibilityInd;

    @NotBlank(message = "LineItem eligibilityForRefYearsInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem eligibilityForRefYearsInd must be 1 character")
    private String eligibilityForRefYearsInd;

    @NotBlank(message = "LineItem yardageInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem yardageInd must be 1 character")
    private String yardageInd;

    @NotBlank(message = "LineItem programPaymentInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem programPaymentInd must be 1 character")
    private String programPaymentInd;

    @NotBlank(message = "LineItem contractWorkInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem contractWorkInd must be 1 character")
    private String contractWorkInd;

    @NotBlank(message = "LineItem supplyManagedCommodityInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem supplyManagedCommodityInd must be 1 character")
    private String supplyManagedCommodityInd;

    @NotBlank(message = "LineItem excludeFromRevenueCalcInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem excludeFromRevenueCalcInd must be 1 character")
    private String excludeFromRevenueCalcInd;

    @NotBlank(message = "LineItem industryAverageExpenseInd must not be blank")
    @Size(min = 1, max = 1, message = "LineItem industryAverageExpenseInd must be 1 character")
    private String industryAverageExpenseInd;

    @Size(min = 0, max = 10, message = "LineItem commodityTypeCode must be between 0 and 10 characters")
    private String commodityTypeCode;

    @Size(min = 0, max = 10, message = "LineItem fruitVegTypeCode must be between 0 and 10 characters")
    private String fruitVegTypeCode;
    private String userEmail;
}
