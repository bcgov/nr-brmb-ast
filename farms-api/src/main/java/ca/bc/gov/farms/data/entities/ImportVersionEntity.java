package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
public class ImportVersionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long importVersionId;
    private String importedByUser;
    private String description;
    private LocalDate importControlFileDate;
    private String importControlFileInformation;
    private LocalDate importDate;
    private String importFileName;
    private String importFilePassword;
    private byte[] importFile;
    private String stagingAuditInformation;
    private String importAuditInformation;
    private String lastStatusMessage;
    private LocalDate lastStatusDate;
    private String importStateCode;
    private String importClassCode;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
