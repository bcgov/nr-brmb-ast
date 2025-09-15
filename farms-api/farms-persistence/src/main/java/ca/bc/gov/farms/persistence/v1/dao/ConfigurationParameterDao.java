package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;

public interface ConfigurationParameterDao extends Serializable {

    ConfigurationParameterDto fetch(Long ConfigurationParameterId) throws DaoException;

    ConfigurationParameterDto fetchAll() throws DaoException;

    void insert(ConfigurationParameterDto dto, String userId) throws DaoException;

    void update(ConfigurationParameterDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long configurationParameterId) throws DaoException, NotFoundDaoException;
}
