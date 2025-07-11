package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.persistence.dao.NotFoundDaoException;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public interface FairMarketValueDao extends Serializable {

    FairMarketValueDto fetch(Integer programYear, String fairMarketValueId) throws DaoException;

    List<FairMarketValueDto> fetchByProgramYear(Integer programYear) throws DaoException;

    List<FairMarketValueDto> fetchBy(Integer programYear, String inventoryItemCode, String municipalityCode,
            String cropUnitCode) throws DaoException;

    void insert(FairMarketValueDto dto, String userId) throws DaoException;

    void update(FairMarketValueDto dto, String userId) throws DaoException, NotFoundDaoException;

    void delete(Integer programYear, String fairMarketValueId) throws DaoException, NotFoundDaoException;
}
