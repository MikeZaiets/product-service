package ua.com.zmike.product.service.exception;

public class TargetNotFoundException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "Target [%s] can't be found by params [%s]:[%s]";

    public TargetNotFoundException(String targetName, String searchKey, Object searchKeyValue) {
        super(String.format(MESSAGE_PATTERN, targetName, searchKey, searchKeyValue));
    }
}