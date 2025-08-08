package com.github.alideweb.stuffshop.modules.product;

import com.github.alideweb.stuffshop.modules.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
