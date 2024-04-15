package com.saysimple.products.controller;

import com.saysimple.products.service.ProductService;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class ProductController {
    private final Environment env;
    private final ProductService productService;

    @Autowired
    public ProductController(Environment env, ProductService productService) {
        this.env = env;
        this.productService = productService;
    }

    @GetMapping("/health-check")
    @Timed(value = "products.status", longTask = true)
    public String status() {
        return String.format("It's Working in Product Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", gateway ip(env)=" + env.getProperty("gateway.ip")
                + ", token expiration time=" + env.getProperty("token.expiration_time"));
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseProduct> create(@RequestBody RequestProduct product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ResponseProduct>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.list());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ResponseProduct> get(@PathVariable("productId") String productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.get(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProduct> update(@PathVariable("productId") RequestProduct product) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> delete(@PathVariable("productId") String productId) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.delete(productId));
    }
}
