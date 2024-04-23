package com.fin4bol.fin4bolbackend.exception.product;

import com.fin4bol.fin4bolbackend.exception.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Exception for product already exists.
 */
public class ProductAlreadyExists extends ApplicationException {

    public ProductAlreadyExists() {
        super("Product already exists!", HttpStatus.CONFLICT);
    }
}
