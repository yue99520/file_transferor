package com.yue99520.tool.file.transferor.service.transfer;

import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.service.connection.Partner;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Service
public class FileTransferServiceImpl implements FileTransferService{

    private static final Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);
    private static final String TEMP_FILE_PREFIX = "uploadStream-%d";
    private static final String TEMP_FILE_SUFFIX = ".tmp";

    @Value("${file.storage.path}")
    private String fileStoragePathString;

    @Value("${my.key}")
    private String accessKey;

    private Path fileStoragePath;

    @PostConstruct
    public void postConstruct() {
        logger.info("Using configuration [file.storage.path={}]", fileStoragePathString);
        fileStoragePath = initializeStoragePath();
    }

    private Path initializeStoragePath() {
        Path path = Paths.get(fileStoragePathString);
        File storageDirectory = path.toFile();
        if (!storageDirectory.exists()) {
            logger.warn("File storage directory \"{}\" not exist.", path);
            if (storageDirectory.mkdirs()) {
                logger.debug("Create storage directory: \"{}\"", path);
            } else {
                logger.warn("Can not create storage directory \"{}\"", path);
            }
        } else if (!Files.isDirectory(path)) {
            logger.error("File storage path is not a directory. fileStoragePath={}", path);
            throw new RuntimeException(String.format("File storage path is not a directory. fileStoragePath=%s", fileStoragePathString));
        }
        return path;
    }

    @Override
    public void send(InputStream fileStream, String originalName, Partner partner) throws FileSendException {
        try {
            File file = toTempFile(fileStream);
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
    public void receive(InputStream fileInputStream, String filename) throws FileReceiveException {
        Path expectFilePath = findAvailablePath(filename);
        try {
            saveFile(fileInputStream, expectFilePath);
        } catch (IOException e) {
            logger.error("fail to receive file.", e);
            throw new FileReceiveException("Error at saving file.", e);
        }
    }

    private Path findAvailablePath(String filename) {
        return findAvailablePath(filename, 0);
    }

    private Path findAvailablePath(String filename, int counter) {
        if (counter != 0) {
            filename = "(" + counter + ")" + filename.replace("(" + (counter - 1) + ")", "");
        }
        Path expectFilePath = fileStoragePath.resolve(filename);
        if (expectFilePath.toFile().exists()) {
            return findAvailablePath(filename, ++counter);
        }
        return expectFilePath;
    }

    private void saveFile(InputStream fileInputStream, Path expectFilePath) throws IOException {
        byte[] buf = new byte[256];
        try (OutputStream fileOutputStream = Files.newOutputStream(
                expectFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            int size;
            while ((size = fileInputStream.read(buf)) != -1) {
                fileOutputStream.write(buf, 0, size);
            }
        }
        fileInputStream.close();
    }

    private File toTempFile(InputStream fileInputStream) throws IOException {
        String formatPrefix = String.format(TEMP_FILE_PREFIX, System.currentTimeMillis());
        final File tempFile = File.createTempFile(formatPrefix, TEMP_FILE_SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)){
            IOUtils.copy(fileInputStream, fileOutputStream);
        }
        return tempFile;
    }

    public void setFileStoragePathString(String fileStoragePathString) {
        this.fileStoragePathString = fileStoragePathString;
    }
}
