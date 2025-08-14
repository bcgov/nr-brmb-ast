package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ImportDao;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;

public class ImportDaoImpl extends BaseDao implements ImportDao {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ImportDaoImpl.class);

    private Connection conn;

    public ImportDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public void insertImportVersion(ImportVersionDto dto) throws DaoException {
        logger.debug("<insertImportVersion");

        try (CallableStatement callableStatement = this.conn
                .prepareCall("call farms_webapp_pkg.insert_import_version(?, ?, ?, ?, ?, ?, ?, ?)")) {
            callableStatement.registerOutParameter(1, Types.BIGINT);
            callableStatement.setString(2, dto.getImportClassCode());
            callableStatement.setString(3, dto.getImportStateCode());
            callableStatement.setString(4, dto.getDescription());
            callableStatement.setString(5, dto.getImportFileName());
            callableStatement.setString(6, null); // passwords no longer used
            callableStatement.setBytes(7, dto.getImportFile());
            callableStatement.setString(8, dto.getImportedByUser());
            callableStatement.execute();
            dto.setImportVersionId(callableStatement.getLong(1));
        } catch (RuntimeException | SQLException e) {
            handleException(e);
        }

        logger.debug(">insertImportVersion");
    }

}
