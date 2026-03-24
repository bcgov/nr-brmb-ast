package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface YearConfigurationParameterList<E extends YearConfigurationParameter> extends Serializable {

    public List<E> getYearConfigurationParameterList();

    public void setYearConfigurationParameterList(List<E> yearConfigurationParameterList);
}
