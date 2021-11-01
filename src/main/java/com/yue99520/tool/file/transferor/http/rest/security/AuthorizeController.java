package com.yue99520.tool.file.transferor.http.rest.security;

import com.yue99520.tool.file.transferor.dao.AuthorizedAgent;
import com.yue99520.tool.file.transferor.exception.AgentNotExistException;
import com.yue99520.tool.file.transferor.service.security.AllowLocal;
import com.yue99520.tool.file.transferor.service.security.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/authorize")
public class AuthorizeController {

    private final AuthorizeService authorizeService;

    @Autowired
    public AuthorizeController(AuthorizeService authorizeService) {
        this.authorizeService = authorizeService;
    }

    @AllowLocal
    @GetMapping(path = "/partner")
    public ResponseEntity<List<AuthorizedAgent>> authorizedPartner() {
        return ResponseEntity.ok(authorizeService.authorizedAgents());
    }

    @AllowLocal
    @GetMapping(path = "/partner/{agent}")
    public ResponseEntity<String> isAuthorized(@PathVariable String agent) {
        if (authorizeService.isAuthorized(agent)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @AllowLocal
    @PostMapping(path = "/partner/{agent}")
    public ResponseEntity<String> authorizePartner(@PathVariable String agent) {
        try {
            authorizeService.addAuthorization(agent);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (AgentNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @AllowLocal
    @DeleteMapping(path = "/partner/{agent}")
    public ResponseEntity<String> removeAuthorize(@PathVariable String agent) {
        authorizeService.removeAuthorization(agent);
        return ResponseEntity.ok().build();
    }
}
