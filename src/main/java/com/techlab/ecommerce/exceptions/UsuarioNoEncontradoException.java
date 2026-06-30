package com.techlab.ecommerce.exceptions;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(Integer id) {

        super("Usuario no encontrado con id: " + id);


    }

    public UsuarioNoEncontradoException(String email) {

        super("Usuario no encontrado con email: " + email);
    }

}
