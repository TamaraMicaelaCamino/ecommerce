package com.techlab.ecommerce.exceptions;

public class CategoriaNoEncontradoException extends RuntimeException {
    public CategoriaNoEncontradoException(Integer id) {


        super("Categoria no encontrada con id: " + id);
    }
}