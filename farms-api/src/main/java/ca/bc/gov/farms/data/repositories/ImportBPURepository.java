package ca.bc.gov.farms.data.repositories;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.ImportBPUEntity;

@Repository
public class ImportBPURepository {

    private final SimpleJdbcCall insertStagingRowCall;
    private final SimpleJdbcCall clearStagingCall;
    private final SimpleJdbcCall deleteStagingErrorsCall;
    private final SimpleJdbcCall validateStagingCall;
    private final SimpleJdbcCall getStagingErrorsCall;
    private final SimpleJdbcCall performImportCall;

    public ImportBPURepository(@NonNull JdbcTemplate jdbcTemplate) {
        this.insertStagingRowCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withProcedureName("insert_staging_row");

        this.clearStagingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withProcedureName("clear_staging");

        this.deleteStagingErrorsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withProcedureName("delete_staging_errors");

        this.validateStagingCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withProcedureName("validate_staging");

        this.getStagingErrorsCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withFunctionName("get_staging_errors")
                .returningResultSet("return",
                        (rs, rowNum) -> rs.getString("log_message"));

        this.performImportCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_bpu_pkg")
                .withProcedureName("staging_to_operational");
    }

    public void insertStagingRow(ImportBPUEntity dto, String userId, int rowNum) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_line_number", rowNum);
                put("in_program_year", dto.getProgramYear() == null ? null : dto.getProgramYear().shortValue());
                put("in_municipality_code", dto.getMunicipalityCode());
                put("in_inventory_item_code", dto.getInventoryItemCode());
                put("in_unit_comment", dto.getUnitComment());
                put("in_year_minus_6_margin", dto.getYearMinus6Margin());
                put("in_year_minus_5_margin", dto.getYearMinus5Margin());
                put("in_year_minus_4_margin", dto.getYearMinus4Margin());
                put("in_year_minus_3_margin", dto.getYearMinus3Margin());
                put("in_year_minus_2_margin", dto.getYearMinus2Margin());
                put("in_year_minus_1_margin", dto.getYearMinus1Margin());
                put("in_year_minus_6_expense", dto.getYearMinus6Expense());
                put("in_year_minus_5_expense", dto.getYearMinus5Expense());
                put("in_year_minus_4_expense", dto.getYearMinus4Expense());
                put("in_year_minus_3_expense", dto.getYearMinus3Expense());
                put("in_year_minus_2_expense", dto.getYearMinus2Expense());
                put("in_year_minus_1_expense", dto.getYearMinus1Expense());
                put("in_file_location", dto.getFileLocation());
                put("in_user", userId);
            }
        };

        insertStagingRowCall.execute(params);
    }

    public void clearStaging() {

        clearStagingCall.execute();
    }

    public void deleteStagingErrors(Long importVersionId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
            }
        };

        deleteStagingErrorsCall.execute(params);
    }

    public void validateStaging(Long importVersionId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
            }
        };

        validateStagingCall.execute(params);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStagingErrors(Long importVersionId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
            }
        };

        return getStagingErrorsCall.executeFunction(List.class, params);
    }

    public void performImport(Long importVersionId, String userId) {

        Map<String, Object> params = new HashMap<>() {
            {
                put("in_import_version_id", importVersionId);
                put("in_user", userId);
            }
        };

        performImportCall.execute(params);
    }
}
