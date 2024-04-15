package com.saysimple.products.controller;

import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.entity.Product;
import com.saysimple.products.repository.ProductRepository;
import com.saysimple.products.service.ProductService;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class ProductController {
    private final Environment env;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(Environment env, ProductService productService, ProductRepository productRepository) {
        this.env = env;
        this.productService = productService;
        this.productRepository = productRepository;
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
        ProductDto productDto = productService.get(productId);

        ResponseProduct returnValue = new ModelMapper().map(productDto, ResponseProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseProduct> update(@PathVariable("productId") String productId) {
        ProductDto productDto = productService.update()

        ResponseProduct returnValue = new ModelMapper().map(productDto, ResponseProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseProduct> delete(@PathVariable("productId") String productId) {
        ProductDto productDto = productService.get(productId);

        ResponseProduct returnValue = new ModelMapper().map(productDto, ResponseProduct.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<ResponseProduct>>> getWithHateoas() {
        List<EntityModel<ResponseProduct>> result = new ArrayList<>();
        Iterable<Product> products = productService.list();

        for (Product product : products) {
            EntityModel entityModel = EntityModel.of(product);
            entityModel.add(linkTo(methodOn(this.getClass()).get(product.getProductId())).withSelfRel());

            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).getWithHateoas()).withSelfRel()));
    }
}
