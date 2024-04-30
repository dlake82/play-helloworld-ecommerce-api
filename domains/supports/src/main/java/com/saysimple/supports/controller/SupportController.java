package com.saysimple.supports.controller;

import com.saysimple.supports.service.SupportService;
import com.saysimple.supports.vo.RequestSupport;
import io.micrometer.core.annotation.Timed;
import org.saysimple.aop.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supports")
public class SupportController {
    private final Environment env;
    private final SupportService supportService;


    @Autowired
    public SupportController(Environment env, SupportService supportService) {
        this.env = env;
        this.supportService = supportService;
    }

    @GetMapping("/health-check")
    @Timed(value = "users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.seßrver.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", gateway ip(env)=" + env.getProperty("gateway.ip")
                + ", token expiration time=" + env.getProperty("token.expiration_time")
                + ", secret=" + env.getProperty("token.secret")
        );
    }

    @PostMapping("/supports")
    public ResponseEntity<SupportResponse> create(@RequestBody RequestSupport support) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(responseService.create(support));
    }

    @GetMapping
    public ResponseEntity<List<supportResponse>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.list();
    }

    @GetMapping("/{supportId}")
    public ResponseEntity<SupportResponse> get(@PathVariable("supportId") String supportId) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.get(supportId));
    }

    @PutMapping
    public ResponseEntity<SupportResponse> update(@RequestBody SupportUpdateRequest support) {
        return ResponseEntity.status(HttpStatus.OK).body(supportService.update(support));
    }

    @DeleteMapping("/{supportId}")
    public ResponseEntity<Boolean> delete(@PathVariable("supportId") String supportId) {
        supportService.delete(supportId);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
