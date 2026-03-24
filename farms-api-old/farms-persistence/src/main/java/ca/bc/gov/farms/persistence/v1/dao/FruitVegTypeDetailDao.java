package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;

public interface FruitVegTypeDetailDao extends Serializable {

    FruitVegTypeDetailDto fetch(String fruitVegTypeCode) throws DaoException;

    List<FruitVegTypeDetailDto> fetchAll() throws DaoException;

    void insert(FruitVegTypeDetailDto dto, String userId) throws DaoException;

    void update(FruitVegTypeDetailDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(String fruitVegTypeCode) throws DaoException, NotFoundDaoException;
}
