package mycrm.services;

import mycrm.models.DiallerAPIResponse;
import mycrm.repositories.DiallerExportRepository;
import mycrm.rest.clients.DiallerRestClient;
import mycrm.upload.DiallerDataCollection;
import mycrm.upload.DiallerDataset;
import mycrm.upload.DiallerDatasetResponse;
import mycrm.upload.DiallerDatasetType;
import mycrm.upload.DiallerExportEntity;
import mycrm.upload.DiallerExportJSONWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Primary
public class DiallerExportServiceImpl implements DiallerExportService {
    private static Logger logger = LogManager.getLogger();
    private final DiallerExportRepository diallerExportRepository;
    private final DiallerRestClient diallerRestClient;
    private final DiallerExportJSONWriter writer;
    private final DiallerDataManipulationService diallerDataManipulationService;
    private final EmailTemplateService emailTemplateService;

    @Autowired
    public DiallerExportServiceImpl(DiallerExportRepository diallerExportRepository,
                                    DiallerRestClient diallerRestClient,
                                    DiallerExportJSONWriter writer,
                                    DiallerDataManipulationService diallerDataManipulationService, EmailTemplateService emailTemplateService) {
        this.diallerExportRepository = diallerExportRepository;
        this.diallerRestClient = diallerRestClient;
        this.writer = writer;
        this.diallerDataManipulationService = diallerDataManipulationService;
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    public void exportAllDiallerData() {

        Set<DiallerExportEntity> allDiallerData = diallerDataManipulationService.getAllDiallerData();
        processDiallerData(allDiallerData);
    }

    @Override
    public void exportLatestDiallerData() {
        Set<DiallerExportEntity> latestDiallerData = diallerDataManipulationService.getLatestDiallerData();
        processDiallerData(latestDiallerData);
    }

    private void processDiallerData(Set<DiallerExportEntity> diallerData) {

        DiallerDataCollection dataCollection = filterDiallerDataForDuplicates(diallerData);

        Set<DiallerExportEntity> newDiallerDataSet = dataCollection.getNewDiallerData();
        Set<DiallerExportEntity> updatedDiallerDataSet = dataCollection.getUpdateDiallerData();

        if (newDiallerDataSet.isEmpty() && updatedDiallerDataSet.isEmpty()) {
            logger.info("No latest Dialler data to upload.");
            try {
                StringBuilder failedEmailBody = new StringBuilder();
                failedEmailBody.append("<p>No data was uploaded today</p>");
                emailTemplateService.sendDiallerUploadNotNeeded(failedEmailBody.toString());
            } catch (Exception e) {
                logger.info("Failed to send lack of data email");
            }
            return;
        }

        try {
            List<DiallerExportEntity> savedNewDiallerEntities = saveAll(newDiallerDataSet);
            List<DiallerExportEntity> savedUpdatedDiallerEntities = saveAll(updatedDiallerDataSet);

            Set<DiallerExportEntity> newDiallerExportEntitiesSet = new HashSet<>(savedNewDiallerEntities);
            Set<DiallerExportEntity> updatedDiallerExportEntitiesSet = new HashSet<>(savedUpdatedDiallerEntities);

            if (!newDiallerExportEntitiesSet.isEmpty()) {
                sendDiallerDataset(newDiallerExportEntitiesSet, DiallerDatasetType.CREATE);
            }

            if (!updatedDiallerExportEntitiesSet.isEmpty()) {
                sendDiallerDataset(updatedDiallerExportEntitiesSet, DiallerDatasetType.UPDATE);
            }

            logger.info("Dialler upload process ended.");

        } catch (Exception e) {
            logger.error("Failed to create file for the Dialler exports. {}", e.getMessage());
        }

    }

    private void sendDiallerDataset(Set<DiallerExportEntity> dataset, DiallerDatasetType type) throws Exception {

        String filePath = writer.buildJSON(dataset, type);

        DiallerAPIResponse apiResponse = diallerRestClient.sendDataToDiallerServer(filePath, type);

        if (apiResponse.isSuccess()) {
            emailTemplateService.sendDiallerSuccessEmailAlert(apiResponse, type);
        } else {
            emailTemplateService.sendDiallerFailedEmailAlert(apiResponse, type);
        }
    }

    private DiallerDataCollection filterDiallerDataForDuplicates(Set<DiallerExportEntity> allDiallerData) {

        Set<DiallerExportEntity> newDiallerData = new HashSet<>();
        Set<DiallerExportEntity> updateDiallerData = new HashSet<>();

        allDiallerData.forEach(entity -> {
            DiallerExportEntity foundRecord =
                    this.diallerExportRepository.filterExistingDiallerRecords(
                            entity.getUniqueId(),
                            entity.getContractType(),
                            entity.getLeadStatus(),
                            entity.getPhone(),
                            entity.getMobile(),
                            entity.getDataset());

            if (foundRecord == null) {
                newDiallerData.add(entity);
            } else {
                if ("CALLBACK".equals(entity.getLeadStatus())) {
                    entity.setId(foundRecord.getId());
                    updateDiallerData.add(entity);
                }
            }
        });

        return new DiallerDataCollection(newDiallerData, updateDiallerData);
    }

    @Override
    public List<DiallerExportEntity> saveAll(Set diallerExports) {
        return this.diallerExportRepository.save(diallerExports);
    }

    @Override
    public List<DiallerDataset> getDatasets() {
        DiallerDatasetResponse datasetResponse = diallerRestClient.getDatasets();

        return datasetResponse.getList();
    }

    @Override
    public void updateDataset(String dataset, String status) {
        diallerRestClient.updateDataset(dataset, status);
    }
}
