package com.techlab.ecommerce.services;

import com.techlab.ecommerce.dtos.CategoriaDTO;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    Optional<CategoriaDTO> getById(Integer id); //GET por id

    CategoriaDTO create(CategoriaDTO dto);// POST CREAR

    List<CategoriaDTO> getAll(); // GET LISTA

    CategoriaDTO update(Integer id, CategoriaDTO dto);  // PUT actualizarTodo

    CategoriaDTO patch(Integer id, CategoriaDTO dto);   // PATCH actualizar a eleccion

    void remove(Integer id);              // DELETE eliminar


}
