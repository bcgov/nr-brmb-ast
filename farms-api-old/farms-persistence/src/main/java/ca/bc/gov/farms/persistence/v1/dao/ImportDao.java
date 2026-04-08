package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;

public interface ImportDao extends Serializable {

    void insertImportVersion(ImportVersionDto dto) throws DaoException;
}
