package com.fanok.mdpu24v1;

public class PhotoUploadRequest {
    private boolean error;
    private String url;

    public PhotoUploadRequest(boolean error, String url) {
        this.error = error;
        this.url = url;
    }

    public boolean isError() {
        return error;
    }

    public String getUrl() {
        return url;
    }
}
