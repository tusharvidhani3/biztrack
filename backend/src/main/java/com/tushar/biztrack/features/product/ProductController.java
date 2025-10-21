package com.tushar.biztrack.features.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductDto>>> searchProducts(@RequestParam("product-name") String productName, @RequestParam("include-inactive") boolean includeInactive, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "100") int size, @RequestParam("sort-by") String sortBy, @RequestParam("sort-order") String sortOrder, PagedResourcesAssembler<ProductDto> assembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> productDto = productService.getProducts(productName, includeInactive, pageable);
        return ResponseEntity.ok(assembler.toModel(productDto));

    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        productDto = productService.addProduct(productDto);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        productDto = productService.updateProduct(productDto);
        return ResponseEntity.ok(productDto);
    }
}
