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
public class StructureGroupAttributeModel extends BaseResource {

    private Long structureGroupAttributeId;

    @NotBlank(message = "StructureGroupAttribute structureGroupCode must not be blank")
    @Size(min = 0, max = 10, message = "StructureGroupAttribute structureGroupCode must be between 0 and 10 characters")
    private String structureGroupCode;
    private String structureGroupDesc;

    @Size(min = 0, max = 10, message = "StructureGroupAttribute rollupStructureGroupCode must be between 0 and 10 characters")
    private String rollupStructureGroupCode;
    private String rollupStructureGroupDesc;
    private String userEmail;
}
