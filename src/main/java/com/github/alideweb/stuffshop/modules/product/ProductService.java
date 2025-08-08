package com.github.alideweb.stuffshop.modules.product;

import com.github.alideweb.stuffshop.modules.product.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public ProductEntity newProduct(ProductEntity product) {
        return productRepository.save(product);
    }
}
