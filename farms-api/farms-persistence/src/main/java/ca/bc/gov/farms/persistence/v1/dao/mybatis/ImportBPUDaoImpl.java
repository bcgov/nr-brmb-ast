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
import ca.bc.gov.farms.persistence.v1.dao.ImportBPUDao;
import ca.bc.gov.farms.persistence.v1.dto.ImportBPUDto;

public class ImportBPUDaoImpl extends BaseDao implements ImportBPUDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportBPUDaoImpl.class);

    private Connection conn;

    public ImportBPUDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void insertStagingRow(ImportBPUDto dto, String userId, int rowNum) throws DaoException {
        logger.debug("<insertStagingRow");

        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "{ call farms_bpu_pkg.insert_staging_row(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }")) {
            callableStatement.setInt(1, rowNum);
            callableStatement.setInt(2, dto.getProgramYear());
            callableStatement.setString(3, dto.getMunicipalityCode());
            callableStatement.setString(4, dto.getInventoryItemCode());
            callableStatement.setString(5, dto.getUnitComment());
            callableStatement.setBigDecimal(6, dto.getYearMinus6Margin());
            callableStatement.setBigDecimal(7, dto.getYearMinus5Margin());
            callableStatement.setBigDecimal(8, dto.getYearMinus4Margin());
            callableStatement.setBigDecimal(9, dto.getYearMinus3Margin());
            callableStatement.setBigDecimal(10, dto.getYearMinus2Margin());
            callableStatement.setBigDecimal(11, dto.getYearMinus1Margin());
            callableStatement.setBigDecimal(12, dto.getYearMinus6Expense());
            callableStatement.setBigDecimal(13, dto.getYearMinus5Expense());
            callableStatement.setBigDecimal(14, dto.getYearMinus4Expense());
            callableStatement.setBigDecimal(15, dto.getYearMinus3Expense());
            callableStatement.setBigDecimal(16, dto.getYearMinus2Expense());
            callableStatement.setBigDecimal(17, dto.getYearMinus1Expense());
            callableStatement.setString(18, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">insertStagingRow");
    }

    @Override
    public void clearStaging() throws DaoException {
        logger.debug("<clearStaging");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ call farms_bpu_pkg.clear_staging() }")) {
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">clearStaging");
    }

    @Override
    public void deleteStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<deleteStagingErrors");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ call farms_bpu_pkg.delete_staging_errors(?) }")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">deleteStagingErrors");
    }

    @Override
    public void validateStaging(Long importVersionId) throws DaoException {
        logger.debug("<validateStaging");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ call farms_bpu_pkg.validate_staging(?) }")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">validateStaging");
    }

    @Override
    public List<String> getStagingErrors(Long importVersionId) throws DaoException {
        logger.debug("<getStagingErrors");

        List<String> importLogDtoList = new ArrayList<>();

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ ? = call farms_bpu_pkg.get_staging_errors(?) }")) {

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
    public void stagingToOperational(Long importVersionId, String userId) throws DaoException {
        logger.debug("<stagingToOperational");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ call farms_bpu_pkg.staging_to_operational(?, ?) }")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.setString(2, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">stagingToOperational");
    }

    @Override
    public void performImport(Long importVersionId, String userId) throws DaoException {
        logger.debug("<performImport");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ call farms_bpu_pkg.perform_import(?, ?) }")) {
            callableStatement.setLong(1, importVersionId);
            callableStatement.setString(2, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">performImport");
    }

}
