package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;

public interface ImportVersionDao extends Serializable {

    Integer createVersion(String description, String importFileName, String user) throws DaoException;

    void updateControlFileInfoStg(Long versionId, String user) throws DaoException;

    void uploadedVersion(Long versionId, String xml, Boolean hasErrorsInd, String user) throws DaoException;

    void startImport(Long versionId, String user) throws DaoException;

    void startUpload(Long versionId, String user) throws DaoException;
}
