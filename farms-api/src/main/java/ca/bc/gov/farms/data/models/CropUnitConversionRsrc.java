package ca.bc.gov.farms.data.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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
public class CropUnitConversionRsrc extends BaseResource {

    private Long cropUnitDefaultId;

    @NotBlank(message = "CropUnitConversion inventoryItemCode must not be blank")
    @Size(min = 0, max = 10, message = "CropUnitConversion inventoryItemCode must be between 0 and 10 characters")
    private String inventoryItemCode;
    private String inventoryItemDesc;

    @NotBlank(message = "CropUnitConversion cropUnitCode must not be blank")
    @Size(min = 0, max = 10, message = "CropUnitConversion cropUnitCode must be between 0 and 10 characters")
    private String cropUnitCode;
    private String cropUnitDesc;

    @JsonDeserialize(contentAs = ConversionUnitRsrc.class)
    @Builder.Default
    private List<ConversionUnitRsrc> conversionUnits = new ArrayList<>();
    private String userEmail;
}
