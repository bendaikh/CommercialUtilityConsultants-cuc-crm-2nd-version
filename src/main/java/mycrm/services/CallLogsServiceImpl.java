package mycrm.services;

import mycrm.models.CallLogs;
import mycrm.models.Customer;
import mycrm.repositories.CallLogsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CallLogsServiceImpl implements CallLogsService{
    private final CallLogsRepository callLogsRepository;

    public CallLogsServiceImpl(CallLogsRepository callLogsRepository) {
        this.callLogsRepository = callLogsRepository;
    }

    @Override
    public CallLogs save(CallLogs callLogs) {
        return callLogsRepository.save(callLogs);
    }

    @Override
    public List<CallLogs> findCallLogsByCustomer(Customer customer) {
        return this.callLogsRepository.findCallLogsByCustomer(customer);
    }
}
