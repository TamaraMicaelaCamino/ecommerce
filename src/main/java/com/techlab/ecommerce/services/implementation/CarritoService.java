package com.techlab.ecommerce.services.implementation;
import com.techlab.ecommerce.entities.UsuarioEntity;
import com.techlab.ecommerce.exceptions.CarritoNoEncontradoException;
import com.techlab.ecommerce.dtos.CarritoDTO;
import com.techlab.ecommerce.entities.CarritoEntity;
import com.techlab.ecommerce.entities.LineaCarritoEntity;
import com.techlab.ecommerce.entities.ProductoEntity;
import com.techlab.ecommerce.exceptions.ProductoNoEncontradoException;
import com.techlab.ecommerce.exceptions.StockInsuficienteException;
import com.techlab.ecommerce.exceptions.UsuarioNoEncontradoException;
import com.techlab.ecommerce.repositories.ICarritoRepository;
import com.techlab.ecommerce.repositories.IProductoRepository;
import com.techlab.ecommerce.repositories.IUsuarioRepository;
import com.techlab.ecommerce.services.ICarritoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("carritoService")
@RequiredArgsConstructor

public class CarritoService implements ICarritoService {

    private final ICarritoRepository carritoRepository;
    private final IProductoRepository productoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public List<CarritoDTO> getAll() {
        return carritoRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, CarritoDTO.class))
                .toList();
    }


    @Transactional
    @Override
    public Optional<CarritoDTO> getById(Integer id) {
        return carritoRepository.findById(id)
                .map(e -> modelMapper.map(e, CarritoDTO.class));
    }

    @Transactional
    @Override
    public CarritoDTO create(Integer usuarioId) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNoEncontradoException(usuarioId));

        CarritoEntity carrito = new CarritoEntity();
        carrito.setCostoTotal(0.0);
        carrito.setUsuario(usuario);

        return modelMapper.map(carritoRepository.save(carrito), CarritoDTO.class);
    }

    @Override
    @Transactional
    public CarritoDTO addProduct(Integer carritoId, Integer productoId, Integer cantidad) {
        // busca el carrito
        CarritoEntity carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new CarritoNoEncontradoException(carritoId));

        // busca el producto
        ProductoEntity producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ProductoNoEncontradoException(productoId));

        // verifica stock
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para: " + producto.getNombre());
        }

        // desconta el stock
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        //  verifica si el producto ya está en el carrito
        LineaCarritoEntity linea = carrito.getLineaCarrito()
                .stream()
                .filter(l -> l.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);

        if (linea != null) {
            // si ya existe suma la cantidad
            linea.setCantidad(linea.getCantidad() + cantidad);
        } else {
            //si no existe crea una nueva línea
            linea = new LineaCarritoEntity();
            linea.setCarrito(carrito);
            linea.setProducto(producto);
            linea.setCantidad(cantidad);
            linea.setPrecioUnitario(producto.getPrecio());
            carrito.getLineaCarrito().add(linea);
        }

        // recalcula el costo total
        double total = carrito.getLineaCarrito()
                .stream()
                .mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad())
                .sum();
        carrito.setCostoTotal(total);

        // guarda y devuelve
        return modelMapper.map(carritoRepository.save(carrito), CarritoDTO.class);
    }

    @Override
    @Transactional
    public void clear(Integer id) {
        CarritoEntity carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNoEncontradoException(id));

        carrito.getLineaCarrito().clear();  // orphanRemoval elimina las líneas
        carrito.setCostoTotal(0.0);
        carritoRepository.save(carrito);
    }

    @Override
    public void remove(Integer id) {
        carritoRepository.findById(id)
                .orElseThrow(() -> new CarritoNoEncontradoException(id));
        carritoRepository.deleteById(id);
    }


    @Override
    @Transactional
    public CarritoDTO eliminarProducto(Integer carritoId, Integer productoId) {
        CarritoEntity carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new CarritoNoEncontradoException(carritoId));

        // busca la linea del producto
        LineaCarritoEntity linea = carrito.getLineaCarrito()
                .stream()
                .filter(l -> l.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException(productoId));

        // devuelve el stock al producto
        ProductoEntity producto = linea.getProducto();
        producto.setStock(producto.getStock() + linea.getCantidad());
        productoRepository.save(producto);

        // elimina la línea del carrito
        carrito.getLineaCarrito().remove(linea);

        // recalcula el total
        double total = carrito.getLineaCarrito()
                .stream()
                .mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad())
                .sum();
        carrito.setCostoTotal(total);

        return modelMapper.map(carritoRepository.save(carrito), CarritoDTO.class);
    }



    @Override
    @Transactional
    public CarritoDTO actualizarCantidad(Integer carritoId, Integer productoId, Integer cantidad) {
        CarritoEntity carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new CarritoNoEncontradoException(carritoId));

        // busca la línea
        LineaCarritoEntity linea = carrito.getLineaCarrito()
                .stream()
                .filter(l -> l.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException(productoId));

        ProductoEntity producto = linea.getProducto();
        int cantidadAnterior = linea.getCantidad();
        int diferencia = cantidad - cantidadAnterior;

        // verifica stock si aumenta
        if (diferencia > 0 && producto.getStock() < diferencia) {
            throw new StockInsuficienteException("Stock insuficiente para: " + producto.getNombre());
        }

        // ajusta el stock
        producto.setStock(producto.getStock() - diferencia);
        productoRepository.save(producto);

        // actualiza la cantidad
        linea.setCantidad(cantidad);

        // recalcula total
        double total = carrito.getLineaCarrito()
                .stream()
                .mapToDouble(l -> l.getPrecioUnitario() * l.getCantidad())
                .sum();
        carrito.setCostoTotal(total);

        return modelMapper.map(carritoRepository.save(carrito), CarritoDTO.class);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<CarritoDTO> getByUsuarioId(Integer usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .map(e -> modelMapper.map(e, CarritoDTO.class));
    }


}