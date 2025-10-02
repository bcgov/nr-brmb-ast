package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public interface FruitVegTypeDetail extends Serializable {

    public String getFruitVegTypeCode();
    public void setFruitVegTypeCode(String fruitVegTypeCode);

    public String getFruitVegTypeDesc();
    public void setFruitVegTypeDesc(String fruitVegTypeDesc);

    public Date getEstablishedDate();
    public void setEstablishedDate(Date establishedDate);

    public Date getExpiryDate();
    public void setExpiryDate(Date expiryDate);

    public BigDecimal getRevenueVarianceLimit();
    public void setRevenueVarianceLimit(BigDecimal revenueVarianceLimit);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
