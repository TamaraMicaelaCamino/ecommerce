package com.techlab.ecommerce.controllers.api;

import com.techlab.ecommerce.dtos.ProductoDTO;
import com.techlab.ecommerce.services.IProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoRestController {

    private final IProductoService productoService;

    // GET /productos/lista
    @GetMapping("/lista")
    public ResponseEntity<List<ProductoDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productoService.getAll());
    }

    // GET /productos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable Integer id) {
        return productoService.getById(id)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /productos
    @PostMapping
    public ResponseEntity<ProductoDTO> create(@Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productoService.create(dto));
    }

    // PUT /productos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productoService.update(id, dto));
    }

    // PATCH /productos/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<ProductoDTO> patch(@PathVariable Integer id, @RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productoService.patch(id, dto));
    }

    // DELETE /productos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        productoService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // GET /productos/nombre/{nombre}
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ProductoDTO> getByNombre(@PathVariable String nombre) {
        return productoService.getByName(nombre)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // GET /productos/categoria/id/{categoriaId}
    @GetMapping("/categoria/id/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getByCategoriaId(@PathVariable Integer categoriaId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productoService.getByCategoriaId(categoriaId));
    }

    // GET /productos/categoria/nombre/{categoriaNombre}
    @GetMapping("/categoria/nombre/{categoriaNombre}")
    public ResponseEntity<List<ProductoDTO>> getByCategoriaNombre(@PathVariable String categoriaNombre) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(productoService.getByCategoryName(categoriaNombre));
    }
}