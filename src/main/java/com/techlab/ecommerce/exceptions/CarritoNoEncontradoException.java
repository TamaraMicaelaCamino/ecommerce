package com.techlab.ecommerce.exceptions;

public class CarritoNoEncontradoException extends RuntimeException {

    public CarritoNoEncontradoException(Integer id) {

        super("No existe el carrito con el id: " + id);
    }
}
