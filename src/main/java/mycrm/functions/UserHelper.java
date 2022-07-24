package mycrm.functions;

import mycrm.models.Broker;
import mycrm.models.User;
import mycrm.services.BrokerService;
import mycrm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    @Value("${default.broker.id}")
    private Long defaultBrokerId;

    private final UserService userService;
    private final BrokerService brokerService;

    @Autowired
    public UserHelper(UserService userService, BrokerService brokerService) {
        this.userService = userService;
        this.brokerService = brokerService;
    }

    public User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return this.userService.findUserByUsername(auth.getName());
    }

    public Broker getDefaultBroker() {
        return this.brokerService.findById(defaultBrokerId);
    }

}
