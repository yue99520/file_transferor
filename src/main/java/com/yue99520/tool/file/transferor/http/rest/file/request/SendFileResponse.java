package com.yue99520.tool.file.transferor.http.rest.file.request;

public class SendFileResponse {

    private String filename;

    private long size;

    private boolean success;

    private String message;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String filename;
        private long size;
        private boolean success;
        private String message;

        private Builder() {
        }

        public static Builder aSendFileResponse() {
            return new Builder();
        }

        public Builder withFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder withSize(long size) {
            this.size = size;
            return this;
        }

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public SendFileResponse build() {
            SendFileResponse sendFileResponse = new SendFileResponse();
            sendFileResponse.setFilename(filename);
            sendFileResponse.setSize(size);
            sendFileResponse.setSuccess(success);
            sendFileResponse.setMessage(message);
            return sendFileResponse;
        }
    }
}
