package com.techlab.ecommerce.services;

import com.techlab.ecommerce.dtos.ProductoDTO;

import java.util.List;
import java.util.Optional;


public interface IProductoService { //metodos de negocio



        List<ProductoDTO> getAll();           // GET todos

        Optional<ProductoDTO> getById(Integer id);  // GET por id

        ProductoDTO create(ProductoDTO dto);   // POST crear

        ProductoDTO update(Integer id, ProductoDTO dto);  // PUT actualizarTodo

        ProductoDTO patch(Integer id, ProductoDTO dto);   // PATCH actualizar a eleccion

        void remove(Integer id);              // DELETE eliminar

        List<ProductoDTO> getByCategoriaId(Integer categoriaId);  // GET id por categoria

        List<ProductoDTO> getByCategoryName(String nombre); // GET nombre por categoria

        Optional<ProductoDTO> getByName(String nombre); //GET nombre producto


}





//Lo que tenías en la BD    +    Lo que mandás en el PUT    =    Resultado
//──────────────────────────────────────────────────────────────────────
//nombreCategoria: "ROPA"        nombreCategoria: "Ropa dep"    "Ropa dep"
//descripcionCategoria: "ALG"    descripcionCategoria: "ALG"    "ALG"  ← repetís