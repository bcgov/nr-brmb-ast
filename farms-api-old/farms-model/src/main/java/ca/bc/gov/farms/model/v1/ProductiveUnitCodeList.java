package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface ProductiveUnitCodeList<E extends ProductiveUnitCode> extends Serializable {

    public List<E> getProductiveUnitCodeList();

    public void setProductiveUnitCodeList(List<E> productiveUnitCodeList);
}
