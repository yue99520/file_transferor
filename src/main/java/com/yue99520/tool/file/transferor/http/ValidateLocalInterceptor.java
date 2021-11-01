package com.yue99520.tool.file.transferor.http;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.service.agent.AgentService;
import com.yue99520.tool.file.transferor.service.security.AllowLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateLocalInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ValidateLocalInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        logger.debug("Handling request");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AllowLocal allowLocal = handlerMethod.getMethodAnnotation(AllowLocal.class);
        if (allowLocal != null) {
            return validateLocalhost(request, response);
        }
        return true;
    }

    private boolean validateLocalhost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteAddress = request.getLocalAddr();
        if (remoteAddress.equals("127.0.0.1") || remoteAddress.equals("0:0:0:0:0:0:0:1")) {
            return true;
        }
        response.sendError(HttpStatus.FORBIDDEN.value(), String.format("Unauthorized Address %s", remoteAddress));
        return false;
    }
}
