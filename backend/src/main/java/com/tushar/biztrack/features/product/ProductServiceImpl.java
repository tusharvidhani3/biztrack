package com.tushar.biztrack.features.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product getProductById(Long productId) {
        return productRepo.findById(productId).get();
    }

    @Override
    public Page<ProductDto> getProducts(String productName, boolean includeInactive, Pageable pageable) {
        Page<ProductDto> products = productRepo.findByProductName(productName, includeInactive, pageable).map(product -> productMapper.toDto(product));
        return products;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepo.save(product);
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepo.findById(productDto.getId()).get();
        productMapper.updateProductFromDto(productDto, product);
        return productMapper.toDto(product);
    }
}
