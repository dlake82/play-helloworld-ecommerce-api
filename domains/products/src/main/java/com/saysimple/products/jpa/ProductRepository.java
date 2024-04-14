package com.saysimple.products.jpa;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByUserId(String userId);

    ProductEntity findByEmail(String username);
}
