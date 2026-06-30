package com.techlab.ecommerce.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class CarritoDTO {


    private Integer id;

    @Positive(message = "El ID del usuario debe ser mayor que 0")
    private Integer usuario_id;

    @PositiveOrZero(message = "El costo total no puede ser negativo")
    private Double costoTotal;

    private List<LineaCarritoDTO> lineaCarrito;



}