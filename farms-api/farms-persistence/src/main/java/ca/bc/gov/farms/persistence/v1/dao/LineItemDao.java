package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;

public interface LineItemDao extends Serializable {

    LineItemDto fetch(Long LineItemId) throws DaoException;

    List<LineItemDto> fetchAll() throws DaoException;

    void insert(LineItemDto dto, String userId) throws DaoException;

    void update(LineItemDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long lineItemId) throws DaoException, NotFoundDaoException;
}
