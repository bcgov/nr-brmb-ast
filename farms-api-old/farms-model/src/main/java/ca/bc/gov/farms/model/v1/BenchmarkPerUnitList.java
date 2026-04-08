package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface BenchmarkPerUnitList<E extends BenchmarkPerUnit> extends Serializable {

    public List<E> getBenchmarkPerUnitList();

    public void setBenchmarkPerUnitList(List<E> benchmarkPerUnitList);
}
