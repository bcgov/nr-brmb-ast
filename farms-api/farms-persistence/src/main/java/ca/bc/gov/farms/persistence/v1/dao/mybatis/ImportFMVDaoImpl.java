package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportFMVDao;

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
}
