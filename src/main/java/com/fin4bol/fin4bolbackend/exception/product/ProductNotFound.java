package com.fin4bol.fin4bolbackend.exception.product;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Exception for product not found.
 */
public class ProductNotFound extends ApplicationException {

    public ProductNotFound() {
        super("Product not found!", HttpStatus.NOT_FOUND);
    }
}