package ca.bc.gov.farms.data.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import jakarta.validation.constraints.NotBlank;
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
public class InventoryTypeXrefModel extends BaseResource {

    private Long agristabilityCommodityXrefId;

    @NotBlank(message = "InventoryTypeXref marketCommodityInd must not be blank")
    @Size(min = 1, max = 1, message = "InventoryTypeXref marketCommodityInd must be 1 character")
    private String marketCommodityInd;

    @NotBlank(message = "InventoryTypeXref inventoryItemCode must not be blank")
    @Size(min = 0, max = 10, message = "InventoryTypeXref inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @Size(min = 0, max = 10, message = "InventoryTypeXref inventoryGroupCode must be between 0 and 10 characters")
    private String inventoryGroupCode;
    private String inventoryGroupDesc;

    @NotBlank(message = "InventoryTypeXref inventoryClassCode must not be blank")
    @Size(min = 0, max = 10, message = "InventoryTypeXref inventoryClassCode must be between 0 and 10 characters")
    private String inventoryClassCode;
    private String inventoryClassDesc;
    private String userEmail;
}
