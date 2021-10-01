package com.yue99520.tool.file.transferor.service.security;

public interface PartnerSecurityService {

    String PARTNER_ACCESS_KEY_HEADER = "Partner-Access-Key";

    boolean isValidPartnerAccessKey(String accessKey);
}
