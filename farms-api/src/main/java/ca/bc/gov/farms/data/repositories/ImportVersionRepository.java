package ca.bc.gov.farms.data.repositories;

import java.sql.CallableStatement;
import java.sql.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImportVersionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer createVersion(String description, String importFileName, String user) {

        return jdbcTemplate.execute("{ ? = call farms_version_pkg.create_version(?, ?, ?) }",
                (CallableStatement cs) -> {

                    cs.registerOutParameter(1, Types.INTEGER);
                    cs.setString(2, description);
                    cs.setString(3, importFileName);
                    cs.setString(4, user);

                    cs.execute();

                    return cs.getInt(1);
                });
    }

    public void updateControlFileInfoStg(Long versionId, String user) {

        jdbcTemplate.execute("call farms_version_pkg.update_control_file_info_stg(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, user);

            cs.execute();

            return null;
        });
    }

    public void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) {

        jdbcTemplate.execute("call farms_version_pkg.uploaded_version(?, ?, ?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, xml);
            cs.setString(3, hasErrorsInd != null && hasErrorsInd ? "Y" : "N");
            cs.setString(4, user);

            cs.execute();

            return null;
        });
    }

    public void startImport(Long versionId, String user) {

        jdbcTemplate.execute("call farms_version_pkg.start_import(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, user);

            cs.execute();

            return null;
        });
    }

    public void startUpload(Long versionId, String user) {

        jdbcTemplate.execute("call farms_version_pkg.start_upload(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, user);

            cs.execute();

            return null;
        });
    }

    public void performImport(Long versionId, String user) {

        jdbcTemplate.execute("call farms_version_pkg.perform_import(?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, user);

            cs.execute();

            return null;
        });
    }

    public void uploadFailure(Long versionId, String message, String user) {

        jdbcTemplate.execute("call farms_version_pkg.upload_failure(?, ?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, message);
            cs.setString(3, user);

            cs.execute();

            return null;
        });
    }

    public void importFailure(Long versionId, String message, String user) {

        jdbcTemplate.execute("call farms_version_pkg.import_failure(?, ?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, message);
            cs.setString(3, user);

            cs.execute();

            return null;
        });
    }

    public void importComplete(Long versionId, String message, String user) {

        jdbcTemplate.execute("call farms_version_pkg.import_complete(?, ?, ?)", (CallableStatement cs) -> {

            cs.setLong(1, versionId);
            cs.setString(2, message);
            cs.setString(3, user);

            cs.execute();

            return null;
        });
    }

    public void clearSuccessfulTransfers() {

        jdbcTemplate.execute("call farms_version_pkg.clear_successful_transfers()", (CallableStatement cs) -> {

            cs.execute();

            return null;
        });
    }
}
