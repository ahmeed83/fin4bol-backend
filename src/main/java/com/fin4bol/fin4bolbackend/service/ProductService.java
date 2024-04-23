package com.fin4bol.fin4bolbackend.service;

import com.fin4bol.fin4bolbackend.configuration.security.jwt.JwtTokenVerifier;
import com.fin4bol.fin4bolbackend.controller.json.ProductJson;
import com.fin4bol.fin4bolbackend.controller.json.ProductUpdateJson;
import com.fin4bol.fin4bolbackend.exception.product.ProductAlreadyExists;
import com.fin4bol.fin4bolbackend.repository.ProductRepository;
import com.fin4bol.fin4bolbackend.repository.entiry.ApplicationUser;
import com.fin4bol.fin4bolbackend.repository.entiry.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationUserService applicationUserService;
    private final JwtTokenVerifier jwtTokenVerifier;

    public ProductService(final ProductRepository productRepository,
                          final ApplicationUserService applicationUserService,
                          final JwtTokenVerifier jwtTokenVerifier) {
        this.productRepository = productRepository;
        this.applicationUserService = applicationUserService;
        this.jwtTokenVerifier = jwtTokenVerifier;
    }

    public List<ProductJson> getProducts(final String token) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        return getProductList(applicationUserService.findUserByEmail(email))
                .stream()
                .map(this::mapProductToJson)
                .toList();
    }

    public List<ProductJson> saveProduct(final String token, final ProductJson productJson) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser applicationUser = applicationUserService.findUserByEmail(email);
        if (productRepository.findByApplicationUserIdAndEanNumber
                (applicationUser, productJson.getEanNumber()).isPresent()) {
            throw new ProductAlreadyExists();
        }
        Product productEntity = new Product();
        productEntity.setApplicationUserId(applicationUser);
        productEntity.setName(productJson.getName());
        final LocalDateTime now = LocalDateTime.now();
        productEntity.setCreatedAt(now);
        productEntity.setUpdatedAt(now);
        productEntity.setEanNumber(productJson.getEanNumber());
        productEntity.setPurchaseCost(productJson.getPurchaseCost());
        productRepository.save(productEntity);
        return getProductList(applicationUser)
                .stream()
                .map(this::mapProductToJson)
                .toList();
    }

    private List<Product> getProductList(final ApplicationUser applicationUser) {
        return productRepository.findByApplicationUserIdOrderByUpdatedAt(applicationUser)
                .orElseThrow(() -> new RuntimeException("Products not found"));
    }

    private ProductJson mapProductToJson(Product product) {
        ProductJson productJson = new ProductJson();
        productJson.setName(product.getName());
        productJson.setEanNumber(product.getEanNumber());
        productJson.setPurchaseCost(product.getPurchaseCost());
        return productJson;
    }

    @Transactional
    public List<ProductJson> deleteProduct(final String token, final String eanNumber) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser applicationUser = applicationUserService.findUserByEmail(email);
        productRepository.deleteProductsByApplicationUserIdAndEanNumber(applicationUser, eanNumber);
        return getProductList(applicationUser)
                .stream()
                .map(this::mapProductToJson)
                .toList();
    }

    @Transactional
    public List<ProductJson> updateProduct(final String token, final ProductUpdateJson productUpdateJson) {
        final String email = jwtTokenVerifier.extractUserEmail(token);
        final ApplicationUser applicationUser = applicationUserService.findUserByEmail(email);
        Product product = productRepository.findByApplicationUserIdAndEanNumber(applicationUser, productUpdateJson.getEanNumber())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setUpdatedAt(LocalDateTime.now());
        product.setName(productUpdateJson.getName() != null ? productUpdateJson.getName() : product.getName());
        final String eanNumber = productUpdateJson.getEanNumber() != null ? productUpdateJson.getEanNumber() : product.getEanNumber();
        final String eanNumberToBeUpdated =
                productUpdateJson.getToUpdateEanNumber() != null ? productUpdateJson.getToUpdateEanNumber() : eanNumber;
        product.setEanNumber(eanNumberToBeUpdated);
        product.setPurchaseCost(productUpdateJson.getPurchaseCost() != null ? productUpdateJson.getPurchaseCost() : product.getPurchaseCost());
        productRepository.save(product);
        return getProductList(applicationUser)
                .stream()
                .map(this::mapProductToJson)
                .toList();
    }
}
