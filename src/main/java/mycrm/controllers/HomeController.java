package mycrm.controllers;

import mycrm.services.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	private final MyService myService;

	@Autowired
	public HomeController(MyService myService) {
		this.myService = myService;
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/admin/index")
	public String adminIndex(Model model) {
		model.addAttribute("myCallbacks", myService.getMyTodaysCallbacks());
		return "admin/index";
	}

	@RequestMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("myCallbacks", myService.getMyTodaysCallbacks());
		return "admin/index";
	}

}
