package ca.bc.gov.farms.service.api.v1.validation.constraints;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface FruitVegTypeDetailRsrcConstraints {

    @NotNull(message = Errors.REVENUE_VARIANCE_LIMIT_NOTNULL, groups = FruitVegTypeDetailRsrcConstraints.class)
    public BigDecimal getRevenueVarianceLimit();

    @NotBlank(message = Errors.FRUIT_VEG_TYPE_CODE_NOTBLANK, groups = FruitVegTypeDetailRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.FRUIT_VEG_TYPE_CODE_SIZE, groups = FruitVegTypeDetailRsrcConstraints.class)
    public String getFruitVegTypeCode();
}
