package com.yue99520.tool.file.transferor.repository;

import com.yue99520.tool.file.transferor.dao.AuthorizedAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizedAgentRepository extends JpaRepository<AuthorizedAgent, Long> {

    Optional<AuthorizedAgent> findAuthorizedAgentByAgent_Host(String agentHost);

    boolean existsAuthorizedAgentByAgent_Name(String agentName);

    Optional<AuthorizedAgent> getAuthorizedAgentByAgent_Name(String agentName);
}
