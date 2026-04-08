package ca.bc.gov.farms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.entities.ImportVersionEntity;
import ca.bc.gov.farms.data.repositories.ImportRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImportService {

    @Autowired
    private ImportRepository importRepository;

    @Transactional
    public ImportVersionEntity createImportVersion(String importClassCode, String importStateCode, String description,
            String fileName, byte[] fileContent, String userId) {

        try {
            ImportVersionEntity importVersionDto = new ImportVersionEntity();
            importVersionDto.setImportClassCode(importClassCode);
            importVersionDto.setImportStateCode(importStateCode);
            importVersionDto.setDescription(description);
            importVersionDto.setImportFileName(fileName);
            importVersionDto.setImportFile(fileContent);
            importVersionDto.setImportedByUser(userId);

            importRepository.insertImportVersion(importVersionDto);
            return importVersionDto;
        } catch (Exception e) {
            log.error("Error creating import version", e);
            throw new ServiceException("Failed to create import version", e);
        }
    }
}
