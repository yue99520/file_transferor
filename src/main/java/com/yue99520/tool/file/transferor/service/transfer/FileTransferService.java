package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;

import java.io.File;
import java.io.InputStream;

public interface FileTransferService {

    void send(InputStream fileStream, String originalName, Agent agent) throws FileSendException;

    File receive(InputStream fileStream, String name, Agent agent) throws FileReceiveException;
}
