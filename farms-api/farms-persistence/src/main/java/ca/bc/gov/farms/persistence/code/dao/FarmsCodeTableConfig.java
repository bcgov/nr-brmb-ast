package ca.bc.gov.farms.persistence.code.dao;

import ca.bc.gov.brmb.common.persistence.code.dao.CodeTableConfig;

public class FarmsCodeTableConfig extends CodeTableConfig {

    private String codeCodeName;

    public String getCodeCodeName() {
        return codeCodeName;
    }

    public void setCodeCodeName(String codeCodeName) {
        this.codeCodeName = codeCodeName;
    }
}
