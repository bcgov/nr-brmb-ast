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
public class InventoryItemDetailModel extends BaseResource {

    private Long inventoryItemDetailId;

    @NotNull(message = "InventoryItemDetail programYear must not be null")
    private Integer programYear;

    @NotBlank(message = "InventoryItemDetail eligibilityInd must not be blank")
    @Size(min = 1, max = 1, message = "InventoryItemDetail eligibilityInd must be 1 character")
    private String eligibilityInd;
    private Integer lineItem;

    @Digits(integer = 10, fraction = 3, message = "InventoryItemDetail insurableValue must be a number with up to 10 integer digits and 3 fraction digits")
    private BigDecimal insurableValue;

    @Digits(integer = 9, fraction = 4, message = "InventoryItemDetail premiumRate must be a number with up to 9 integer digits and 4 fraction digits")
    private BigDecimal premiumRate;

    @Size(min = 0, max = 10, message = "InventoryItemDetail inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @Size(min = 0, max = 10, message = "InventoryItemDetail commodityTypeCode must be between 0 and 10 characters")
    private String commodityTypeCode;
    private String commodityTypeDesc;

    @Size(min = 0, max = 10, message = "InventoryItemDetail fruitVegTypeCode must be between 0 and 10 characters")
    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;

    @Size(min = 0, max = 10, message = "InventoryItemDetail multiStageCommdtyCode must be between 0 and 10 characters")
    private String multiStageCommdtyCode;
    private String multiStageCommdtyDesc;
    private Long urlId;
    private String url;
    private String userEmail;
}
