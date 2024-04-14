package com.saysimple.products.service;

import com.saysimple.products.client.CatalogServiceClient;
import com.saysimple.products.client.OrderServiceClient;
import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.jpa.ProductEntity;
import com.saysimple.products.jpa.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.UUID;

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
                              OrderServiceClient orderServiceClient,
                              CatalogServiceClient catalogServiceClient,
                              CircuitBreakerFactory circuitBreakerFactory) {
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
        this.env = env;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ProductEntity productEntity = productRepository.findByEmail(username);

        if (productEntity == null)
            throw new UsernameNotFoundException(username + ": not found");

        return new User(productEntity.getEmail(), productEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        productDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductEntity productEntity = mapper.map(productDto, ProductEntity.class);
        productEntity.setEncryptedPwd(passwordEncoder.encode(productDto.getPwd()));

        productRepository.save(productEntity);

        ProductDto returnProductDto = mapper.map(productEntity, ProductDto.class);

        return returnProductDto;
    }

    @Override
    public ProductDto get(String userId) {
        ProductEntity productEntity = productRepository.findByUserId(userId);

        log.info("UserEntity {}", productEntity);

        if (productEntity == null)
            throw new UsernameNotFoundException("User not found");

        ProductDto productDto = new ModelMapper().map(productEntity, ProductDto.class);

        log.info("Before call orders microservice");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
        List<ResponseOrder> orders = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
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
