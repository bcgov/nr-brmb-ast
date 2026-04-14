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
public class StructureGroupAttributeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long structureGroupAttributeId;
    private String structureGroupCode;
    private String structureGroupDesc;
    private String rollupStructureGroupCode;
    private String rollupStructureGroupDesc;

    private Integer revisionCount;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
