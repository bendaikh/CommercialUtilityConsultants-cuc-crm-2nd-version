package mycrm.services;

import mycrm.models.DoNotRenewReason;
import mycrm.repositories.ContractReasonRepository;
import mycrm.repositories.DoNotRenewReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoNotRenewReasonServiceImpl implements DoNotRenewReasonService{
    private final DoNotRenewReasonRepository doNotRenewReasonRepository;

    @Autowired
    public DoNotRenewReasonServiceImpl(DoNotRenewReasonRepository doNotRenewReasonRepository) {
        this.doNotRenewReasonRepository = doNotRenewReasonRepository;
    }

    @Override
    public DoNotRenewReason findById(Long id) {
        return doNotRenewReasonRepository.findOne(id);
    }

    @Override
    public List<DoNotRenewReason> findAll() {
        return doNotRenewReasonRepository.findAll();
    }

    @Override
    public DoNotRenewReason save(DoNotRenewReason doNotRenewReason) {
        return doNotRenewReasonRepository.save(doNotRenewReason);
    }
}
