package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;

public interface InventoryTypeXrefDao extends Serializable {

    InventoryTypeXrefDto fetch(Long agristabilityCommodityXrefId) throws DaoException;

    List<InventoryTypeXrefDto> fetchByInventoryClassCode(String inventoryClassCode) throws DaoException;

    void insert(InventoryTypeXrefDto dto, String userId) throws DaoException;

    void update(InventoryTypeXrefDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long agristabilityCommodityXrefId) throws DaoException, NotFoundDaoException;
}
