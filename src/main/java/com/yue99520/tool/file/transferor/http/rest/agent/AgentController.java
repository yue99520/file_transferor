package com.yue99520.tool.file.transferor.http.rest.agent;

import com.yue99520.tool.file.transferor.dao.Agent;
import com.yue99520.tool.file.transferor.dto.WriteAgentDto;
import com.yue99520.tool.file.transferor.service.agent.AgentService;
import com.yue99520.tool.file.transferor.service.security.AllowLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/agent")
public class AgentController {

    public static Logger logger = LoggerFactory.getLogger(AgentController.class);

    private final AgentService agentService;

    @Autowired
    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @AllowLocal
    @GetMapping(path = "/local")
    public ResponseEntity<Agent> getLocalAgent() {
        Agent agent = agentService.fetchLocalAgent();
        return ResponseEntity.ok(agent);
    }

    @AllowLocal
    @GetMapping(path = "/partner")
    public ResponseEntity<List<Agent>> getAllPartners() {
        List<Agent> agents = agentService.getAgents();
        return ResponseEntity.ok(agents);
    }

    @AllowLocal
    @PostMapping(path = "/partner/{name}")
    public ResponseEntity<?> createPartner(@PathVariable String name, @RequestBody WriteAgentDto agentDto) {
        logger.debug("Creating agent: name={}, agent={}", name, agentDto);
        Agent agent = new Agent();
        agent.setName(name);
        agent.setHost(agentDto.getHost());
        agent.setPort(agentDto.getPort());
        agent.setProtocol(agentDto.getProtocol());

        if (agentService.createAgent(agent)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @AllowLocal
    @GetMapping(path = "/partner/{name}")
    public ResponseEntity<Agent> getPartnerByName(@PathVariable String name) {
        Optional<Agent> optionalAgent = agentService.getAgentByName(name);
        if (optionalAgent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalAgent.get());
    }

    @AllowLocal
    @PutMapping(path = "/partner/{name}")
    public ResponseEntity<Agent> updatePartnerByName(@PathVariable String name, @RequestBody WriteAgentDto agentDto) {
        logger.debug("Updating agent: name={}, agent={}", name, agentDto);
        Agent agent = new Agent();
        agent.setName(name);
        agent.setHost(agentDto.getHost());
        agent.setPort(agentDto.getPort());
        agent.setProtocol(agentDto.getProtocol());

        if (agentService.updateAgent(agent)) {
            return ResponseEntity.ok(agent);
        }
        return ResponseEntity.badRequest().build();
    }

    @AllowLocal
    @DeleteMapping(path = "/partner/{name}")
    public ResponseEntity<String> deletePartner(@PathVariable String name) {
        logger.debug("Deleting agent {}", name);
        agentService.deleteByName(name);
        return ResponseEntity.ok().build();
    }

    @AllowLocal
    @DeleteMapping(path = "/partner")
    public ResponseEntity<String> deleteAllPartners() {
        logger.debug("Deleting all agents");
        agentService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
