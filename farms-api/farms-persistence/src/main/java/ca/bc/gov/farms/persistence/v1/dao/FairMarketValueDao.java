package ca.bc.gov.farms.persistence.v1.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public interface FairMarketValueDao extends Serializable {

    List<FairMarketValueDto> fetchByProgramYear(Map<String, Object> parameters);
}
