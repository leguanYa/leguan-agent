package com.leguan.agent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WeatherTools {

    
    @Tool(description = "Get today's weather")
    public String getWeather(@ToolParam(description = "city name") String city) {
        return "上海今天的天气为100度";
    }
}
