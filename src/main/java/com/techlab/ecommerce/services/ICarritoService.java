package com.techlab.ecommerce.services;
import com.techlab.ecommerce.dtos.CarritoDTO;
import java.util.List;
import java.util.Optional;

public interface ICarritoService {


    List<CarritoDTO> getAll(); //trae todos los carritos con sus lineas


    Optional<CarritoDTO> getById(Integer id); //trae un carrito con sus lineas y productos


    //CarritoDTO create();//crea carrito vacio con costoTotal 0


    CarritoDTO addProduct(Integer carritoId, Integer productoId, Integer cantidad);//valida stock, crea linea, recalcula total


    void clear(Integer id);//limpia las lineas, pone total en 0


    void remove(Integer id);//elimina el carrito y sus lineas (cascade)


    CarritoDTO eliminarProducto(Integer carritoId, Integer productoId);

    CarritoDTO actualizarCantidad(Integer carritoId, Integer productoId, Integer cantidad);


     CarritoDTO create(Integer usuarioId);


    Optional<CarritoDTO> getByUsuarioId(Integer usuarioId);

}
