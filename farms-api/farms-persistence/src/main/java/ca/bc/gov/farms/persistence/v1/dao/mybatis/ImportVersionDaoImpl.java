package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportVersionDao;

public class ImportVersionDaoImpl extends BaseDao implements ImportVersionDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportVersionDaoImpl.class);

    private Connection conn;

    public ImportVersionDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public Integer createVersion(String description, String importFileName, String user) throws DaoException {
        logger.debug("<createVersion");

        Integer versionId = null;

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ ? = call farms_version_pkg.create_version(?, ?, ?) }")) {
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, description);
            callableStatement.setString(3, importFileName);
            callableStatement.setString(4, user);
            callableStatement.execute();
            versionId = callableStatement.getInt(1);
            logger.debug(">createVersion: " + versionId);
            return versionId;
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">createVersion: " + versionId);
        return versionId;
    }

    @Override
    public void updateControlFileInfoStg(Long versionId, String user) throws DaoException {
        logger.debug("<updateControlFileInfoStg");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.update_control_file_info_stg(?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">updateControlFileInfoStg");
    }

    @Override
    public void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) throws DaoException {
        logger.debug("<uploadedVersion");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.uploaded_version(?, ?, ?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, xml);
            callableStatement.setString(3, hasErrorsInd != null && hasErrorsInd ? "Y" : "N");
            callableStatement.setString(4, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">uploadedVersion");
    }

    @Override
    public void startImport(Long versionId, String user) throws DaoException {
        logger.debug("<startImport");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.start_import(?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">startImport");
    }

    @Override
    public void startUpload(Long versionId, String user) throws DaoException {
        logger.debug("<startUpload");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.start_upload(?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">startUpload");
    }

    @Override
    public void performImport(Long versionId, String user) throws DaoException {
        logger.debug("<performImport");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.perform_import(?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">performImport");
    }

    @Override
    public void uploadFailure(Long versionId, String message, String user) {
        logger.debug("<uploadFailure");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.upload_failure(?, ?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, message);
            callableStatement.setString(3, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            // eat it
            logger.error("Unexpected error: ", e);
        }

        logger.debug(">uploadFailure");
    }

    @Override
    public void importFailure(Long versionId, String message, String user) throws DaoException {
        logger.debug("<importFailure");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.import_failure(?, ?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, message);
            callableStatement.setString(3, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">importFailure");
    }

    @Override
    public void importComplete(Long versionId, String message, String user) throws DaoException {
        logger.debug("<importComplete");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.import_complete(?, ?, ?)")) {
            callableStatement.setLong(1, versionId);
            callableStatement.setString(2, message);
            callableStatement.setString(3, user);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">importComplete");
    }

    @Override
    public void clearSuccessfulTransfers() throws DaoException {
        logger.debug("<clearSuccessfulTransfers");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_version_pkg.clear_successful_transfers()")) {
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">clearSuccessfulTransfers");
    }

}
