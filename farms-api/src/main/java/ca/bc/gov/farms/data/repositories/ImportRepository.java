package ca.bc.gov.farms.data.repositories;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.data.entities.ImportVersionEntity;

@Repository
public class ImportRepository {

    private final SimpleJdbcCall insertImportVersionCall;

    public ImportRepository(@NonNull JdbcTemplate jdbcTemplate) {

        this.insertImportVersionCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("farms_webapp_pkg")
                .withProcedureName("insert_import_version")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter("io_import_version_id", Types.BIGINT),
                        new SqlParameter("in_import_class", Types.VARCHAR),
                        new SqlParameter("in_import_state", Types.VARCHAR),
                        new SqlParameter("in_description", Types.VARCHAR),
                        new SqlParameter("in_import_file_name", Types.VARCHAR),
                        new SqlParameter("in_import_file_pwd", Types.VARCHAR),
                        new SqlParameter("in_import_file", Types.BINARY),
                        new SqlParameter("in_user", Types.VARCHAR));
    }

    public void insertImportVersion(ImportVersionEntity dto) throws DaoException {
        Map<String, Object> in = new HashMap<>() {
            {
                put("in_import_class", dto.getImportClassCode());
                put("in_import_state", dto.getImportStateCode());
                put("in_description", dto.getDescription());
                put("in_import_file_name", dto.getImportFileName());
                put("in_import_file_pwd", null); // passwords no longer used
                put("in_import_file", dto.getImportFile());
                put("in_user", dto.getImportedByUser());
            }
        };

        Map<String, Object> out = insertImportVersionCall.execute(in);

        dto.setImportVersionId(((Number) out.get("io_import_version_id")).longValue());
    }
}
