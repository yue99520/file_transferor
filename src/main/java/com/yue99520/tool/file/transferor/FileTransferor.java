package com.yue99520.tool.file.transferor;


import com.sun.istack.NotNull;
import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.service.agent.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNullApi;


@SpringBootApplication
public class FileTransferor implements ApplicationListener<ContextRefreshedEvent> {

    public static void main(String[] args) {
        SpringApplication.run(FileTransferor.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(FileTransferor.class);

    private final AgentService agentService;

    public FileTransferor(AgentService agentService) {
        this.agentService = agentService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Agent current = agentService.fetchLocalAgent();
        logger.info("");
        logger.info("----Service info-----");
        logger.info("\tpid: {}", ProcessHandle.current().pid());
        logger.info("\tprotocol: {}", current.getProtocol());
        logger.info("\tname: {}", current.getName());
        logger.info("\thost: {}", current.getHost());
        logger.info("\tport: {}", current.getPort());
        logger.info("");
    }
}
