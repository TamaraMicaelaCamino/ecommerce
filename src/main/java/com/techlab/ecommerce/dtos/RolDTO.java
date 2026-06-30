package com.techlab.ecommerce.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;


    @Size(max = 30, message = "El nombre del rol no puede superar los 30 caracteres")
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombre;


}

