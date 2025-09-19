package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface FruitVegTypeDetailList extends Serializable {

    public List<FruitVegTypeDetail> getFruitVegTypeDetails();

    public void setFruitVegTypeDetails(List<FruitVegTypeDetail> fruitVegTypeDetails);
}
