package ca.bc.gov.farms.api.rest.v1.resource;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.brmb.common.rest.resource.transformers.LocalDateJAXBAdapter;
import ca.bc.gov.brmb.common.rest.resource.transformers.LocalDateJacksonDeserializer;
import ca.bc.gov.brmb.common.rest.resource.transformers.LocalDateJacksonSerializer;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;
import io.swagger.v3.oas.annotations.media.Schema;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FRUIT_VEG_TYPE_DETAIL_NAME)
@XmlSeeAlso({ FruitVegTypeDetailRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FruitVegTypeDetailRsrc extends BaseResource implements FruitVegTypeDetail {

    private static final long serialVersionUID = 1L;

    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private LocalDate establishedDate;
    private LocalDate expiryDate;
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

    @JsonSerialize(using = LocalDateJacksonSerializer.class)
    @JsonDeserialize(using = LocalDateJacksonDeserializer.class)
    @XmlJavaTypeAdapter(LocalDateJAXBAdapter.class)
    @Schema(type = "string")
    @Override
    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    @Override
    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    @JsonSerialize(using = LocalDateJacksonSerializer.class)
    @JsonDeserialize(using = LocalDateJacksonDeserializer.class)
    @XmlJavaTypeAdapter(LocalDateJAXBAdapter.class)
    @Schema(type = "string")
    @Override
    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(LocalDate expiryDate) {
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
