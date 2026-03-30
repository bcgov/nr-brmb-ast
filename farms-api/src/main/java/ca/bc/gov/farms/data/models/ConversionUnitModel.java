package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversionUnitModel {

    private Long cropUnitConversionFactorId;
    private BigDecimal conversionFactor;
    private String targetCropUnitCode;
    private String targetCropUnitDesc;
}
