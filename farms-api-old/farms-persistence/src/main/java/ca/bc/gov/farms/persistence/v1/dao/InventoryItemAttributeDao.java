package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;

public interface InventoryItemAttributeDao extends Serializable {

    InventoryItemAttributeDto fetch(Long inventoryItemAttributeId) throws DaoException;

    InventoryItemAttributeDto fetchByInventoryItemCode(String inventoryItemCode) throws DaoException;

    void insert(InventoryItemAttributeDto dto, String userId) throws DaoException;

    void update(InventoryItemAttributeDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long inventoryItemAttributeId) throws DaoException, NotFoundDaoException;
}
