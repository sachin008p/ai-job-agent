package com.example.aijobagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {

    @Tool(description = "Add two integer numbers.")
    public int add(
            @ToolParam(description = "First integer") int first,
            @ToolParam(description = "Second integer") int second) {
        return first + second;
    }

    @Tool(description = "Calculate what percentage a value is of a total.")
    public double percentage(
            @ToolParam(description = "Value to convert to percentage") double value,
            @ToolParam(description = "Total value") double total) {
        if (total == 0) {
            return 0.0;
        }
        return Math.round((value * 100.0 / total) * 100.0) / 100.0;
    }
}
