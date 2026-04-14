package ca.bc.gov.farms.data.repositories;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.ImportIVPREntity;

@Repository
public class ImportIVPRRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clearStaging() {

        jdbcTemplate.execute("call farms_ivpr_pkg.clear_staging()", (CallableStatement cs) -> {

            cs.execute();

            return null;
        });
    }

    public void insertStagingRow(ImportIVPREntity dto, String userId, int rowNum) {

        jdbcTemplate.execute("call farms_ivpr_pkg.insert_staging_row(?, ?, ?, ?, ?, ?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, rowNum);
            cs.setObject(2, dto.getProgramYear() == null ? null : dto.getProgramYear().shortValue(),
                    Types.SMALLINT);
            cs.setString(3, dto.getInventoryItemCode());
            cs.setBigDecimal(4, dto.getInsurableValue());
            cs.setBigDecimal(5, dto.getPremiumRate());
            cs.setString(6, dto.getFileLocation());
            cs.setString(7, userId);

            cs.execute();

            return null;
        });
    }

    public void validateStaging(Long importVersionId) {

        jdbcTemplate.execute("call farms_ivpr_pkg.validate_staging(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public void deleteStagingErrors(Long importVersionId) {

        jdbcTemplate.execute("call farms_ivpr_pkg.delete_staging_errors(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public List<String> getStagingErrors(Long importVersionId) {

        List<String> importLogDtoList = new ArrayList<>();

        jdbcTemplate.execute("{ ? = call farms_ivpr_pkg.get_staging_errors(?) }", (CallableStatement cs) -> {

            cs.registerOutParameter(1, Types.OTHER);
            cs.setLong(2, importVersionId);
            cs.execute();

            ResultSet resultSet = cs.getObject(1, ResultSet.class);
            while (resultSet.next()) {
                String logMessage = resultSet.getString("log_message");
                importLogDtoList.add(logMessage);
            }
            resultSet.close();

            return null;
        });

        return importLogDtoList;
    }

    public void performImport(Long importVersionId, String userId) {

        jdbcTemplate.execute("call farms_ivpr_pkg.staging_to_operational(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);
            cs.setString(2, userId);

            cs.execute();

            return null;
        });
    }
}
