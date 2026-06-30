package com.techlab.ecommerce.services;

import com.techlab.ecommerce.dtos.UsuarioDTO;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    // GET todos los usuarios
    List<UsuarioDTO> getAll();

    // GET usuario por id
    Optional<UsuarioDTO> getById(Integer id);

    // POST crear usuario
    UsuarioDTO create(UsuarioDTO dto);

    // DELETE eliminar usuario
    void remove(Integer id);


    Optional<UsuarioDTO> getByEmail(String email);

    boolean login(String email, String password);

}

