package mycrm.services;

import mycrm.upload.DiallerExportEntity;

import java.util.Set;

public interface DiallerDataManipulationService {
    Set<DiallerExportEntity> getAllDiallerData();

    Set<DiallerExportEntity> getLatestDiallerData();
}
