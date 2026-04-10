package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class FruitVegTypeDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private LocalDate establishedDate;
    private LocalDate expiryDate;
    private BigDecimal revenueVarianceLimit;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
