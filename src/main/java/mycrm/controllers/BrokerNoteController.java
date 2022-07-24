package mycrm.controllers;

import mycrm.models.Broker;
import mycrm.models.BrokerNote;
import mycrm.models.User;
import mycrm.services.BrokerNoteService;
import mycrm.services.BrokerService;
import mycrm.services.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BrokerNoteController {

    private static Logger logger = LogManager.getLogger();

    private final BrokerNoteService brokerNoteService;
    private final BrokerService brokerService;
    private final UserService userService;

    @Autowired
    public BrokerNoteController(BrokerNoteService brokerNoteService, BrokerService brokerService,
            UserService userService) {
        this.brokerNoteService = brokerNoteService;
        this.brokerService = brokerService;
        this.userService = userService;
    }

    // access broker notes
    @RequestMapping(value = "/admin/broker/brokernotes/{id}")
    public String getNotes(@PathVariable("id") Long id, Model model) {
        Broker broker = brokerService.findById(id);
        List<BrokerNote> brokerNotes = brokerNoteService.findByBroker(broker);
        List<User> users = userService.findAll();
        
        model.addAttribute("brokerNotes", brokerNotes);
        model.addAttribute("users", users);
        model.addAttribute("broker", broker);
        return "admin/broker/brokernotes";
    }

    // create broker notes
    @RequestMapping(value = "/brokerNote", method = RequestMethod.POST)
    public String saveNote(BrokerNote brokerNote) {
        BrokerNote createdBrokerNote = brokerNoteService.create(brokerNote);
        if (createdBrokerNote.getId() != null) {
            logger.info("Successfully added broker note, brokerNote={}", createdBrokerNote.getId());
            return "redirect:/admin/broker/brokernotes/" + brokerNote.getBroker().getId();
        } else {
            logger.info("Failed to add broker note");
            return "redirect:/admin/broker/brokers";
        }

    }

    // delete broker note
    @RequestMapping("/admin/broker/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") Long id) {
        BrokerNote brokerNote = brokerNoteService.findById(id);

        if (brokerNote != null) {
            Long brokerId = brokerNote.getBroker().getId();
            brokerNoteService.deleteById(id);
            return "redirect:/admin/broker/brokernotes/" + brokerId;
        }

        return "redirect:/admin/index";

    }

}
