package ca.bc.gov.farms.service.api.v1.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface StructureGroupAttributeRsrcConstraints {

    @NotBlank(message = Errors.STRUCTURE_GROUP_CODE_NOTBLANK, groups = StructureGroupAttributeRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.STRUCTURE_GROUP_CODE_SIZE, groups = StructureGroupAttributeRsrcConstraints.class)
    public String getStructureGroupCode();

    @Size(min = 0, max = 10, message = Errors.ROLLUP_STRUCTURE_GROUP_CODE_SIZE, groups = StructureGroupAttributeRsrcConstraints.class)
    public String getRollupStructureGroupCode();

}
