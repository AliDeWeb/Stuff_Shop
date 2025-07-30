package com.github.alideweb.stuffshop.modules.products;

import com.github.alideweb.stuffshop.modules.products.dto.ProductCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> findAll() {
        List<ProductEntity> products = productService.findAll();

        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<ProductEntity> save(@Valid @RequestBody ProductCreateRequestDto productDto) {
        ProductEntity product = new ProductEntity(0, productDto.getName(), productDto.getDescription(), productDto.getPrice());

        ProductEntity result = this.productService.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
