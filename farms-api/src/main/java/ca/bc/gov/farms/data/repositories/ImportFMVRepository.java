package ca.bc.gov.farms.data.repositories;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.ImportFMVEntity;

@Repository
public class ImportFMVRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clearStaging() {

        jdbcTemplate.execute("call farms_fmv_pkg.clear_staging()", (CallableStatement cs) -> {

            cs.execute();

            return null;
        });
    }

    public void insertStagingRow(ImportFMVEntity dto, String userId, int rowNum) {

        jdbcTemplate.execute("call farms_fmv_pkg.insert_staging_row(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    cs.setLong(1, rowNum);
                    cs.setObject(2, dto.getProgramYear() == null ? null : dto.getProgramYear().shortValue(),
                            Types.SMALLINT);
                    cs.setObject(3, dto.getPeriod() == null ? null : dto.getPeriod().shortValue(),
                            Types.SMALLINT);
                    cs.setBigDecimal(4, dto.getAveragePrice());
                    cs.setBigDecimal(5, dto.getPercentVariance());
                    cs.setString(6, dto.getMunicipalityCode());
                    cs.setString(7, dto.getCropUnitCode());
                    cs.setString(8, dto.getInventoryItemCode());
                    cs.setString(9, dto.getFileLocation());
                    cs.setString(10, userId);

                    cs.execute();

                    return null;
                });
    }

    public void validateStaging(Long importVersionId) {

        jdbcTemplate.execute("call farms_fmv_pkg.validate_staging(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public void deleteStagingErrors(Long importVersionId) {

        jdbcTemplate.execute("call farms_fmv_pkg.delete_staging_errors(?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);

            cs.execute();

            return null;
        });
    }

    public List<String> getStagingErrors(Long importVersionId) {

        List<String> importLogDtoList = new ArrayList<>();

        jdbcTemplate.execute("{ ? = call farms_fmv_pkg.get_staging_errors(?) }", (CallableStatement cs) -> {

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

        jdbcTemplate.execute("call farms_fmv_pkg.staging_to_operational(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, importVersionId);
            cs.setString(2, userId);

            cs.execute();

            return null;
        });
    }
}
