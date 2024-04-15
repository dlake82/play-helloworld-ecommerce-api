package com.saysimple.products.service;

import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.jpa.ProductEntity;
import com.saysimple.products.jpa.ProductRepository;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.saysimple.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    BCryptPasswordEncoder passwordEncoder;

    Environment env;
    CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              BCryptPasswordEncoder passwordEncoder,
                              Environment env,
                              RestTemplate restTemplate,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public ResponseProduct create(RequestProduct product) {
        ProductEntity productEntity = ModelUtils.mapper(product, ProductEntity.class);
        productRepository.save(productEntity);

        return ModelUtils.mapper(productEntity, ResponseProduct.class);
    }

    @Override
    public ProductDto get(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId);

        log.info("UserEntity {}", productEntity);

        if (productEntity == null)
            throw new UsernameNotFoundException("User not found");

        ProductDto productDto = new ModelMapper().map(productEntity, ProductDto.class);

        log.info("Before call orders microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(productId),
                throwable -> new ArrayList<>());
        productDto.setOrders(orders);
        log.info("After called orders microservice");

        return productDto;
    }

    @Override
    public Iterable<ProductEntity> list() {
        return productRepository.findAll();
    }

    @Override
    public ProductDto getByEmail(String email) {
        ProductEntity productEntity = productRepository.findByEmail(email);
        if (productEntity == null)
            throw new UsernameNotFoundException(email);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(productEntity, ProductDto.class);
        return productDto;
    }
}
