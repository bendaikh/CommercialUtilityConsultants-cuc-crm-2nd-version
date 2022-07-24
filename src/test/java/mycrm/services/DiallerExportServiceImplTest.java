package mycrm.services;

import mycrm.repositories.DiallerExportRepository;
import mycrm.rest.clients.DiallerRestClient;
import mycrm.upload.DiallerDatasetResponse;
import mycrm.upload.DiallerDatasetType;
import mycrm.upload.DiallerExportEntity;
import mycrm.upload.DiallerExportJSONWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiallerExportServiceImplTest {

    @InjectMocks
    private DiallerExportServiceImpl diallerExportService;

    @Mock
    private DiallerDataManipulationService mockDiallerDataManipulationService;

    @Mock
    private DiallerExportRepository mockDiallerExportRepository;

    @Mock
    private DiallerRestClient mockDiallerRestClient;

    @Mock
    private DiallerExportJSONWriter mockWriter;

    @Test
    public void shouldExportAllDiallerDataWhenNoEntityFound() {
        DiallerExportEntity entity = new DiallerExportEntity();
        Set<DiallerExportEntity> entitySet = new HashSet<>();
        entitySet.add(entity);
        when(mockDiallerDataManipulationService.getAllDiallerData()).thenReturn(entitySet);
        when(mockDiallerExportRepository.filterExistingDiallerRecords(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
                any(String.class))).thenReturn(entity);


        diallerExportService.exportAllDiallerData();

        verify(mockDiallerRestClient, times(0)).sendDataToDiallerServer(any(String.class), any(DiallerDatasetType.class));
    }

    @Test
    public void shouldNotExportAllDiallerDataWhenDuplicateEntityFound() {
        DiallerExportEntity entity = new DiallerExportEntity();
        Set<DiallerExportEntity> entitySet = new HashSet<>();
        entitySet.add(entity);
        when(mockDiallerDataManipulationService.getAllDiallerData()).thenReturn(entitySet);

        when(mockDiallerExportRepository.filterExistingDiallerRecords(any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
                any(String.class))).thenReturn(null);


        when(mockDiallerExportRepository.save(any(DiallerExportEntity.class))).thenReturn(entity);

        when(mockWriter.buildJSON(any(Set.class), any(DiallerDatasetType.class))).thenReturn("path");

        diallerExportService.exportAllDiallerData();

        //verify(mockDiallerRestClient, times(1)).sendDataToDiallerServer(any(String.class), any(DiallerDatasetType.class));
    }

    @Test
    public void shouldExportLatestDiallerData() {
        diallerExportService.exportLatestDiallerData();
        verify(mockDiallerDataManipulationService, times(1)).getLatestDiallerData();
    }

    @Test
    public void shouldSaveAllDiallerData() {
        Set data = new HashSet();
        diallerExportService.saveAll(data);
    }

    @Test
    public void shouldGetDatasets() {
        DiallerDatasetResponse dataset = new DiallerDatasetResponse();
        when(mockDiallerRestClient.getDatasets()).thenReturn(dataset);
        diallerExportService.getDatasets();
    }

    @Test
    public void shouldUpdateDataset() {

        String dataset = "14";
        String status = "LIVE";
        diallerExportService.updateDataset(dataset, status);

        verify(mockDiallerRestClient, times(1)).updateDataset(any(String.class), any(String.class));
    }
}
