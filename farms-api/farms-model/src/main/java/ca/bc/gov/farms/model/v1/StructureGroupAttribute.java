package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface StructureGroupAttribute extends Serializable {

    public Long getStructureGroupAttributeId();
    public void setStructureGroupAttributeId(Long structureGroupAttributeId);

    public String getStructureGroupCode();
    public void setStructureGroupCode(String structureGroupCode);

    public String getStructureGroupDesc();
    public void setStructureGroupDesc(String structureGroupDesc);

    public String getRollupStructureGroupCode();
    public void setRollupStructureGroupCode(String rollupStructureGroupCode);

    public String getRollupStructureGroupDesc();
    public void setRollupStructureGroupDesc(String rollupStructureGroupDesc);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
