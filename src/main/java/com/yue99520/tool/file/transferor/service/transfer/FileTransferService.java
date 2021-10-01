package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.service.connection.Partner;

import java.io.InputStream;

public interface FileTransferService {

    void send(InputStream fileStream, String originalName, Partner partner) throws FileSendException;

    void receive(InputStream fileStream, String name) throws FileReceiveException;
}
