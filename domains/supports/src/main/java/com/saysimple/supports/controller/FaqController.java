package com.saysimple.supports.controller;

import com.saysimple.supports.service.FaqService;
import com.saysimple.supports.vo.ListFaq;
import com.saysimple.supports.vo.RequestFaq;
import com.saysimple.supports.vo.RequestUpdateFaq;
import com.saysimple.supports.vo.ResponseFaq;
import io.micrometer.core.annotation.Timed;
import org.saysimple.aop.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/faq")
@RestController
public class FaqController {
    private final Environment env;
    private final FaqService faqService;


    @Autowired
    public FaqController(Environment env, FaqService faqService) {
        this.env = env;
        this.faqService = faqService;
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

    @PostMapping("/create")
    public ResponseEntity<ResponseFaq> create(@RequestBody RequestFaq faq) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.create(faq));
    }

    @GetMapping
    public ResponseEntity<List<ListFaq>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.list());
    }

    @GetMapping("/{faqId}")
    public ResponseEntity<ResponseFaq> get(@PathVariable("faqId") String faqId) {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.get(faqId));
    }

    @PutMapping("/put")
    public ResponseEntity<ResponseFaq> update(@RequestBody RequestUpdateFaq faq) {
        return ResponseEntity.status(HttpStatus.OK).body(faqService.update(faq));
    }

    @DeleteMapping("/{faqId}")
    public ResponseEntity<Boolean> delete(@PathVariable("faqId") String faqId) {
        faqService.delete(faqId);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}