package com.yue99520.tool.file.transferor.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PartnerSecurityServiceImpl implements PartnerSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerSecurityServiceImpl.class);

    @Value("${partner.keys}")
    private String partnerKeys;

    private List<String> keyWhiteList;

    @PostConstruct
    private void postConstruct() {
        if (partnerKeys == null) {
            logger.error("Configuration not found [partner.host]");
        } else {
            keyWhiteList = Arrays.stream(partnerKeys.split(","))
                    .map(key -> {
                        key = key.trim().toLowerCase();
                        logger.info("Add partner key to whitelist: {}", key);
                        return key;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public boolean isValidPartnerAccessKey(String accessKey) {
        return keyWhiteList.contains(accessKey);
    }

    public void setPartnerKeys(String partnerKeys) {
        this.partnerKeys = partnerKeys;
    }
}
