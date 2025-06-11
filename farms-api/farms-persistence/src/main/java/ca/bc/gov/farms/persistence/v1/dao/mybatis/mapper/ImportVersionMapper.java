package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

public interface ImportVersionMapper {

    Integer createVersion(Map<String, Object> parameters);

    void uploadedVersion(Map<String, Object> parameters);
}
