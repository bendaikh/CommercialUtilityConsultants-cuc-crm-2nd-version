package mycrm.services;

import mycrm.models.ContractReason;
import mycrm.repositories.ContractReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ContractReasonServiceImpl implements ContractReasonService {

    private final ContractReasonRepository contractReasonRepository;

    @Autowired
    public ContractReasonServiceImpl(ContractReasonRepository contractReasonRepository) {
        this.contractReasonRepository = contractReasonRepository;
    }


    @Override
    public ContractReason findById(Long id) {
        return this.contractReasonRepository.findOne(id);
    }

    @Override
    public List<ContractReason> findAll() {
        return this.contractReasonRepository.findAll();
    }

    @Override
    public ContractReason save(ContractReason contractReason) {
        return this.contractReasonRepository.save(contractReason);
    }
}
