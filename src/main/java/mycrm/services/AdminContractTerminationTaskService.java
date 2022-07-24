package mycrm.services;

import mycrm.models.PendingTerminationTask;

import java.util.List;

public interface AdminContractTerminationTaskService {

    List<PendingTerminationTask> findAllContractsToTerminate();
}
