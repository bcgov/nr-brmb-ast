package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.persistence.v1.dto.ImportFMVDto;

public interface ImportFMVDao extends Serializable {

    void clearStaging() throws DaoException;

    void insertStagingRow(ImportFMVDto dto, String userId, int rowNum) throws DaoException;

    void validateStaging(Long importVersionId) throws DaoException;

    void deleteStagingErrors(Long importVersionId) throws DaoException;

    List<String> getStagingErrors(Long importVersionId) throws DaoException;

    void performImport(Long importVersionId, String userId) throws DaoException;
}
