package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportFMVDao;
import ca.bc.gov.farms.persistence.v1.dto.ImportFMVDto;

public class ImportFMVDaoImpl extends BaseDao implements ImportFMVDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportFMVDaoImpl.class);

    private Connection conn;

    public ImportFMVDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void clearStaging() throws DaoException {
        logger.debug("<clearStaging");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_fmv_pkg.clear_staging()")) {
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">clearStaging");
    }

    @Override
    public void insertStagingRow(ImportFMVDto dto, String userId, int rowNum) throws DaoException {
        logger.debug("<insertStagingRow");

        try (CallableStatement callableStatement = this.conn
                .prepareCall(
                        "call farms_fmv_pkg.insert_staging_row(?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            callableStatement.setInt(1, rowNum);
            callableStatement.setInt(2, dto.getProgramYear());
            callableStatement.setInt(3, dto.getPeriod());
            callableStatement.setBigDecimal(4, dto.getAveragePrice());
            callableStatement.setBigDecimal(5, dto.getPercentVariance());
            callableStatement.setString(6, dto.getMunicipalityCode());
            callableStatement.setString(7, dto.getCropUnitCode());
            callableStatement.setString(8, dto.getInventoryItemCode());
            callableStatement.setString(9, userId);
            callableStatement.execute();
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">insertStagingRow");
    }
}
