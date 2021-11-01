package com.yue99520.tool.file.transferor.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Component
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String TEMP_FILE_SUFFIX = ".tmp";

    @Value("${file.storage.path.temp}")
    private String tempFileStoragePathString;

    private Path tempFileStoragePath;

    @Value("${file.storage.path}")
    private String fileStoragePathString;

    private Path fileStoragePath;

    @PostConstruct
    public void postConstruct() {
        logger.info("Using configuration [file.storage.path.temp={}]", tempFileStoragePathString);
        tempFileStoragePath = resolveDirectory(tempFileStoragePathString);
        fileStoragePath = resolveDirectory(fileStoragePathString);
    }

    public File saveAsTemp(InputStream inputStream, String classifier) throws IOException {
        String filenamePrefix = String.format("%s-%d", classifier, System.currentTimeMillis());
        final File tempFile = File.createTempFile(filenamePrefix, TEMP_FILE_SUFFIX, tempFileStoragePath.toFile());
        tempFile.deleteOnExit();
        try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)){
            IOUtils.copy(inputStream, fileOutputStream);
        }
        return tempFile;
    }

    public File save(String filename, InputStream inputStream) throws IOException {
        Path expectFilePath = findAvailablePath(filename);
        OutputStream fileOutputStream = Files.newOutputStream(expectFilePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING);
        appendStream(fileOutputStream, inputStream);
        fileOutputStream.close();
        inputStream.close();
        return expectFilePath.toFile();
    }

    public void saveAppend(File file, InputStream inputStream) throws IOException{
        FileOutputStream outputStream = new FileOutputStream(file, true);
        appendStream(outputStream, inputStream);
        outputStream.close();
        inputStream.close();
    }

    private void appendStream(OutputStream toOutputStream, InputStream inputStream) throws IOException {
        byte[] buf = new byte[512];
        int size;
        while ((size = inputStream.read(buf)) != -1) {
            toOutputStream.write(buf, 0, size);
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

    public Path resolveDirectory(String directoryPath) {
        Path path = Paths.get(directoryPath);
        File storageDirectory = path.toFile();
        if (!storageDirectory.exists()) {
            logger.warn("Directory \"{}\" not exist.", path);
            if (storageDirectory.mkdirs()) {
                logger.debug("Create directory: \"{}\"", path);
            } else {
                logger.warn("Can not create directory \"{}\"", path);
            }
        } else if (!Files.isDirectory(path)) {
            logger.error("Path is not a directory. tempFileStoragePath={}", path);
            throw new RuntimeException(String.format("File storage path is not a directory. fileStoragePath=%s", path));
        }
        return path;
    }

    public void setFileStoragePathStringTemp(String tempFileStoragePathString) {
        this.tempFileStoragePathString = tempFileStoragePathString;
    }
}
