package com.fin4bol.fin4bolbackend.controller;

import com.fin4bol.fin4bolbackend.controller.json.ProductJson;
import com.fin4bol.fin4bolbackend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.fin4bol.fin4bolbackend.configuration.security.auth.ApplicationUserRole.HAS_ROLE_CUSTOMER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController()
@RequestMapping("/product")
@PreAuthorize(HAS_ROLE_CUSTOMER)
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductJson>> getProducts(@RequestHeader(AUTHORIZATION) String token) {
        return ResponseEntity.ok(productService.getProducts(token));
    }

    @PostMapping()
    public ResponseEntity<List<ProductJson>> saveProduct(@RequestHeader(AUTHORIZATION) String token,
                                            @RequestBody ProductJson product) {
        return ResponseEntity.ok(productService.saveProduct(token, product));
    }

    @DeleteMapping("/{ean-number}")
    public ResponseEntity<List<ProductJson>> deleteProduct(@RequestHeader(AUTHORIZATION) String token,
                                                           @PathVariable("ean-number") String eanNumber) {
        return ResponseEntity.ok(productService.deleteProduct(token, eanNumber));
    }

    @PutMapping()
    public ResponseEntity<List<ProductJson>> updateProduct(@RequestHeader(AUTHORIZATION) String token,
                                                           @RequestBody ProductJson productJson) {
        return ResponseEntity.ok(productService.updateProduct(token, productJson));
    }
}
