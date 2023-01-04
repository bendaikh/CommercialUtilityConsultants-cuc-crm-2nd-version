package mycrm.controllers;

import mycrm.models.CallLogs;
import mycrm.services.CallLogsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CallLogsController {

    private final CallLogsService callLogsService;

    public CallLogsController(CallLogsService callLogsService) {
        this.callLogsService = callLogsService;
    }

    @RequestMapping(value = "/callLogCreate",method = RequestMethod.POST)
    public String saveCallLog(CallLogs callLogObject){
        System.out.println(callLogObject.getNote());
        CallLogs callLogs = callLogsService.save(callLogObject);
        return "redirect:/admin/customer/info/"+callLogObject.getCustomer().getId();
    }
}
