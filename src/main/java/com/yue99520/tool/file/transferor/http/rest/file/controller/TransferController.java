package com.yue99520.tool.file.transferor.http.rest.file.controller;

import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.http.rest.file.request.ReceiveFileResponse;
import com.yue99520.tool.file.transferor.http.rest.file.request.SendFileResponse;
import com.yue99520.tool.file.transferor.service.connection.ConnectionService;
import com.yue99520.tool.file.transferor.service.connection.Partner;
import com.yue99520.tool.file.transferor.service.security.ValidateLocalhost;
import com.yue99520.tool.file.transferor.service.security.ValidatePartner;
import com.yue99520.tool.file.transferor.service.transfer.FileTransferService;
import com.yue99520.tool.file.transferor.util.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping(value = "/file/transfer")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final FileTransferService fileTransferService;
    private final ConnectionService connectionService;

    @Autowired
    public TransferController(FileTransferService fileTransferService, ConnectionService connectionService) {
        this.fileTransferService = fileTransferService;
        this.connectionService = connectionService;
    }

    @ValidatePartner
    @PostMapping(value = "/receive")
    @ResponseBody
    public ResponseEntity<ReceiveFileResponse> receive(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename) {
        long filesize = file.getSize();
        filename = Base64Utils.decode(filename);
        logger.info("Receiving file, name={}, size={}", filename, filesize);
        try {
            InputStream fileInputStream = file.getInputStream();
            fileTransferService.receive(fileInputStream, filename);
            return ResponseEntity.ok(ReceiveFileResponse.newBuilder()
                    .withFilename(filename)
                    .withSize(filesize)
                    .withSuccess(true)
                    .withMessage("success")
                    .build());
        } catch (IOException e) {
            logger.error("fail to get input stream.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ReceiveFileResponse.newBuilder()
                            .withFilename(filename)
                            .withSize(filesize)
                            .withSuccess(false)
                            .withMessage("Can not read file input stream from request.")
                            .build());
        } catch (FileReceiveException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ReceiveFileResponse.newBuilder()
                            .withFilename(filename)
                            .withSize(filesize)
                            .withSuccess(false)
                            .withMessage(e.getMessage())
                            .build());
        }
    }

    @ValidateLocalhost
    @PostMapping(value = "/send")
    public ResponseEntity<SendFileResponse> send(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        long filesize = file.getSize();
        logger.info("Sending file, name={}, size={}", filename, filesize);
        try {
            InputStream fileInputStream = file.getInputStream();
            String originalName = file.getOriginalFilename();
            Partner partner = connectionService.getPartner();
            fileTransferService.send(fileInputStream, originalName, partner);
            logger.info("successfully send file.");
            return ResponseEntity.ok(SendFileResponse.newBuilder()
                    .withFilename(filename)
                    .withSize(filesize)
                    .withSuccess(true)
                    .withMessage("success")
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SendFileResponse.newBuilder()
                            .withFilename(filename)
                            .withSize(filesize)
                            .withSuccess(false)
                            .withMessage("Can not read file input stream from request.")
                            .build());
        } catch (FileSendException e) {
            logger.info("fail to send file.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(SendFileResponse.newBuilder()
                            .withFilename(filename)
                            .withSize(filesize)
                            .withSuccess(false)
                            .withMessage(e.getMessage())
                            .build());
        }
    }
}
