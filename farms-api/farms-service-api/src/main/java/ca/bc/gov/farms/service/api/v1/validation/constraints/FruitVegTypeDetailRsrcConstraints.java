package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface FruitVegTypeDetailRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = FruitVegTypeDetailRsrcConstraints.class)
    public BigDecimal getRevenueVarianceLimit();

    @NotBlank(message = Errors.FRUIT_VEG_TYPE_CODE_NOTBLANK, groups = FruitVegTypeDetailRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.FRUIT_VEG_TYPE_CODE_SIZE, groups = FruitVegTypeDetailRsrcConstraints.class)
    public String getFruitVegTypeCode();
}
