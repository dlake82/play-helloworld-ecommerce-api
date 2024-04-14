package com.saysimple.products.service;

import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.jpa.ProductEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ProductService extends UserDetailsService {
    ProductDto create(ProductDto productDto);

    ProductDto get(String userId);

    Iterable<ProductEntity> list();

    ProductDto getByEmail(String userName);
}
