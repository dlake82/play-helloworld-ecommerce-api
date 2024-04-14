package com.saysimple.products.controller;

import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.service.ProductService;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import io.micrometer.core.annotation.Timed;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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
    public ResponseEntity<ResponseProduct> create(@RequestBody RequestProduct user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(user, ProductDto.class);
        productService.create(productDto);

        ResponseUser responseUser = mapper.map(productDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ResponseUser>> list() {
        Iterable<ProductEntity> userList = productService.list();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUser> get(@PathVariable("userId") String userId) {
        ProductDto productDto = productService.get(userId);

        ResponseUser returnValue = new ModelMapper().map(productDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }


    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<ResponseUser>>> getWithHateoas() {
        List<EntityModel<ResponseUser>> result = new ArrayList<>();
        Iterable<ProductEntity> users = productService.list();

        for (ProductEntity user : users) {
            EntityModel entityModel = EntityModel.of(user);
            entityModel.add(linkTo(methodOn(this.getClass()).get(user.getUserId())).withSelfRel());

            result.add(entityModel);
        }

        return ResponseEntity.ok(CollectionModel.of(result, linkTo(methodOn(this.getClass()).getWithHateoas()).withSelfRel()));
    }
}
