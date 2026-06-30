package com.techlab.ecommerce.exceptions;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String message) {

        super(message);
    }
}

