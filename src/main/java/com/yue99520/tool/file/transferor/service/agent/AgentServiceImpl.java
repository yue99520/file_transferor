package com.yue99520.tool.file.transferor.service.agent;

import com.google.common.base.Suppliers;
import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.repository.AgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class AgentServiceImpl implements AgentService{

    public static final String RESERVED_LOCAL_AGENT_NAME = "LOCAL";
    public static final String PROTOCOL = "http";

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);
    private static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    private Supplier<Agent> localAgentSupplier;

    private final AgentRepository agentRepository;

    @Value("${server.port}")
    private int applicationServerPort;

    @PostConstruct
    private void postConstruct() {
        localAgentSupplier = Suppliers.memoizeWithExpiration(() -> {
            try {
                InetAddress localHost = Inet4Address.getLocalHost();
                Agent agent = new Agent();
                agent.setName(RESERVED_LOCAL_AGENT_NAME);
                agent.setHost(localHost.getHostAddress());
                agent.setProtocol(PROTOCOL);
                agent.setPort(applicationServerPort);
                logger.debug("Updating local agent...");
                return agent;
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }, 30, TimeUnit.SECONDS);
    }

    @Autowired
    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @Override
    public synchronized Agent fetchLocalAgent() {
        Agent newAgent = localAgentSupplier.get();
        Optional<Agent> optionalOldAgent = agentRepository.findById(RESERVED_LOCAL_AGENT_NAME);
        if (optionalOldAgent.isEmpty()) {
            agentRepository.save(newAgent);
            return newAgent;
        }
        Agent oldAgent = optionalOldAgent.get();
        if (newAgent.equals(oldAgent)) {
            return newAgent;
        }
        logger.info("Found local agent net changes.");
        logger.info("Old local agent: {}", oldAgent);
        logger.info("New local agent: {}", newAgent);
        agentRepository.save(newAgent);
//        broadcastLocalAgent(newAgent); TODO broadcast
        return newAgent;
    }

    @Override
    public List<Agent> getAgents() {
        return agentRepository.findAll();
    }

    @Override
    public Optional<Agent> getAgentByName(String name) {
        if (name.equals(RESERVED_LOCAL_AGENT_NAME)) {
            return Optional.of(fetchLocalAgent());
        }
        return agentRepository.findById(name);
    }

    @Override
    public boolean createAgent(Agent agent) {
        if (agent.getName().equals(RESERVED_LOCAL_AGENT_NAME)) {
            return false;
        }
        if (agentRepository.existsById(agent.getName())) {
            return false;
        }
        agentRepository.save(agent);
        return true;
    }

    @Override
    public synchronized boolean updateAgent(Agent agent) {
        if (agent.getName().equals(RESERVED_LOCAL_AGENT_NAME)) {
            return false;
        }
        agentRepository.save(agent);
        return true;
    }

    @Override
    public void deleteByName(String name) {
        agentRepository.deleteById(name);
    }

    @Override
    public void deleteAll() {
        agentRepository.deleteAll();
    }

    private void broadcastLocalAgent(Agent local) {
        logger.info("Broadcasting local agent...");
        List<Agent> partners = agentRepository.findAll();
        for (Agent partner : partners) {
            if (!partner.getName().equals(RESERVED_LOCAL_AGENT_NAME)) {
                executor.submit(() -> {
                    // TODO: 2021/11/1 Implement broadcast local agent
                });
            }
        }
    }
}
