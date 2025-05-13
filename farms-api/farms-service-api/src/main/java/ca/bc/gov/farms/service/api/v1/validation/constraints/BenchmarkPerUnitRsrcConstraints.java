package ca.bc.gov.farms.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface BenchmarkPerUnitRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = BenchmarkPerUnitRsrcConstraints.class)
    public Integer getProgramYear();

    @Size(min = 0, max = 2000, message = Errors.UNIT_COMMENT_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getUnitComment();

    @NotBlank(message = Errors.MUNICIPALITY_CODE_NOTBLANK, groups = BenchmarkPerUnitRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.MUNICIPALITY_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getMunicipalityCode();

    @Size(min = 0, max = 10, message = Errors.INVENTORY_ITEM_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getInventoryItemCode();

    @Size(min = 0, max = 10, message = Errors.STRUCTURE_GROUP_CODE_SIZE, groups = BenchmarkPerUnitRsrcConstraints.class)
    public String getStructureGroupCode();
}
