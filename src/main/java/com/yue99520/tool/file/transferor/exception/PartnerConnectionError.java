package com.yue99520.tool.file.transferor.exception;

public class PartnerConnectionError extends RuntimeException{

    public PartnerConnectionError() {
    }

    public PartnerConnectionError(String message) {
        super(message);
    }

    public PartnerConnectionError(String message, Throwable cause) {
        super(message, cause);
    }

    public PartnerConnectionError(Throwable cause) {
        super(cause);
    }

    public PartnerConnectionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
