package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

public interface ImportBPUMapper {

    void insertStagingRow(Map<String, Object> parameters);

    void clearStaging();

    void deleteStagingErrors(Map<String, Object> parameters);
}
