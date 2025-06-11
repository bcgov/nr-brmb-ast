package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;

public interface VersionDao extends Serializable {

    Integer createVersion(String description, String importFileName, String user) throws DaoException;
}
