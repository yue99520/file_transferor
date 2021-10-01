package com.yue99520.tool.file.transferor.http.rest.file.request;

public class ReceiveFileResponse {

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

        public static Builder aRemoteFileUploadResponse() {
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

        public ReceiveFileResponse build() {
            ReceiveFileResponse receiveFileResponse = new ReceiveFileResponse();
            receiveFileResponse.setFilename(filename);
            receiveFileResponse.setSize(size);
            receiveFileResponse.setSuccess(success);
            receiveFileResponse.setMessage(message);
            return receiveFileResponse;
        }
    }
}
