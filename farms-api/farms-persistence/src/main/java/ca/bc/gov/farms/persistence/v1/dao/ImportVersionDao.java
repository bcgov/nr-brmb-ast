package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;

public interface ImportVersionDao extends Serializable {

    Integer createVersion(String description, String importFileName, String user) throws DaoException;

    void updateControlFileInfoStg(Long versionId, String user) throws DaoException;

    void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) throws DaoException;

    void startImport(Long versionId, String user) throws DaoException;

    void startUpload(Long versionId, String user) throws DaoException;

    void performImport(Long versionId, String user) throws DaoException;

    void uploadFailure(Long versionId, String message, String user);

    void importFailure(Long versionId, String message, String user) throws DaoException;

    void importComplete(Long versionId, String message, String user) throws DaoException;

    void clearSuccessfulTransfers() throws DaoException;
}
