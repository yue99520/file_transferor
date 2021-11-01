package com.yue99520.tool.file.transferor.service.security;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.dao.AuthorizedAgent;
import com.yue99520.tool.file.transferor.exception.AgentNotExistException;

import java.util.List;
import java.util.Optional;

public interface AuthorizeService {

    List<AuthorizedAgent> authorizedAgents();

    void addAuthorization(String agentName) throws AgentNotExistException;

    void removeAuthorization(String agentName);

    boolean isAuthorized(String agentName);

    Optional<Agent> findAgentByAddress(String address);
}
