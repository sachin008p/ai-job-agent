package com.example.aijobagent.controller;

import com.example.aijobagent.dto.AgentAskRequest;
import com.example.aijobagent.dto.AgentAskResponse;
import com.example.aijobagent.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AgentAskResponse> ask(@Valid @RequestBody AgentAskRequest request) {
        String answer = agentService.ask(request.question());
        return ResponseEntity.ok(new AgentAskResponse(request.question(), answer));
    }
}
