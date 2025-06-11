package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

public interface VersionMapper {

    Integer createVersion(Map<String, Object> parameters);
}
