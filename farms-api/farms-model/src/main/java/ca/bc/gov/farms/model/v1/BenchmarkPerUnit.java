package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.Date;

public interface BenchmarkPerUnit extends Serializable {

    public Long getBenchmarkPerUnitId();
    public void setBenchmarkPerUnitId(Long benchmarkPerUnitId);

    public Integer getProgramYear();
    public void setProgramYear(Integer programYear);

    public String getUnitComment();
    public void setUnitComment(String unitComment);

    public Date getExpiryDate();
    public void setExpiryDate(Date expiryDate);

    public String getMunicipalityCode();
    public void setMunicipalityCode(String municipalityCode);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getStructureGroupCode();
    public void setStructureGroupCode(String structureGroupCode);
}
