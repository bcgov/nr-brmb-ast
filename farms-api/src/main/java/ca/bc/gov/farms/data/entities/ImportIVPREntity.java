package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
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
public class ImportIVPREntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer programYear;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private BigDecimal insurableValue;
    private BigDecimal premiumRate;
    private String fileLocation;
}
