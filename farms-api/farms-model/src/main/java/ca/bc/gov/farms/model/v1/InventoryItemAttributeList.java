package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface InventoryItemAttributeList<E extends InventoryItemAttribute> extends Serializable {

    public List<E> getInventoryItemAttributeList();

    public void setInventoryItemAttributeList(List<E> inventoryItemAttributeList);
}
