package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@Service
public class FileTransferServiceImpl implements FileTransferService{

    private static final Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);

    private final FileUtil fileUtil;

    @Autowired
    public FileTransferServiceImpl(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void send(InputStream fileStream, String originalName, Agent agent) throws FileSendException {
        try {
            File file = fileUtil.saveAsTemp(fileStream, originalName);
            TransferClient partnerClient = new TransferClient(
                    agent.getProtocol(),
                    agent.getHost(),
                    agent.getPort());
            partnerClient.send(file, originalName);

            if (!file.delete()) {
                logger.warn("Temp file {} was not deleted expectedly.", file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new FileSendException("Error at creating temp file before sending transfer file.", e);
        }
    }

    @Override
    public File receive(InputStream fileInputStream, String filename, Agent agent) throws FileReceiveException {
        try {
            File result = fileUtil.save(filename, fileInputStream);
            logger.info("received file from agent: {}, ip: {}:{}.", agent.getName(), agent.getHost(), agent.getPort());
            return result;
        } catch (IOException e) {
            logger.error("fail to receive file.", e);
            throw new FileReceiveException("Error at saving file.", e);
        }
    }
}
