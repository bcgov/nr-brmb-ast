package ca.bc.gov.farms.data.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@XmlRootElement(namespace = BaseResourceTypes.COMMON_NAMESPACE, name = BaseResourceTypes.CODE_NAME)
@JsonSubTypes({ @Type(value = CodeRsrc.class, name = BaseResourceTypes.CODE) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeRsrc extends BaseResource {

    private String code;
    private String description;
    private Integer displayOrder;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String userEmail;
}
