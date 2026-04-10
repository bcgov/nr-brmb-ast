package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
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
public class YearConfigurationParameterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long yearConfigurationParameterId;
    private Integer programYear;
    private String parameterName;
    private String parameterValue;
    private String configParamTypeCode;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
