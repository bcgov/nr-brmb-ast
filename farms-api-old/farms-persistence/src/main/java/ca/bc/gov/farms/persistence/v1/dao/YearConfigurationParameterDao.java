package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;

public interface YearConfigurationParameterDao extends Serializable {

    YearConfigurationParameterDto fetch(Long yearConfigurationParameterId) throws DaoException;

    List<YearConfigurationParameterDto> fetchAll() throws DaoException;

    void insert(YearConfigurationParameterDto dto, String userId) throws DaoException;

    void update(YearConfigurationParameterDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long yearConfigurationParameterId) throws DaoException, NotFoundDaoException;
}
