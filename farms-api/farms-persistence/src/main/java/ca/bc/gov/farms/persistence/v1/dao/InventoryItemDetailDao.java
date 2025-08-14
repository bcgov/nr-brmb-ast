package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;

public interface InventoryItemDetailDao extends Serializable {

    InventoryItemDetailDto fetch(Long inventoryItemDetailId) throws DaoException;

    List<InventoryItemDetailDto> fetchByInventoryItemCode(String inventoryItemCode) throws DaoException;

    void insert(InventoryItemDetailDto dto, String userId) throws DaoException;

    void update(InventoryItemDetailDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long inventoryItemDetailId) throws DaoException, NotFoundDaoException;
}
