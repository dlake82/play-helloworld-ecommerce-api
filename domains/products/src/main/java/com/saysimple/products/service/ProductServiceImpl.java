package com.saysimple.products.service;

import com.saysimple.products.client.CatalogServiceClient;
import com.saysimple.products.client.OrderServiceClient;
import com.saysimple.products.dto.InfoDto;
import com.saysimple.products.dto.OptionDto;
import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.entity.Info;
import com.saysimple.products.entity.Option;
import com.saysimple.products.entity.Product;
import com.saysimple.products.repository.ProductRepository;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseProduct create(RequestProduct requestProduct) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Product product = mapper.map(requestProduct, Product.class);
        product.setProductId(UUID.randomUUID().toString());
        Product createdProduct = productRepository.save(product);

        return mapper.map(createdProduct, ResponseProduct.class);
    }

    @Override
    public ProductDto get(String productId) {
        Product product = productRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException("Product not found")
        );

        return new ModelMapper().map(product, ProductDto.class);
    }

    @Override
    public List<ProductDto> list() {
        ModelMapper mapper = new ModelMapper();
        Iterable<Product> productEntities =  productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();

        productEntities.forEach(product -> {
            List<InfoDto> infos = product.getInfos().stream()
                    .map(info -> mapper.map(info, InfoDto.class))
                    .collect(Collectors.toList());
            List<OptionDto> options = product.getOptions().stream()
                    .map(option -> mapper.map(option, OptionDto.class))
                    .collect(Collectors.toList());

            ProductDto productDto = mapper.map(product, ProductDto.class);
            productDto.setInfos(infos);
            productDto.setOptions(options);
            productDtos.add(productDto);
        });

        return productDtos;
    }

    @Override
    @Transactional
    public ProductDto update(ProductDto productDto) {
        ModelMapper mapper = new ModelMapper();
        Product product = productRepository.findByProductId(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());

        List<Info> infos = productDto.getInfos().stream()
                .map(infoDto -> mapper.map(infoDto, Info.class))
                .toList();
        List<Option> options = productDto.getOptions().stream()
                .map(optionDto -> mapper.map(optionDto, Option.class))
                .toList();

        product.setInfos(infos);
        product.setOptions(options);

        return mapper.map(product, ProductDto.class);
    }

    @Override
    @Transactional
    public void delete(String productId) {
        productRepository.delete(
                productRepository.findByProductId(productId).orElseThrow(() -> new NotFoundException("Product not found"))
        );
    }
}
