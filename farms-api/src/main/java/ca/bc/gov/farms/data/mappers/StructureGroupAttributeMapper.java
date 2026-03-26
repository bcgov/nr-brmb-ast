package ca.bc.gov.farms.data.mappers;

import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.StructureGroupAttributeEntity;

@Mapper
public interface StructureGroupAttributeMapper {

    StructureGroupAttributeEntity fetch(Long structureGroupAttributeId);

    StructureGroupAttributeEntity fetchByStructureGroupCode(String structureGroupCode);

    int insertStructureGroupAttribute(StructureGroupAttributeEntity dto, String userId);

    int updateStructureGroupAttribute(StructureGroupAttributeEntity dto, String userId);

    int deleteStructureGroupAttribute(Long structureGroupAttributeId);
}
