package mycrm.configuration;

public enum EmailProcess {
    AUTOMATED("AUTOMATED"),
    MANUAL("MANUAL");

    private String name;

    EmailProcess(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
