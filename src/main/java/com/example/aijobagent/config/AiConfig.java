package com.example.aijobagent.config;

import com.example.aijobagent.tools.CalculatorTool;
import com.example.aijobagent.tools.JobSearchTool;
import com.example.aijobagent.tools.SkillMatchTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder,
                                 JobSearchTool jobSearchTool,
                                 SkillMatchTool skillMatchTool,
                                 CalculatorTool calculatorTool) {
        return builder
                .defaultSystem("""
                        You are an AI Job Assistant Agent.
                        Answer clearly and simply.
                        Use tools when searching jobs, comparing skills, or calculating numbers.
                        Never invent job information. If matching jobs are not found in the database, say so.
                        When recommending jobs, base recommendations only on tool results and available job data.
                        """)
                .defaultTools(jobSearchTool, skillMatchTool, calculatorTool)
                .build();
    }
}
