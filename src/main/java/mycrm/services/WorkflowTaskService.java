package mycrm.services;

import mycrm.models.ObjectedTask;
import mycrm.models.VerbalTask;

import java.util.List;

public interface WorkflowTaskService {
    List<ObjectedTask> findAllObjectedContracts();

    List<VerbalTask> findAllVerbalContracts();
}
