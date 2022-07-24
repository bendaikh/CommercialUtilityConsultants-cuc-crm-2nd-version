package mycrm.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service()
public class NotificationServiceImpl implements NotificationService {

	public static final String NOTIFY_MSG_SESSION_KEY = "siteNotificationMessages";

	private final HttpSession httpSession;

	@Autowired
	public NotificationServiceImpl(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	@Override
	public void addInfoMessage(String msg) {
		addNotificationMessage(NotificationMessageType.INFO, msg);
	}

	@Override
	public void addErrorMessage(String msg) {
		addNotificationMessage(NotificationMessageType.ERROR, msg);
	}

	@SuppressWarnings("unchecked")
	private void addNotificationMessage(NotificationMessageType type, String msg) {
		List<NotificationMessage> notifyMessages = (List<NotificationMessage>) httpSession
				.getAttribute(NOTIFY_MSG_SESSION_KEY);
		if (notifyMessages == null) {
			notifyMessages = new ArrayList<NotificationMessage>();
		}
	}
}
