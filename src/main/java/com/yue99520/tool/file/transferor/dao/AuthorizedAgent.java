package com.yue99520.tool.file.transferor.dao;

import javax.persistence.*;


@Entity
public class AuthorizedAgent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long authorizeId;

    // TODO: 2021/11/1 AuthorizedAgent should be deleted when doing Agent deletion.
    @OneToOne
    private Agent agent;

    private long authorizedAt;

    public long getAuthorizeId() {
        return authorizeId;
    }

    public void setAuthorizeId(long authorizeId) {
        this.authorizeId = authorizeId;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public long getAuthorizedAt() {
        return authorizedAt;
    }

    public void setAuthorizedAt(long authorizedAt) {
        this.authorizedAt = authorizedAt;
    }
}
