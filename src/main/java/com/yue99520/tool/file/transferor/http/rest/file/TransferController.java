package com.yue99520.tool.file.transferor.http.rest.file;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.exception.FileReceiveException;
import com.yue99520.tool.file.transferor.exception.FileSendException;
import com.yue99520.tool.file.transferor.http.ValidatePartnerInterceptor;
import com.yue99520.tool.file.transferor.http.rest.file.request.ReceiveFileResponse;
import com.yue99520.tool.file.transferor.http.rest.file.request.SendFileResponse;
import com.yue99520.tool.file.transferor.service.agent.AgentService;
import com.yue99520.tool.file.transferor.service.security.AllowLocal;
import com.yue99520.tool.file.transferor.service.security.AllowPartners;
import com.yue99520.tool.file.transferor.service.transfer.FileTransferService;
import com.yue99520.tool.file.transferor.util.Base64Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;


@RestController
@RequestMapping(value = "/file/transfer")
public class TransferController {

    private static final Logger logger = LoggerFactory.getLogger(TransferController.class);

    private final FileTransferService fileTransferService;
    private final AgentService agentService;

    @Autowired
    public TransferController(FileTransferService fileTransferService, AgentService agentService) {
        this.fileTransferService = fileTransferService;
        this.agentService = agentService;
    }

    @AllowPartners
    @PostMapping(value = "/receive")
    @ResponseBody
    public ResponseEntity<ReceiveFileResponse> receive(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("filename") String filename,
            @RequestAttribute(ValidatePartnerInterceptor.REQUEST_ATTR_PARTNER) Agent remoteAgent) {
        long filesize = multipartFile.getSize();
        filename = Base64Utils.decode(filename);
        logger.info("Receiving file, name={}, size={}", filename, filesize);
        try {
            InputStream fileInputStream = multipartFile.getInputStream();
            File file = fileTransferService.receive(fileInputStream, filename, remoteAgent);
            if (!file.exists()) {
                throw new FileReceiveException("No error on receiving but the file is not exist after saving.");
            }
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

    @AllowLocal
    @PostMapping(value = "/send")
    public ResponseEntity<SendFileResponse> send(
            @RequestParam("file") MultipartFile file,
            @RequestParam("agent") String agentName) {

        String filename = file.getOriginalFilename();
        long filesize = file.getSize();
        logger.info("Sending file, name={}, size={}", filename, filesize);
        try {
            InputStream fileInputStream = file.getInputStream();
            String originalName = file.getOriginalFilename();
            Optional<Agent> optionalAgent = agentService.getAgentByName(agentName);
            if (optionalAgent.isEmpty()) {
                logger.debug("Agent \"{}\" does not exist.", agentName);
                throw new FileSendException("Agent does not exist.");
            }

            fileTransferService.send(fileInputStream, originalName, optionalAgent.get());
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
