package mycrm.controllers;

import mycrm.models.User;
import mycrm.services.BrokerService;
import mycrm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final BrokerService brokerService;

    @Autowired
    public UserController(UserService userService, BrokerService brokerService) {
        this.userService = userService;
        this.brokerService = brokerService;
    }

    @RequestMapping("/admin/users/users")
    public String users(Model model) {
        List<User> findAll = userService.findAll();
        model.addAttribute("findAll", findAll);

        return "admin/users/users";
    }

    @RequestMapping("/admin/users/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("brokers", brokerService.findAll());
        return "admin/users/edituser";
    }

    @RequestMapping("/admin/users/newuser")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("brokers", brokerService.findAll());
        return "admin/users/newuser";
    }

    @RequestMapping("/admin/users/changepassword/{id}")
    public String changePassword(@PathVariable("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));

        return "admin/users/changepassword";
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(User user, @RequestParam("passwordHash") String passwordHash, @RequestParam("confirmPassword") String confirmPassword) {

        if (passwordHash.equals(confirmPassword)) {
            userService.updatePassword(user, passwordHash);
        }

        return "redirect:/admin/users/users";
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public String createUser(@Valid User user, BindingResult bindingResult) {

        userService.create(user);

        return "redirect:/admin/users/users";
    }

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public String saveUser(@Valid User user, BindingResult bindingResult, @RequestParam("roles") int role) {
        // check to see if the user exists
        User userExists = userService.findById(user.getId());
        if (userExists != null) {

            userService.update(user);
            userService.updateRole(user, role);
            return "redirect:/admin/users/users";
        }

        return "redirect:/admin/users/users";
    }

}
