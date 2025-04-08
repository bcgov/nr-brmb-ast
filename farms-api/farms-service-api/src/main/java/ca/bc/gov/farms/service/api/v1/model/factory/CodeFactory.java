package ca.bc.gov.farms.service.api.v1.model.factory;

import ca.bc.gov.nrs.wfone.common.model.Code;
import ca.bc.gov.nrs.wfone.common.persistence.code.dto.CodeDto;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.nrs.wfone.common.service.api.model.factory.FactoryException;

public interface CodeFactory {

    public Code getCode(CodeDto dto, FactoryContext context) throws FactoryException;
}
