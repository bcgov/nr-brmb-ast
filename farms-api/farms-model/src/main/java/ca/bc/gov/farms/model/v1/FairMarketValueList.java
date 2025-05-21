package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface FairMarketValueList<E extends FairMarketValue> extends Serializable {

    public List<E> getFairMarketValueList();

    public void setFairMarketValueList(List<E> fairMarketValueList);
}
