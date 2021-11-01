package com.yue99520.tool.file.transferor.service.security;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.dao.AuthorizedAgent;
import com.yue99520.tool.file.transferor.exception.AgentNotExistException;
import com.yue99520.tool.file.transferor.repository.AuthorizedAgentRepository;
import com.yue99520.tool.file.transferor.service.agent.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    private final AuthorizedAgentRepository authorizedAgentRepository;
    private final AgentService agentService;

    @Autowired
    public AuthorizeServiceImpl(AuthorizedAgentRepository authorizedAgentRepository, AgentService agentService) {
        this.authorizedAgentRepository = authorizedAgentRepository;
        this.agentService = agentService;
    }

    @Override
    public List<AuthorizedAgent> authorizedAgents() {
        return authorizedAgentRepository.findAll();
    }

    @Override
    public synchronized void addAuthorization(String agentName) throws AgentNotExistException {
        if (!isAuthorized(agentName)) {
            Optional<Agent> optionalAgent = agentService.getAgentByName(agentName);
            if (optionalAgent.isEmpty()) {
                throw new AgentNotExistException("Agent name does not exist.");
            }
            AuthorizedAgent authorizedAgent = new AuthorizedAgent();
            authorizedAgent.setAgent(optionalAgent.get());
            authorizedAgent.setAuthorizedAt(System.currentTimeMillis());
            authorizedAgentRepository.save(authorizedAgent);
        }
    }

    @Override
    public void removeAuthorization(String agentName) {
        Optional<AuthorizedAgent> optionalAuthorizedAgent = authorizedAgentRepository.getAuthorizedAgentByAgent_Name(agentName);
        optionalAuthorizedAgent.ifPresent(authorizedAgentRepository::delete);
    }

    @Override
    public boolean isAuthorized(String agentName) {
        return authorizedAgentRepository.existsAuthorizedAgentByAgent_Name(agentName);
    }

    @Override
    public Optional<Agent> findAgentByAddress(String address) {
        Optional<AuthorizedAgent> optionalAuthorizedAgent = authorizedAgentRepository.findAuthorizedAgentByAgent_Host(address);
        return optionalAuthorizedAgent.map(AuthorizedAgent::getAgent);
    }
}
