package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface FruitVegTypeDetailList<E extends FruitVegTypeDetail> extends Serializable {

    public List<E> getFruitVegTypeDetailList();

    public void setFruitVegTypeDetailList(List<E> fruitVegTypeDetails);
}
