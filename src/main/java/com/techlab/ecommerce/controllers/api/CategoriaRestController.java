package com.techlab.ecommerce.controllers.api;

import com.techlab.ecommerce.dtos.CategoriaDTO;
import com.techlab.ecommerce.services.ICategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaRestController {

    private final ICategoriaService categoriaService;

    // GET /categorias/lista
    @GetMapping("/lista")
    public ResponseEntity<List<CategoriaDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.getAll());
    }

    // GET /categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable Integer id) {
        return categoriaService.getById(id)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /categorias
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoriaService.create(dto));
    }

    // PUT /categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.update(id,dto));
    }

    // PATCH /categorias/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaDTO> patch(@PathVariable Integer id, @RequestBody CategoriaDTO dto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.patch(id,dto));
    }

    // DELETE /categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        categoriaService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
