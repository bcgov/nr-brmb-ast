package ca.bc.gov.farms.data.repositories;

import java.sql.CallableStatement;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.bc.gov.farms.data.entities.ImportVersionEntity;

@Repository
public class ImportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertImportVersion(ImportVersionEntity dto) {

        jdbcTemplate.execute("call farms_webapp_pkg.insert_import_version(?, ?, ?, ?, ?, ?, ?, ?)",
                (CallableStatement cs) -> {

                    cs.registerOutParameter(1, Types.BIGINT);

                    cs.setString(2, dto.getImportClassCode());
                    cs.setString(3, dto.getImportStateCode());
                    cs.setString(4, dto.getDescription());
                    cs.setString(5, dto.getImportFileName());
                    cs.setString(6, null); // passwords no longer used
                    cs.setBytes(7, dto.getImportFile());
                    cs.setString(8, dto.getImportedByUser());

                    cs.execute();

                    dto.setImportVersionId(cs.getLong(1));
                    return dto.getImportVersionId();
                });
    }
}
