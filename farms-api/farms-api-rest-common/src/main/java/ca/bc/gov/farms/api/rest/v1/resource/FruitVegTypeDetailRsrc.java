package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FRUIT_VEG_TYPE_DETAIL_NAME)
@XmlSeeAlso({ FruitVegTypeDetailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FruitVegTypeDetailRsrc extends BaseResource implements FruitVegTypeDetail {

    private static final long serialVersionUID = 1L;

    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private Date establishedDate;
    private Date expiryDate;
    private BigDecimal revenueVarianceLimit;
    private String userEmail;

    @Override
    public BigDecimal getRevenueVarianceLimit() {
        return revenueVarianceLimit;
    }

    @Override
    public void setRevenueVarianceLimit(BigDecimal revenueVarianceLimit) {
        this.revenueVarianceLimit = revenueVarianceLimit;
    }

    @Override
    public String getFruitVegTypeCode() {
        return fruitVegTypeCode;
    }

    @Override
    public void setFruitVegTypeCode(String fruitVegTypeCode) {
        this.fruitVegTypeCode = fruitVegTypeCode;
    }

    @Override
    public String getFruitVegTypeDesc() {
        return fruitVegTypeDesc;
    }

    @Override
    public void setFruitVegTypeDesc(String fruitVegTypeDesc) {
        this.fruitVegTypeDesc = fruitVegTypeDesc;
    }

    @Override
    public Date getEstablishedDate() {
        return establishedDate;
    }

    @Override
    public void setEstablishedDate(Date establishedDate) {
        this.establishedDate = establishedDate;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
