package com.techlab.ecommerce.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombreCategoria;

    @Size(max = 100, message = "La descripcion no puede superar los 100 caracteres")
    @NotBlank(message = "La descripcion de la categoria es obligatoria")
    private String descripcionCategoria;
}

