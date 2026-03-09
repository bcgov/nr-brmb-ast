package ca.bc.gov.srm.farm.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class DAOUtils {

    public static void setLong(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BIGINT);
        } else {
            ps.setLong(index, value.longValue());
        }
    }

    public static void setInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value.intValue());
        }
    }

    public static void setShort(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.SMALLINT);
        } else {
            ps.setShort(index, value.shortValue());
        }
    }

    public static void setDate(PreparedStatement ps, int index, Date value) throws SQLException {
        ps.setDate(index, value == null ? null : new java.sql.Date(value.getTime()));
    }
}
