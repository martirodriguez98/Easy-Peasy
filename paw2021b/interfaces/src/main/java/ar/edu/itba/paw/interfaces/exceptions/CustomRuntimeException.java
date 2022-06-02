package ar.edu.itba.paw.interfaces.exceptions;

public class CustomRuntimeException extends RuntimeException {
    private final int responseStatus;
    private final String msgCode;

    public CustomRuntimeException(String msgCode, int responseStatus) {
        super();
        this.msgCode = msgCode;
        this.responseStatus = responseStatus;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getMsgCode() {
        return msgCode;
    }
}
