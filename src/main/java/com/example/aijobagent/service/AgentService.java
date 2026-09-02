package com.example.aijobagent.service;

import com.example.aijobagent.exception.AiAgentException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AgentService {

    private static final Pattern OPENAI_KEY_PATTERN = Pattern.compile("sk-[A-Za-z0-9_\\-]+");

    private final ChatClient chatClient;

    public AgentService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String ask(String question) {
        try {
            String answer = chatClient.prompt()
                    .user(question)
                    .call()
                    .content();

            if (answer == null || answer.isBlank()) {
                return "I could not generate an answer for that question.";
            }
            return answer;
        } catch (RuntimeException exception) {
            throw new AiAgentException("AI service request failed: " + sanitize(rootMessage(exception)), exception);
        }
    }

    private String rootMessage(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        if (current.getMessage() == null || current.getMessage().isBlank()) {
            return current.getClass().getSimpleName();
        }
        return current.getMessage();
    }

    private String sanitize(String message) {
        return OPENAI_KEY_PATTERN.matcher(message).replaceAll("sk-***");
    }
}
