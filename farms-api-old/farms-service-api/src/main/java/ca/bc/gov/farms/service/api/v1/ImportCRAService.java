package ca.bc.gov.farms.service.api.v1;

import java.io.InputStream;

public interface ImportCRAService {

    void importCSV(Long importVersionId, InputStream inputStream, String userId);

    void processImport(Long pImportVersionId, String userId);
}
