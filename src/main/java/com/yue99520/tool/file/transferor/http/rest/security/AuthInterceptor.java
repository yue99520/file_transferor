package com.yue99520.tool.file.transferor.http.rest.security;

import com.yue99520.tool.file.transferor.service.connection.ConnectionService;
import com.yue99520.tool.file.transferor.service.security.PartnerSecurityService;
import com.yue99520.tool.file.transferor.service.security.ValidateLocalhost;
import com.yue99520.tool.file.transferor.service.security.ValidatePartner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private final ConnectionService connectionService;
    private final PartnerSecurityService partnerSecurityService;

    public AuthInterceptor(ConnectionService connectionService, PartnerSecurityService partnerSecurityService) {
        this.connectionService = connectionService;
        this.partnerSecurityService = partnerSecurityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.debug("Handling request");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        ValidatePartner validatePartner = handlerMethod.getMethodAnnotation(ValidatePartner.class);
        if (validatePartner != null) {
            return validatePartner(request, response);
        }

        ValidateLocalhost validateLocalhost = handlerMethod.getMethodAnnotation(ValidateLocalhost.class);
        if (validateLocalhost != null) {
            return validateLocalhost(request, response);
        }
        return true;
    }

    private boolean validateLocalhost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String localhost = "0:0:0:0:0:0:0:1";
        String remoteHost = request.getRemoteHost();
        logger.debug("Request remote host \"{}\"", remoteHost);
        if (!remoteHost.equals(localhost)) {
            response.sendError(401, "Unauthorized");
            return false;
        }
        return true;
    }

    private boolean validatePartner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accessKey = request.getHeader(PartnerSecurityService.PARTNER_ACCESS_KEY_HEADER);
        logger.debug("Request access key: \"{}\"", accessKey);
        if (!partnerSecurityService.isValidPartnerAccessKey(accessKey)) {
            response.sendError(401, "Unauthorized");
            return false;
        }
        return true;
    }

    private ValidatePartner firstNotNull(ValidatePartner... annotations) {
        for (ValidatePartner validatePartner : annotations) {
            if (validatePartner != null) {
                return validatePartner;
            }
        }
        return null;
    }
}
