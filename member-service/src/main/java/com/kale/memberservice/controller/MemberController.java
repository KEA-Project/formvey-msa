package com.kale.memberservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/member-service")
public class MemberController {
    Environment env;

    @Autowired
    public MemberController(Environment env) {
        this.env = env;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("check is called in Member Service");
        log.info("Server Port from HttpServletRequest: port = {}", request.getServerPort());
        log.info("Server Port from Environment: port = {}", env.getProperty("local.server.port"));

        return String.format("check is called in Member Service, Server port is %s from HttpServletRequest and %s from Environment", request.getServerPort(), env.getProperty("local.server.port"));
    }

}
