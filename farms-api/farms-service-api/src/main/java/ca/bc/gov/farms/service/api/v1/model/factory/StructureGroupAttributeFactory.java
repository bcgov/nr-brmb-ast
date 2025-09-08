package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.StructureGroupAttribute;
import ca.bc.gov.farms.model.v1.StructureGroupAttributeList;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;

public interface StructureGroupAttributeFactory {

    StructureGroupAttribute getStructureGroupAttribute(StructureGroupAttributeDto dto, FactoryContext context);

    StructureGroupAttributeList<? extends StructureGroupAttribute> getStructureGroupAttributeList(
            List<StructureGroupAttributeDto> dtos, FactoryContext context);

    void updateStructureGroupAttribute(StructureGroupAttributeDto dto, StructureGroupAttribute model);
}
