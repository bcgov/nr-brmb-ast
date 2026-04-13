package ca.bc.gov.farms.data.repositories;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.ImportBPUEntity;

@Repository
public class ImportBPURepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertStagingRow(ImportBPUEntity dto, String userId, int rowNum) {

        jdbcTemplate.execute(
                "call farms_bpu_pkg.insert_staging_row(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    cs.setLong(1, rowNum);
                    cs.setObject(2, dto.getProgramYear() == null ? null : dto.getProgramYear().shortValue(),
                            Types.SMALLINT);
                    cs.setString(3, dto.getMunicipalityCode());
                    cs.setString(4, dto.getInventoryItemCode());
                    cs.setString(5, dto.getUnitComment());
                    cs.setBigDecimal(6, dto.getYearMinus6Margin());
                    cs.setBigDecimal(7, dto.getYearMinus5Margin());
                    cs.setBigDecimal(8, dto.getYearMinus4Margin());
                    cs.setBigDecimal(9, dto.getYearMinus3Margin());
                    cs.setBigDecimal(10, dto.getYearMinus2Margin());
                    cs.setBigDecimal(11, dto.getYearMinus1Margin());
                    cs.setBigDecimal(12, dto.getYearMinus6Expense());
                    cs.setBigDecimal(13, dto.getYearMinus5Expense());
                    cs.setBigDecimal(14, dto.getYearMinus4Expense());
                    cs.setBigDecimal(15, dto.getYearMinus3Expense());
                    cs.setBigDecimal(16, dto.getYearMinus2Expense());
                    cs.setBigDecimal(17, dto.getYearMinus1Expense());
                    cs.setString(18, dto.getFileLocation());
                    cs.setString(19, userId);

                    cs.execute();

                    return null;
                });
    }

    public void clearStaging() {

        jdbcTemplate.execute("call farms_bpu_pkg.clear_staging()", (CallableStatement cs) -> {

            cs.execute();

            return null;
        });
    }

    public void deleteStagingErrors(Long importVersionId) {

        jdbcTemplate.execute("call farms_bpu_pkg.delete_staging_errors(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public void validateStaging(Long importVersionId) {

        jdbcTemplate.execute("call farms_bpu_pkg.validate_staging(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public List<String> getStagingErrors(Long importVersionId) {

        List<String> importLogDtoList = new ArrayList<>();

        jdbcTemplate.execute("{ ? = call farms_bpu_pkg.get_staging_errors(?) }", (CallableStatement cs) -> {

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

        jdbcTemplate.execute("call farms_bpu_pkg.staging_to_operational(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);
            cs.setString(2, userId);

            cs.execute();

            return null;
        });
    }
}
