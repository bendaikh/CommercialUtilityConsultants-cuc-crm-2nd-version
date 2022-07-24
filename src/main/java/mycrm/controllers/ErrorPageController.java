package mycrm.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 
@Controller
public class ErrorPageController {   
	
    @RequestMapping("/access-denied")
    public String index() {
        return "access-denied";
    }
    
    @RequestMapping("/unauthorised")
    public String unauthorised() {
        return "admin/error/401";
    }
    
    @RequestMapping("/page-not-found")
    public String pageNotfound() {
        return "admin/error/404";
    }
}
