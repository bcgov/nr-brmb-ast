package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.BENCHMARK_PER_UNIT_NAME)
@XmlSeeAlso({ BenchmarkPerUnitRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class BenchmarkPerUnitRsrc extends BaseResource implements BenchmarkPerUnit {

    private static final long serialVersionUID = 1L;

    private Long benchmarkPerUnitId;
    private Integer programYear;
    private String unitComment;
    private Date expiryDate;
    private String municipalityCode;
    private String inventoryItemCode;
    private String structureGroupCode;

    @Override
    public Long getBenchmarkPerUnitId() {
        return benchmarkPerUnitId;
    }

    @Override
    public void setBenchmarkPerUnitId(Long benchmarkPerUnitId) {
        this.benchmarkPerUnitId = benchmarkPerUnitId;
    }

    @Override
    public Integer getProgramYear() {
        return programYear;
    }

    @Override
    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
    }

    @Override
    public String getUnitComment() {
        return unitComment;
    }

    @Override
    public void setUnitComment(String unitComment) {
        this.unitComment = unitComment;
    }

    @Override
    public Date getExpiryDate() {
        return expiryDate;
    }

    @Override
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public String getMunicipalityCode() {
        return municipalityCode;
    }

    @Override
    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    @Override
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    @Override
    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    @Override
    public String getStructureGroupCode() {
        return structureGroupCode;
    }

    @Override
    public void setStructureGroupCode(String structureGroupCode) {
        this.structureGroupCode = structureGroupCode;
    }

}
