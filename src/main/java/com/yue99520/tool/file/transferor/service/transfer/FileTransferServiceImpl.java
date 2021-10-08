package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.service.connection.Partner;
import com.yue99520.tool.file.transferor.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@Service
public class FileTransferServiceImpl implements FileTransferService{

    private static final Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);

    private final FileUtil fileUtil;

    @Value("${my.key}")
    private String accessKey;

    @Autowired
    public FileTransferServiceImpl(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void send(InputStream fileStream, String originalName, Partner partner) throws FileSendException {
        try {
            File file = fileUtil.saveAsTemp(fileStream, originalName);
            TransferClient partnerClient = new TransferClient(partner.getHost(), partner.getPort(), accessKey);
            partnerClient.send(file, originalName);

            if (!file.delete()) {
                logger.warn("Temp file {} was not deleted expectedly.", file.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new FileSendException("Error at creating temp file before sending transfer file.", e);
        }
    }

    @Override
    public File receive(InputStream fileInputStream, String filename) throws FileReceiveException {
        try {
            return fileUtil.save(filename, fileInputStream);
        } catch (IOException e) {
            logger.error("fail to receive file.", e);
            throw new FileReceiveException("Error at saving file.", e);
        }
    }
}
