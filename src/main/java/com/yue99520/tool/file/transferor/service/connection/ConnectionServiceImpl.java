package com.yue99520.tool.file.transferor.service.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionServiceImpl.class);

    @Value("${partner.host}")
    private String partnerHost;

    @Value("${partner.port}")
    private String partnerPort;

    @Value("${partner.name}")
    private String partnerName;

    @PostConstruct
    private void postConstruct() {
        logger.info("Using configuration [partner.host={}]", partnerHost);
        logger.info("Using configuration [partner.port={}]", partnerPort);
        logger.info("Using configuration [partner.name={}]", partnerName);
    }

    @Override
    public Partner getPartner() {
        Partner partner = new Partner();
        partner.setHost(partnerHost);
        partner.setPort(Integer.parseInt(partnerPort));
        partner.setName(partnerName);
        return partner;
    }

    @Override
    public boolean ping(String partnerName) {
        return false;
    }

    public void setPartnerHost(String partnerHost) {
        this.partnerHost = partnerHost;
    }

    public void setPartnerPort(String partnerPort) {
        this.partnerPort = partnerPort;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
