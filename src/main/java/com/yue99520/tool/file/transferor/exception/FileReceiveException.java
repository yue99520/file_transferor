package com.yue99520.tool.file.transferor.exception;

public class FileReceiveException extends Exception{

    public FileReceiveException() {
    }

    public FileReceiveException(String message) {
        super(message);
    }

    public FileReceiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReceiveException(Throwable cause) {
        super(cause);
    }

    public FileReceiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
