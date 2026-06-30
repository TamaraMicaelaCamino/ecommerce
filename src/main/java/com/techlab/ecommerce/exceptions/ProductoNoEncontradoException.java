package com.techlab.ecommerce.exceptions;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(Integer id) {

        super("Producto no encontrado con id: " + id);
    }
}