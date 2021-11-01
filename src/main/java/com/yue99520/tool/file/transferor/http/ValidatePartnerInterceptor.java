package com.yue99520.tool.file.transferor.http;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.service.security.AuthorizeService;
import com.yue99520.tool.file.transferor.service.security.AllowPartners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class ValidatePartnerInterceptor implements HandlerInterceptor {
    public static final String REQUEST_ATTR_PARTNER = "AGENT_NAME";
    private static final Logger logger = LoggerFactory.getLogger(ValidatePartnerInterceptor.class);

    private final AuthorizeService authorizeService;

    public ValidatePartnerInterceptor(AuthorizeService uploadAuthorizationService) {
        this.authorizeService = uploadAuthorizationService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        logger.debug("Handling request");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AllowPartners validatePartner = handlerMethod.getMethodAnnotation(AllowPartners.class);
        if (validatePartner != null) {
            return validatePartner(request, response);
        }
        return true;
    }

    private boolean validatePartner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteAddress = request.getRemoteAddr();
        Optional<Agent> optionalAgent = authorizeService.findAgentByAddress(remoteAddress);
        if (optionalAgent.isPresent()) {
            request.setAttribute(REQUEST_ATTR_PARTNER, optionalAgent.get());
            return true;
        }
        response.sendError(HttpStatus.FORBIDDEN.value(), String.format("Unauthorized Address %s", remoteAddress));
        return false;
    }

    private AllowPartners firstNotNull(AllowPartners... annotations) {
        for (AllowPartners allowPartners : annotations) {
            if (allowPartners != null) {
                return allowPartners;
            }
        }
        return null;
    }
}
