package mycrm.services;

import mycrm.upload.DiallerDataset;
import mycrm.upload.DiallerExportEntity;

import java.util.List;
import java.util.Set;

public interface DiallerExportService {
    void exportAllDiallerData();

    void exportLatestDiallerData();

    List<DiallerExportEntity> saveAll(Set diallerExports);

    List<DiallerDataset> getDatasets();

    void updateDataset(String dataset, String status);
}
