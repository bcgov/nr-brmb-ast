package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;

public interface ExpectedProductionDao extends Serializable {

    ExpectedProductionDto fetch(Long expectedProductionId) throws DaoException;

    List<ExpectedProductionDto> fetchByInventoryItemCode(String inventoryItemCode) throws DaoException;

    List<ExpectedProductionDto> fetchAll() throws DaoException;

    void insert(ExpectedProductionDto dto, String userId) throws DaoException;

    void update(ExpectedProductionDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long expectedProductionId) throws DaoException, NotFoundDaoException;
}
