package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public interface FairMarketValueDao extends Serializable {

    FairMarketValueDto fetch(Integer programYear, String fairMarketValueId) throws DaoException;

    List<FairMarketValueDto> fetchByProgramYear(Integer programYear) throws DaoException;
}
