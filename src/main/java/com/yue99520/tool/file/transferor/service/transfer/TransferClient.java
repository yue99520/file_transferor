package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.exception.PartnerConnectionError;
import com.yue99520.tool.file.transferor.service.security.PartnerSecurityService;
import com.yue99520.tool.file.transferor.util.Base64Utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class TransferClient {
    private static final Logger logger = LoggerFactory.getLogger(TransferClient.class);
    public static final String FILE_RECEIVE_URI = "/file/transfer/receive";
    private final String fileReceiveApiUrl;
    private final HttpClient httpClient;
    private final String key;

    public TransferClient(String host, int port, String key) {
        this.key = key;
        httpClient = HttpClientBuilder.create().build();
        fileReceiveApiUrl = String.format("%s:%d%s", host, port, FILE_RECEIVE_URI);
    }

    public void send(File file, String originalName) throws FileSendException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        HttpEntity entity = builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY))
                .addPart("filename", new StringBody(Base64Utils.encode(originalName), ContentType.DEFAULT_BINARY))
                .build();
        HttpPost post = new HttpPost(fileReceiveApiUrl);
        post.addHeader(PartnerSecurityService.PARTNER_ACCESS_KEY_HEADER, key);
        post.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(post);
            validateStatus200(response);
        } catch (IOException e) {
            logger.error("IOException at sending file request.", e);
            throw new FileSendException(e);
        }
    }

    private void validateStatus200(HttpResponse response) throws FileSendException {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine == null) {
            throw new PartnerConnectionError(String.format("Partner response empty status line. response=%s", response));
        }
        int statusCode = statusLine.getStatusCode();
        if (statusCode != 200) {
            throw new FileSendException(String.format("Partner response http %d. response=%s", statusCode, response));
        }
    }
}
