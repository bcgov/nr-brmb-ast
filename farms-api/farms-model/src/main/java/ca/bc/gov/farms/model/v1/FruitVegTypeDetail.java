package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface FruitVegTypeDetail extends Serializable {

    public String getFruitVegTypeCode();
    public void setFruitVegTypeCode(String fruitVegTypeCode);

    public String getFruitVegTypeDesc();
    public void setFruitVegTypeDesc(String fruitVegTypeDesc);

    public LocalDate getEstablishedDate();
    public void setEstablishedDate(LocalDate establishedDate);

    public LocalDate getExpiryDate();
    public void setExpiryDate(LocalDate expiryDate);

    public BigDecimal getRevenueVarianceLimit();
    public void setRevenueVarianceLimit(BigDecimal revenueVarianceLimit);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
