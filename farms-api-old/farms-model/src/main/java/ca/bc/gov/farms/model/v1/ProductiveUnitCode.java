package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface ProductiveUnitCode extends Serializable {

    public String getCode();
    public void setCode(String code);

    public String getDescription();
    public void setDescription(String description);
}
