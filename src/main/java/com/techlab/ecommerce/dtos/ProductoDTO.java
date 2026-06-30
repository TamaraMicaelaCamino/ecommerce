package com.techlab.ecommerce.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 30)
    private String nombre;

    @NotBlank(message = "La descripcion del producto es obligatorio")
    @Size(max = 100)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private CategoriaDTO categoria;

    @URL(message = "Debe ingresar una URL válida")
    private String imagenUrl;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;
}