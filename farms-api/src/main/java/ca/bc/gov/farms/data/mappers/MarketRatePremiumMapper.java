package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.MarketRatePremiumEntity;

@Mapper
public interface MarketRatePremiumMapper {

    MarketRatePremiumEntity fetch(Long marketRatePremiumId);

    List<MarketRatePremiumEntity> fetchAll();

    int insertMarketRatePremium(MarketRatePremiumEntity dto, String userId);

    int updateMarketRatePremium(MarketRatePremiumEntity dto, String userId);

    int deleteMarketRatePremium(Long marketRatePremiumId);
}
