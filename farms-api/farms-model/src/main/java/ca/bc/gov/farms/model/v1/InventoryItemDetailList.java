package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface InventoryItemDetailList<E extends InventoryItemDetail> extends Serializable {

    public List<E> getInventoryItemDetailList();

    public void setInventoryItemDetailList(List<E> inventoryItemDetailList);
}
