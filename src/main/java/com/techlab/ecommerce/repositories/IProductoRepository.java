package com.techlab.ecommerce.repositories;

import com.techlab.ecommerce.entities.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("productoRepository")
public interface IProductoRepository extends JpaRepository<ProductoEntity, Integer> {
               //                                                           ↑ int del id


       //Busca un solo producto por su nombre exacto. Devuelve Optional porque puede o no existir
        Optional<ProductoEntity> findByNombre(String nombre);
       //Busca y retorna productos por el id de una categoria
        List<ProductoEntity> findByCategoriaId(Integer categoriaId);
        //Busca y retorna productos por el nombre de una categoria
        List<ProductoEntity> findByCategoriaNombreCategoria(String nombreCategoria);






}

