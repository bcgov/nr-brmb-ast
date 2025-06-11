package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

public interface ImportVersionMapper {

    Integer createVersion(Map<String, Object> parameters);

    void updateControlFileInfoStg(Map<String, Object> parameters);

    void uploadedVersion(Map<String, Object> parameters);

    void startImport(Map<String, Object> parameters);

    void startUpload(Map<String, Object> parameters);

    void performImport(Map<String, Object> parameters);

    void uploadFailure(Map<String, Object> parameters);

    void importFailure(Map<String, Object> parameters);

    void importComplete(Map<String, Object> parameters);
}
