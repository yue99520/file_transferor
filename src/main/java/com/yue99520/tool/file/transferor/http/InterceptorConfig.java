package com.yue99520.tool.file.transferor.http;

import com.yue99520.tool.file.transferor.service.agent.AgentService;
import com.yue99520.tool.file.transferor.service.security.AuthorizeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final AgentService agentService;

    private final AuthorizeService authorizeService;

    public InterceptorConfig(AgentService agentService, AuthorizeService authorizeService) {
        this.agentService = agentService;
        this.authorizeService = authorizeService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ValidatePartnerInterceptor(authorizeService));
        registry.addInterceptor(new ValidateLocalInterceptor());
    }
}
