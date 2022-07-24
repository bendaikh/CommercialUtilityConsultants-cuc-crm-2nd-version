package mycrm.configuration;

public enum EmailType {
    RENEWAL("RENEWAL"),
    MISSING_OUT("MISSING_OUT"),
    SOLD("SOLD"),
    OBJECTED("OBJECTED"),
    LIVE("LIVE");

    private String type;

    EmailType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
