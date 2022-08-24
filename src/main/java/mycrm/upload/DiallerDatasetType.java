package mycrm.upload;

public enum DiallerDatasetType {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");

    private final String method;

    DiallerDatasetType(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
