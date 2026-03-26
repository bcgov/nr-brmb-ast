package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
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
public class ConfigurationParameterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long configurationParameterId;
    private String parameterName;
    private String parameterValue;
    private String sensitiveDataInd;
    private String configParamTypeCode;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;
}
