package mycrm.controllers;

import mycrm.services.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DiaryController {
    private final MyService myService;

    @Autowired
    public DiaryController(MyService myService) {
        this.myService = myService;
    }


    @RequestMapping("/admin/customer/diary/index")
    public String getDiaryHomePage(
            @RequestParam(value = "callbackSearchDate", required = false) String callbackSearchDate,
            Model model) {
        System.out.println("+++++++++++++++++++ callbackSearchDate");
        System.out.println(callbackSearchDate);
        if (StringUtils.hasText(callbackSearchDate)) {
            System.out.println("------------------- has text is true");
            model.addAttribute("callbacksMap", myService.getCallbacksForDate(callbackSearchDate));
            System.out.println(myService.getCallbacksForDate(callbackSearchDate));
            model.addAttribute("callbackSearchDate", getDateFromString(callbackSearchDate));
        }
        return "admin/customer/diary/index";
    }

    private Date getDateFromString(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception ignored) {

        }
        return new Date();
    }
}
