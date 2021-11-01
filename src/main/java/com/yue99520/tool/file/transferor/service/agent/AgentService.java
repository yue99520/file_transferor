package com.yue99520.tool.file.transferor.service.agent;

import com.yue99520.tool.file.transferor.dao.Agent;

import java.util.List;
import java.util.Optional;

public interface AgentService {

    Agent fetchLocalAgent();

    Optional<Agent> getAgentByName(String name);

    List<Agent> getAgents();

    boolean createAgent(Agent agent);

    boolean updateAgent(Agent agent);

    void deleteByName(String name);

    void deleteAll();
}
