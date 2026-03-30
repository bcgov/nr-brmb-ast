package ca.bc.gov.farms.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.data.entities.ImportIVPREntity;

@Repository
public class ImportIVPRRepository {

    private final SimpleJdbcCall clearStagingCall;
    private final SimpleJdbcCall insertStagingCall;
    private final SimpleJdbcCall validateStagingCall;
    private final SimpleJdbcCall deleteErrorsCall;
    private final SimpleJdbcCall getErrorsCall;
    private final SimpleJdbcCall performImportCall;

    public ImportIVPRRepository(@NonNull JdbcTemplate jdbcTemplate) {
        this.clearStagingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withProcedureName("clear_staging");

        this.insertStagingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withProcedureName("insert_staging_row");

        this.validateStagingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withProcedureName("validate_staging");

        this.deleteErrorsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withProcedureName("delete_staging_errors");

        this.performImportCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withProcedureName("staging_to_operational");

        this.getErrorsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_ivpr_pkg")
                .withFunctionName("get_staging_errors")
                .returningResultSet("return",
                        (rs, rowNum) -> rs.getString("log_message"));
    }

    public void clearStaging() throws DaoException {

        clearStagingCall.execute();
    }

    public void insertStagingRow(ImportIVPREntity dto, String userId, int rowNum) throws DaoException {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_line_number", rowNum);
                put("in_program_year",
                        dto.getProgramYear() == null ? null : dto.getProgramYear().shortValue());
                put("in_inventory_item_code", dto.getInventoryItemCode());
                put("in_insurable_value", dto.getInsurableValue());
                put("in_premium_rate", dto.getPremiumRate());
                put("in_file_location", dto.getFileLocation());
                put("in_user", userId);
            }
        };

        insertStagingCall.execute(params);
    }

    public void validateStaging(Long importVersionId) throws DaoException {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
            }
        };

        validateStagingCall.execute(params);
    }

    public void deleteStagingErrors(Long importVersionId) throws DaoException {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
            }
        };

        deleteErrorsCall.execute(params);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStagingErrors(Long importVersionId) throws DaoException {

        return getErrorsCall.executeFunction(List.class, importVersionId);
    }

    public void performImport(Long importVersionId, String userId) throws DaoException {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
                put("in_user", userId);
            }
        };

        performImportCall.execute(params);
    }
}
