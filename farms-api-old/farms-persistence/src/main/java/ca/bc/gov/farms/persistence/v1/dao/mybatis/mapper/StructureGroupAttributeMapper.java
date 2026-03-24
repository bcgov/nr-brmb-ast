package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;

public interface StructureGroupAttributeMapper {

    StructureGroupAttributeDto fetch(Map<String, Object> parameters);

    StructureGroupAttributeDto fetchByStructureGroupCode(Map<String, Object> parameters);

    int insertStructureGroupAttribute(Map<String, Object> parameters);

    int updateStructureGroupAttribute(Map<String, Object> parameters);

    int deleteStructureGroupAttribute(Map<String, Object> parameters);
}
