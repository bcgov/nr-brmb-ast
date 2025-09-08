package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface StructureGroupAttributeList<E extends StructureGroupAttribute> extends Serializable {

    public List<E> getStructureGroupAttributeList();

    public void setStructureGroupAttributeList(List<E> structureGroupAttributeList);
}
