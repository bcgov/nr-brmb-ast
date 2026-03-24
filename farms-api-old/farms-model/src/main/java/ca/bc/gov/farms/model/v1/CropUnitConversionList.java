package ca.bc.gov.farms.model.v1;

import java.io.Serializable;
import java.util.List;

public interface CropUnitConversionList<E extends CropUnitConversion> extends Serializable {

    public List<E> getCropUnitConversionList();

    public void setCropUnitConversionList(List<E> cropUnitConversionList);
}
