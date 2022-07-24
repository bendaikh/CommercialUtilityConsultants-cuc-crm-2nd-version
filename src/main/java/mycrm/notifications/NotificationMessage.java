package mycrm.notifications;

public class NotificationMessage {

    NotificationMessageType type;
    String text;

    public NotificationMessage(NotificationMessageType type, String text) {
        this.type = type;
        this.text = text;
    }

    public NotificationMessageType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

}
