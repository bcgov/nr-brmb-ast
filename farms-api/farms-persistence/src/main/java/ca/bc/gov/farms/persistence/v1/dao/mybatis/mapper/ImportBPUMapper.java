package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.ImportLogDto;

public interface ImportBPUMapper {

    void insertStagingRow(Map<String, Object> parameters);

    void clearStaging();

    void deleteStagingErrors(Map<String, Object> parameters);

    void validateStaging(Map<String, Object> parameters);

    List<ImportLogDto> getStagingErrors(Map<String, Object> parameters);
}
