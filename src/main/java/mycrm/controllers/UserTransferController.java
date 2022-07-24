package mycrm.controllers;

import mycrm.models.User;
import mycrm.models.UserNoteTransferUpdate;
import mycrm.models.UserTaggedNotesPackage;
import mycrm.services.UserService;
import mycrm.services.UserTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserTransferController {
    private final UserService userService;
    private final UserTransferService userTransferService;

    @Autowired
    public UserTransferController(UserService userService,
                                  UserTransferService userTransferService) {
        this.userService = userService;
        this.userTransferService = userTransferService;
    }

    @RequestMapping("/admin/users/transfer-portal/{id}")
    public String userTransferPortal(@PathVariable("id") Long id, Model model) {

        model.addAttribute("selectedUser", userService.findById(id));
        return "admin/users/transfer-portal/transfer-portal";
    }

    @RequestMapping("/admin/users/transfer-portal/user-notes/{id}")
    public String userNotesTransfer(@PathVariable("id") Long id, Model model) {
        User selectedUser = userService.findById(id);

        UserTaggedNotesPackage notes = userTransferService.findUserTaggedNotes(selectedUser);
        List<User> users = userService.findAll();
        users.remove(selectedUser);

        model.addAttribute("selectedUser", selectedUser);
        model.addAttribute("notes", notes);
        model.addAttribute("users", users);
        return "admin/users/transfer-portal/user-notes";
    }

    @RequestMapping(value = "/admin/users/transfer-portal/transfer-user-notes", method = RequestMethod.POST)
    public String transferUserNotes(UserNoteTransferUpdate userNoteTransferUpdate) {
        boolean successful = userTransferService.transferUserNotes(userNoteTransferUpdate);

        return "redirect:/admin/users/transfer-portal/confirmation?success=" + successful;
    }

    // show transfer confirmation
    @RequestMapping("/admin/users/transfer-portal/confirmation")
    public String brokerTransferConfirmation(@RequestParam boolean success, Model model) {
        if (success) {
            model.addAttribute("message", "Updated");
        } else {
            model.addAttribute("message", "Failed");
        }
        return "admin/users/transfer-portal/confirmation";
    }
}
