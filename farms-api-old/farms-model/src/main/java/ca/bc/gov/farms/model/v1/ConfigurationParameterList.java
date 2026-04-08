package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface ConfigurationParameterList<E extends ConfigurationParameter> extends Serializable {

    public List<E> getConfigurationParameterList();

    public void setConfigurationParameterList(List<E> configurationParameterList);
}
