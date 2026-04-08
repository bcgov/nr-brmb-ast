package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class InventoryItemDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long inventoryItemDetailId;
    private Integer programYear;
    private String eligibilityInd;
    private Integer lineItem;
    private BigDecimal insurableValue;
    private BigDecimal premiumRate;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String commodityTypeCode;
    private String commodityTypeDesc;
    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private String multiStageCommdtyCode;
    private String multiStageCommdtyDesc;
    private Long urlId;
    private String url;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
}
