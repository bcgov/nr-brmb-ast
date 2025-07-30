package ca.bc.gov.farms.service.api.v1;

import java.io.InputStream;
import java.util.List;

public interface ImportFMVService {

    List<Object> getStagingErrors();

    void importCSV(Long importVersionId, InputStream inputStream, String userId);

    void processImport(Long importVersionId, String userId);
}
