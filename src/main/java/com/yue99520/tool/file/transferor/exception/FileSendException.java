package com.yue99520.tool.file.transferor.exception;

public class FileSendException extends Exception{

    public FileSendException() {
    }

    public FileSendException(String message) {
        super(message);
    }

    public FileSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSendException(Throwable cause) {
        super(cause);
    }

    public FileSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
