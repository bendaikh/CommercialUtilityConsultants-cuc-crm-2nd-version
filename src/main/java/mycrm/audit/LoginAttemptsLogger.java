package mycrm.audit;

import mycrm.models.User;
import mycrm.models.UserStat;
import mycrm.services.UserService;
import mycrm.services.UserStatsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class LoginAttemptsLogger {

    private static Logger logger = LogManager.getLogger();

    private final UserService userService;
    private final UserStatsService userStatsService;

    @Autowired
    public LoginAttemptsLogger(UserService userService, UserStatsService userStatsService) {
        this.userService = userService;
        this.userStatsService = userStatsService;
    }

    @EventListener
    public void auditEventHappened(AuditApplicationEvent auditApplicationEvent) {

        AuditEvent auditEvent = auditApplicationEvent.getAuditEvent();
//        logger.info("Audit Event,principal={},type={},data={}", auditEvent.getPrincipal(), auditEvent.getType(),
//                auditEvent.getData().get("details"));

        WebAuthenticationDetails details = (WebAuthenticationDetails) auditEvent.getData().get("details");
//        logger.info("Remote IP address: " + details.getRemoteAddress());
//        logger.info("  Session Id: " + details.getSessionId());
//        logger.info("  Request URL: " + auditEvent.getData().get("requestUrl"));

        if (!auditEvent.getPrincipal().toString().equals("anonymousUser")) {
            User user = userService.findUserByUsername(auditEvent.getPrincipal());
            userStatsService.create(new UserStat(auditEvent.getPrincipal(), user, details.getRemoteAddress(),
                    (String) auditEvent.getData().get("requestUrl"), details.getSessionId(),
                    auditEvent.getType().toString()));
        } else {
            userStatsService.create(new UserStat(auditEvent.getPrincipal(), null, details.getRemoteAddress(),
                    (String) auditEvent.getData().get("requestUrl"), details.getSessionId(),
                    auditEvent.getType().toString()));
        }
    }
}
