package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;

public interface StructureGroupAttributeDao extends Serializable {

    StructureGroupAttributeDto fetch(Long structureGroupAttributeId) throws DaoException;

    StructureGroupAttributeDto fetchByStructureGroupCode(String structureGroupCode) throws DaoException;

    void insert(StructureGroupAttributeDto dto, String userId) throws DaoException;

    void update(StructureGroupAttributeDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long structureGroupAttributeId) throws DaoException, NotFoundDaoException;
}
