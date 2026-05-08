package ca.bc.gov.farms.data.models;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnwProductiveValueRsrc implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal bpuMargin;
    private BigDecimal productiveValue;
}
