package com.yue99520.tool.file.transferor.exception;

public class AgentNotExistException extends Exception{

    public AgentNotExistException() {
        super();
    }

    public AgentNotExistException(String message) {
        super(message);
    }

    public AgentNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentNotExistException(Throwable cause) {
        super(cause);
    }

    protected AgentNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
