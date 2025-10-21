package com.tushar.biztrack.features.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product getProductById(Long productId);
    Page<ProductDto> getProducts(String productName, boolean includeInactive, Pageable pageable);
    ProductDto addProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);
}
