package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportIVPRDao;
import ca.bc.gov.farms.persistence.v1.dto.ImportIVPRDto;

public class ImportIVPRDaoImpl extends BaseDao implements ImportIVPRDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportIVPRDaoImpl.class);

    private Connection conn;

    public ImportIVPRDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void clearStaging() throws DaoException {
        logger.debug("<clearStaging");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_ivpr_pkg.clear_staging()")) {
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">clearStaging");
    }

    @Override
    public void insertStagingRow(ImportIVPRDto dto, String userId, int rowNum) throws DaoException {
        logger.debug("<insertStagingRow");

        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_ivpr_pkg.insert_staging_row(?, ?, ?, ?, ?, ?)")) {
            callableStatement.setInt(1, rowNum);
            callableStatement.setInt(2, dto.getProgramYear());
            callableStatement.setString(3, dto.getInventoryItemCode());
            callableStatement.setBigDecimal(4, dto.getInsurableValue());
            callableStatement.setBigDecimal(5, dto.getPremiumRate());
            callableStatement.setString(6, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">insertStagingRow");
    }

    @Override
    public void validateStaging(Long importVersionId) throws DaoException {
        logger.debug("<validateStaging");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_ivpr_pkg.validate_staging(?)")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">validateStaging");
    }

    @Override
    public void deleteStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<deleteStagingErrors");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_ivpr_pkg.delete_staging_errors(?)")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">deleteStagingErrors");
    }

    @Override
    public List<String> getStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<getStagingErrors");

        List<String> importLogDtoList = new ArrayList<>();

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ ? = call farms_ivpr_pkg.get_staging_errors(?) }")) {

            callableStatement.registerOutParameter(1, Types.OTHER);
            callableStatement.setLong(2, importVersionId);
            callableStatement.execute();

            ResultSet resultSet = callableStatement.getObject(1, ResultSet.class);
            while (resultSet.next()) {
                String logMessage = resultSet.getString("log_message");
                importLogDtoList.add(logMessage);
            }
            resultSet.close();

        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">getStagingErrors: " + importLogDtoList.size());
        return importLogDtoList;
    }

    @Override
    public void performImport(Long importVersionId, String userId) throws DaoException {
        logger.debug("<performImport");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_ivpr_pkg.staging_to_operational(?, ?)")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.setString(2, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">performImport");
    }
}
