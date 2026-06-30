package com.techlab.ecommerce.controllers.api;

import com.techlab.ecommerce.dtos.UsuarioDTO;
import com.techlab.ecommerce.services.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioRestController {

    private final IUsuarioService usuarioService;

    // GET /usuarios/lista
    @GetMapping("/lista")
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.getAll());
    }

    // GET /usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Integer id) {
        return usuarioService.getById(id)
                .map(dto -> ResponseEntity.status(HttpStatus.OK).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /usuarios
    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.create(dto));
    }

    // DELETE /usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Integer id) {
        usuarioService.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



    @GetMapping("/email/{email:.+}")
    public ResponseEntity<UsuarioDTO> getByEmail(@PathVariable String email) {
        return usuarioService.getByEmail(email)
                .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestParam String email, @RequestParam String password) {
        boolean valido = usuarioService.login(email, password);
        return ResponseEntity.ok(valido);
    }

}
