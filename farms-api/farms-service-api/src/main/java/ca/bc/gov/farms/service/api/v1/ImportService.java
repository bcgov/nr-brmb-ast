package ca.bc.gov.farms.service.api.v1;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.persistence.v1.dto.ImportVersionDto;

public interface ImportService {

    ImportVersionDto createImportVersion(String importClassCode, String importStateCode, String description,
            String fileName, byte[] fileContent, String userId) throws ServiceException;
}
