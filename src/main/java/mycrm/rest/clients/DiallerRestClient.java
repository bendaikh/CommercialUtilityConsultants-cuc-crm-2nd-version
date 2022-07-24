package mycrm.rest.clients;

import mycrm.models.DiallerAPIResponse;
import mycrm.upload.DiallerDatasetResponse;
import mycrm.upload.DiallerDatasetType;

public interface DiallerRestClient {
    DiallerAPIResponse getDiallerToken();

    DiallerAPIResponse sendDataToDiallerServer(String filePath, DiallerDatasetType type);

    void updateDataset(String dataset, String status);

    DiallerDatasetResponse getDatasets();
}
