package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;

public interface ProductiveUnitCodeDao extends Serializable {

    List<ProductiveUnitCodeDto> fetchAll() throws DaoException;
}
