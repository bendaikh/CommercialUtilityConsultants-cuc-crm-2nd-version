package mycrm.services;

import mycrm.models.DoNotRenewReason;

import java.util.List;

public interface DoNotRenewReasonService {

    DoNotRenewReason findById(Long id);

    List<DoNotRenewReason> findAll();

    DoNotRenewReason save(DoNotRenewReason doNotRenewReason);

}
