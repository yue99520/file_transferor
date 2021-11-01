package com.yue99520.tool.file.transferor.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;


@Entity
public class Agent {

    @Id
    private String name;

    private String host;

    private int port;

    private String protocol;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return port == agent.port && Objects.equals(name, agent.name) && Objects.equals(host, agent.host) && Objects.equals(protocol, agent.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, host, port, protocol);
    }

    @Override
    public String toString() {
        return "Agent{" + "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
