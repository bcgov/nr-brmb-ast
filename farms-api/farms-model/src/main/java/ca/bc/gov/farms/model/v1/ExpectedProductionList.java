package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface ExpectedProductionList<E extends ExpectedProduction> extends Serializable {

    public List<E> getExpectedProductionList();

    public void setExpectedProductionList(List<E> expectedProductionList);
}
