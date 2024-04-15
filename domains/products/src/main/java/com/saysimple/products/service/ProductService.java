package com.saysimple.products.service;

import com.saysimple.products.dto.ProductDto;
import com.saysimple.products.vo.RequestProduct;
import com.saysimple.products.vo.ResponseProduct;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface ProductService extends UserDetailsService {
    ResponseProduct create(RequestProduct product);
    List<ResponseProduct> list();
    ResponseProduct get(String productId);
    ResponseProduct update(ProductDto productDto);
    void delete(String productId);
}