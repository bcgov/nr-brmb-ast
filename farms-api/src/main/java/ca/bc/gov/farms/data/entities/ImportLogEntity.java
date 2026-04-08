package ca.bc.gov.farms.data.entities;

import java.io.Serializable;

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
public class ImportLogEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long importLogId;
    private String logMessage;
    private Long importVersionId;
    private Long programYearVersionId;
}
