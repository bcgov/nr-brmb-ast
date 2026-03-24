package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface InventoryTypeXrefList<E extends InventoryTypeXref> extends Serializable {

    public List<E> getInventoryTypeXrefList();

    public void setInventoryTypeXrefList(List<E> inventoryTypeXrefList);
}
