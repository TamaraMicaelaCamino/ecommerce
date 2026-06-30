package com.techlab.ecommerce.controllers.api;

import com.techlab.ecommerce.dtos.CarritoDTO;
import com.techlab.ecommerce.services.ICarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carritos")
@RequiredArgsConstructor
public class CarritoRestController {

    private final ICarritoService carritoService;

    // GET /carritos/lista
    @GetMapping("/lista")
    public ResponseEntity<List<CarritoDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carritoService.getAll());
    }

    // GET /carritos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CarritoDTO> getById(@PathVariable Integer id) {
        return carritoService.getById(id)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//    // POST /carritos
//    @PostMapping
//    public ResponseEntity<CarritoDTO> create() {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(carritoService.create());
//    }

    // POST /carritos/{carritoId}/productos/{productoId}
    @PostMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> agregarProducto(@PathVariable Integer carritoId, @PathVariable Integer productoId, @RequestParam Integer cantidad) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carritoService.addProduct(carritoId, productoId, cantidad));
    }

    // DELETE /carritos/{id}/vaciar
    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Void> vaciar(@PathVariable Integer id) {
        carritoService.clear(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // DELETE /carritos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        carritoService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // DELETE /carritos/{carritoId}/productos/{productoId}
    @DeleteMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarProducto(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carritoService.eliminarProducto(carritoId, productoId));
    }


    // PATCH /carritos/{carritoId}/productos/{productoId}
    @PatchMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(@PathVariable Integer carritoId, @PathVariable Integer productoId, @RequestParam Integer cantidad) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(carritoService.actualizarCantidad(carritoId, productoId, cantidad));
    }

    @PostMapping
    public ResponseEntity<CarritoDTO> create(@RequestParam Integer usuarioId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.create(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoDTO> getByUsuarioId(@PathVariable Integer usuarioId) {
        return carritoService.getByUsuarioId(usuarioId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}