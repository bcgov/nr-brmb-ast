package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;

public interface FruitVegTypeDetail extends Serializable {

    public Long getFruitVegTypeDetailId();
    public void setFruitVegTypeDetailId(Long fruitVegTypeDetailId);

    public BigDecimal getRevenueVarianceLimit();
    public void setRevenueVarianceLimit(BigDecimal revenueVarianceLimit);

    public String getFruitVegTypeCode();
    public void setFruitVegTypeCode(String fruitVegTypeCode);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
