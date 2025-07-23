package ca.bc.gov.farms.persistence.v1.dao.mybatis;

import java.math.BigDecimal;

import ca.bc.gov.brmb.common.persistence.dao.mybatis.BaseDao;

public class BaseDaoImpl extends BaseDao {

    protected BigDecimal toBigDecimal(Integer i) {
        return i == null ? null : BigDecimal.valueOf(i);
    }

    protected BigDecimal toBigDecimal(Double d) {
        return d == null ? null : BigDecimal.valueOf(d);
    }

    protected String toString(Boolean b) {
        return b == null ? null : b ? "Y" : "N";
    }
}
