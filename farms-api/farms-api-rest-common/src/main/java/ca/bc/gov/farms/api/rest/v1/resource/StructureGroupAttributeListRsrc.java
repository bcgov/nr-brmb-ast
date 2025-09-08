package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.StructureGroupAttributeList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.STRUCTURE_GROUP_ATTRIBUTE_LIST_NAME)
@XmlSeeAlso({ StructureGroupAttributeListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class StructureGroupAttributeListRsrc extends BaseResource
        implements StructureGroupAttributeList<StructureGroupAttributeRsrc> {

    private static final long serialVersionUID = 1L;

    private List<StructureGroupAttributeRsrc> structureGroupAttributeList;

    public StructureGroupAttributeListRsrc() {
        this.structureGroupAttributeList = new ArrayList<>();
    }

    @Override
    public List<StructureGroupAttributeRsrc> getStructureGroupAttributeList() {
        return structureGroupAttributeList;
    }

    @Override
    public void setStructureGroupAttributeList(List<StructureGroupAttributeRsrc> structureGroupAttributeList) {
        this.structureGroupAttributeList = structureGroupAttributeList;
    }

}
