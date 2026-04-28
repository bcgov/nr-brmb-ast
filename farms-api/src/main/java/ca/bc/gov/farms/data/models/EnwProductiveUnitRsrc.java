package ca.bc.gov.farms.data.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnwProductiveUnitRsrc implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private BigDecimal productiveCapacity;
    private List<EnwProductiveValueRsrc> productiveValues;
}
