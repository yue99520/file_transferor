package com.yue99520.tool.file.transferor.http;

import com.yue99520.tool.file.transferor.http.rest.security.AuthInterceptor;
import com.yue99520.tool.file.transferor.service.connection.ConnectionService;
import com.yue99520.tool.file.transferor.service.security.PartnerSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final ConnectionService connectionService;
    private final PartnerSecurityService partnerSecurityService;

    @Autowired
    public InterceptorConfig(ConnectionService connectionService, PartnerSecurityService partnerSecurityService) {
        this.connectionService = connectionService;
        this.partnerSecurityService = partnerSecurityService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(connectionService, partnerSecurityService));
    }
}
