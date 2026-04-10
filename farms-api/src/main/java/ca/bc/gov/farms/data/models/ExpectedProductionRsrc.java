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
public class ExpectedProductionRsrc extends BaseResource {

    private Long expectedProductionId;

    @NotNull(message = "ExpectedProduction expectedProductionPerProdUnit must not be null")
    @Digits(integer = 10, fraction = 3, message = "ExpectedProduction expectedProductionPerProdUnit must be a number with up to 10 integer digits and 3 fraction digits")
    private BigDecimal expectedProductionPerProdUnit;

    @NotBlank(message = "ExpectedProduction inventoryItemCode must not be blank")
    @Size(min = 0, max = 10, message = "ExpectedProduction inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @NotBlank(message = "ExpectedProduction cropUnitCode must not be blank")
    @Size(min = 0, max = 10, message = "ExpectedProduction cropUnitCode must be between 0 and 10 characters")
    private String cropUnitCode;
    private String cropUnitDesc;
    private String userEmail;
}
