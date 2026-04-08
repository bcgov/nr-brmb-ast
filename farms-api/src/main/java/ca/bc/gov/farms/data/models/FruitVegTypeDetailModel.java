package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FruitVegTypeDetailModel extends BaseResource {

    @NotBlank(message = "FruitVegTypeDetail fruitVegTypeCode must not be blank")
    @Size(min = 0, max = 10, message = "FruitVegTypeDetail fruitVegTypeCode must be between 0 and 10 characters")
    private String fruitVegTypeCode;
    private String fruitVegTypeDesc;
    private LocalDate establishedDate;
    private LocalDate expiryDate;

    @NotNull(message = "FruitVegTypeDetail revenueVarianceLimit must not be null")
    private BigDecimal revenueVarianceLimit;
    private String userEmail;
}
