package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;

public interface MarketRatePremiumMapper {

    MarketRatePremiumDto fetch(Map<String, Object> parameters);

    List<MarketRatePremiumDto> fetchAll();

    int insertMarketRatePremium(Map<String, Object> parameters);

    int updateMarketRatePremium(Map<String, Object> parameters);

    int deleteMarketRatePremium(Map<String, Object> parameters);
}
