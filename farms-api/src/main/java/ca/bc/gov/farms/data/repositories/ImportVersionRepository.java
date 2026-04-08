package ca.bc.gov.farms.data.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class ImportVersionRepository {

    private final SimpleJdbcCall createVersionCall;
    private final SimpleJdbcCall updateControlFileInfoStgCall;
    private final SimpleJdbcCall uploadedVersionCall;
    private final SimpleJdbcCall startImportCall;
    private final SimpleJdbcCall startUploadCall;
    private final SimpleJdbcCall performImportCall;
    private final SimpleJdbcCall uploadFailureCall;
    private final SimpleJdbcCall importFailureCall;
    private final SimpleJdbcCall importCompleteCall;
    private final SimpleJdbcCall clearSuccessfulTransfersCall;

    public ImportVersionRepository(@NonNull JdbcTemplate jdbcTemplate) {
        this.createVersionCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withFunctionName("create_version");

        this.updateControlFileInfoStgCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("update_control_file_info_stg");

        this.uploadedVersionCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("uploaded_version");

        this.startImportCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("start_import");

        this.startUploadCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("start_upload");

        this.performImportCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("perform_import");

        this.uploadFailureCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("upload_failure");

        this.importFailureCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("import_failure");

        this.importCompleteCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("import_complete");

        this.clearSuccessfulTransfersCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_version_pkg")
                .withProcedureName("clear_successful_transfers");
    }

    public Integer createVersion(String description, String importFileName, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_description", description);
                put("in_import_file_name", importFileName);
                put("in_user", user);
            }
        };

        return createVersionCall.executeFunction(Integer.class, params);
    }

    public void updateControlFileInfoStg(Long versionId, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_user", user);
            }
        };

        updateControlFileInfoStgCall.execute(params);
    }

    public void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_xml", xml);
                put("in_has_errors_ind", (hasErrorsInd != null && hasErrorsInd) ? "Y" : "N");
                put("in_user", user);
            }
        };

        uploadedVersionCall.execute(params);
    }

    public void startImport(Long versionId, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_user", user);
            }
        };

        startImportCall.execute(params);
    }

    public void startUpload(Long versionId, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_user", user);
            }
        };

        startUploadCall.execute(params);
    }

    public void performImport(Long versionId, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_user", user);
            }
        };

        performImportCall.execute(params);
    }

    public void uploadFailure(Long versionId, String message, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_message", message);
                put("in_user", user);
            }
        };

        uploadFailureCall.execute(params);
    }

    public void importFailure(Long versionId, String message, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_message", message);
                put("in_user", user);
            }
        };

        importFailureCall.execute(params);
    }

    public void importComplete(Long versionId, String message, String user) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_version_id", versionId);
                put("in_message", message);
                put("in_user", user);
            }
        };

        importCompleteCall.execute(params);
    }

    public void clearSuccessfulTransfers() {

        clearSuccessfulTransfersCall.execute();
    }
}
