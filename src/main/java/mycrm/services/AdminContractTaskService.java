package mycrm.services;

import mycrm.models.NewSaleTask;
import mycrm.models.ProcessingTask;

import java.util.List;

public interface AdminContractTaskService {

    List<NewSaleTask> findAllAdminContractNewSalesTasks();

    List<ProcessingTask> findAllContractsToProcess();
}
