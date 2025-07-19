package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;
import ca.bc.gov.farms.persistence.v1.dao.ErrorDao;

public class ErrorDaoImpl extends BaseDao implements ErrorDao {

    private static final long serialVersionUID = 1L;

    private Connection conn;

    public ErrorDaoImpl(Connection c) {
        this.conn = c;
    }

    @Override
    public String codify(String msg) throws DaoException {
        int i = 1;
        try (CallableStatement callableStatement = this.conn
                .prepareCall("{ ? = call farms_error_pkg.codify(?) }")) {
            callableStatement.registerOutParameter(i++, Types.VARCHAR);
            callableStatement.setString(i++, msg);
            callableStatement.execute();
            return callableStatement.getString(1);
        } catch (SQLException e) {
            handleException(e);
            return msg;
        }
    }

}
