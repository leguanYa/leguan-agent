package com.leguan.agent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description：
 * @Author：LeGuan
 * @Package：com.leguan.agent.controller
 * @Date: 2026/1/20
 */
@RequestMapping("/health")
@RestController
public class HealthController {

    @GetMapping
    public String health() {
        return "ok";
    }
}
