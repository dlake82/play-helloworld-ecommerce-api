package com.saysimple.products.service;

import com.saysimple.products.entity.ProductEntity;
import com.saysimple.products.repository.ProductRepository;
import com.saysimple.products.vo.ProductRequest;
import com.saysimple.products.vo.ProductRequestUpdate;
import com.saysimple.products.vo.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.saysimple.aop.exception.NotFoundException;
import org.saysimple.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    Environment env;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse create(ProductRequest product) {
        log.info("{}", product);
        ProductEntity productEntity = ModelUtils.strictMap(product, ProductEntity.class);


        productEntity.setProductId(UUID.randomUUID().toString());

        log.info("{}", productEntity);
        productRepository.save(productEntity);

        return ModelUtils.map(productEntity, ProductResponse.class);
    }

    @Override
    public List<ProductResponse> list() {
        List<ProductEntity> productEntities = (List<ProductEntity>) productRepository.findAll();

        return productEntities.stream()
                .map(entity -> ModelUtils.map(entity, ProductResponse.class))
                .toList();
    }


    @Override
    public ProductResponse get(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId).orElseThrow(() ->
                new NotFoundException("Product not found"));

        return ModelUtils.map(productEntity, ProductResponse.class);
    }

    @Override
    public ProductResponse update(ProductRequestUpdate product) {
        ProductEntity productEntity = productRepository.findByProductId(product.getProductId()).orElseThrow(() ->
                new NotFoundException("Product not found"));

        productEntity.setName(product.getName());
        productEntity.setCategoryId(product.getCategoryId());

        return ModelUtils.map(productEntity, ProductResponse.class);
    }

    @Override
    public void delete(String productId) {
        ProductEntity productEntity = productRepository.findByProductId(productId).orElseThrow(() ->
                new NotFoundException("Product not found"));

        productRepository.delete(productEntity);
    }
}
