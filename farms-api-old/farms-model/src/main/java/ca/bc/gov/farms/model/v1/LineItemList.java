package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface LineItemList<E extends LineItem> extends Serializable {

    public List<E> getLineItemList();

    public void setLineItemList(List<E> lineItemList);
}
