package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
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
public class InventoryTypeXrefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long agristabilityCommodityXrefId;
    private String marketCommodityInd;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String inventoryGroupCode;
    private String inventoryGroupDesc;
    private String inventoryClassCode;
    private String inventoryClassDesc;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
