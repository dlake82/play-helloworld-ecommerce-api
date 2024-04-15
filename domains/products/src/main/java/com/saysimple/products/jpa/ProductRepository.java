package com.saysimple.products.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByProductId(String productId);
}
