package ca.bc.gov.farms.api.rest.v1.resource;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.StructureGroupAttribute;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.STRUCTURE_GROUP_ATTRIBUTE_NAME)
@XmlSeeAlso({ StructureGroupAttributeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class StructureGroupAttributeRsrc extends BaseResource implements StructureGroupAttribute {

    private static final long serialVersionUID = 1L;

    private Long structureGroupAttributeId;
    private String structureGroupCode;
    private String structureGroupDesc;
    private String rollupStructureGroupCode;
    private String rollupStructureGroupDesc;
    private String userEmail;

    @Override
    public Long getStructureGroupAttributeId() {
        return structureGroupAttributeId;
    }

    @Override
    public void setStructureGroupAttributeId(Long structureGroupAttributeId) {
        this.structureGroupAttributeId = structureGroupAttributeId;
    }

    @Override
    public String getStructureGroupCode() {
        return structureGroupCode;
    }

    @Override
    public void setStructureGroupCode(String structureGroupCode) {
        this.structureGroupCode = structureGroupCode;
    }

    @Override
    public String getStructureGroupDesc() {
        return structureGroupDesc;
    }

    @Override
    public void setStructureGroupDesc(String structureGroupDesc) {
        this.structureGroupDesc = structureGroupDesc;
    }

    @Override
    public String getRollupStructureGroupCode() {
        return rollupStructureGroupCode;
    }

    @Override
    public void setRollupStructureGroupCode(String rollupStructureGroupCode) {
        this.rollupStructureGroupCode = rollupStructureGroupCode;
    }

    @Override
    public String getRollupStructureGroupDesc() {
        return rollupStructureGroupDesc;
    }

    @Override
    public void setRollupStructureGroupDesc(String rollupStructureGroupDesc) {
        this.rollupStructureGroupDesc = rollupStructureGroupDesc;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
