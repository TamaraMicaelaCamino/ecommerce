package com.techlab.ecommerce.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaCarritoDTO {

    private Integer id;

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El ID del producto debe ser mayor que 0")
    private Integer productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;

    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    private Double precioUnitario;

    private ProductoDTO producto;


}