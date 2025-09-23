package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface MarketRatePremiumList<E extends MarketRatePremium> extends Serializable {

    public List<E> getMarketRatePremiumList();

    public void setMarketRatePremiumList(List<E> marketRatePremiumList);
}
