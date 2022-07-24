package mycrm.services;

import mycrm.models.ContractReason;

import java.util.List;

public interface ContractReasonService {
    ContractReason findById(Long id);

    List<ContractReason> findAll();

    ContractReason save(ContractReason contractReason);
}
