package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;

public interface MarketRatePremiumDao extends Serializable {

    MarketRatePremiumDto fetch(Long marketRatePremiumId) throws DaoException;

    List<MarketRatePremiumDto> fetchAll() throws DaoException;

    void insert(MarketRatePremiumDto dto, String userId) throws DaoException;

    void update(MarketRatePremiumDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Long marketRatePremiumId) throws DaoException, NotFoundDaoException;
}
