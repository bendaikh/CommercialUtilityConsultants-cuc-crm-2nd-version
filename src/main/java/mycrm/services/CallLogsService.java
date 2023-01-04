package mycrm.services;

import mycrm.models.CallLogs;
import mycrm.models.Customer;

import java.util.List;

public interface CallLogsService {
    CallLogs save(CallLogs callLogs);
    List<CallLogs> findCallLogsByCustomer(Customer customer);
}
