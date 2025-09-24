package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;

public interface CropUnitConversionDao extends Serializable {

    CropUnitConversionDto fetch(Long cropUnitConversionFactorId) throws DaoException;

    List<CropUnitConversionDto> fetchByInventoryItemCode(String inventoryItemCode) throws DaoException;

    void insert(CropUnitConversionDto dto, String userId) throws DaoException;

    void update(CropUnitConversionDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long cropUnitConversionFactorId) throws DaoException, NotFoundDaoException;
}
