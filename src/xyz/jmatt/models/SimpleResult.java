package xyz.jmatt.models;

public class SimpleResult {
    private String message;
    private boolean isError;

    public SimpleResult(String message, boolean isError) {
        this.message = message;
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isError() {
        return isError;
    }
}
