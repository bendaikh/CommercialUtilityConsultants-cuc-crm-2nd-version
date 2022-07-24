package mycrm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
   
@Controller
public class SystemController {   
	
    @RequestMapping("/admin/system/index")
    public String systemIndex() {
        return "admin/system/index";
    }
    

}
