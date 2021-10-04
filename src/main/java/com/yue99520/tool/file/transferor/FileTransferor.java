package com.yue99520.tool.file.transferor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


@SpringBootApplication
public class FileTransferor implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(FileTransferor.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(FileTransferor.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("Current process id: {}", ProcessHandle.current().pid());
    }
}
