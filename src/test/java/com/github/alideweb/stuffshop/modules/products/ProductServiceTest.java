package com.github.alideweb.stuffshop.modules.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void testFindAll() {
        ProductEntity p1 = new ProductEntity(1, "Book", "Book desc", BigDecimal.valueOf(10));
        ProductEntity p2 = new ProductEntity(2, "Pen", "Pen desc", BigDecimal.valueOf(5));

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProductEntity> products = productService.findAll();

        assertEquals(2, products.size());
        assertEquals("Book", products.getFirst().getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        ProductEntity product = new ProductEntity(1, "Book", "Book desc", BigDecimal.valueOf(10));

        when(productRepository.save(product)).thenReturn(product);

        ProductEntity result = productService.save(product);

        assertEquals(product, result);
        assertEquals(product.getPrice(), result.getPrice());
        verify(productRepository, times(1)).save(product);
    }
}
