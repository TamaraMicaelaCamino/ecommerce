package com.techlab.ecommerce.configuration;
import com.techlab.ecommerce.dtos.CarritoDTO;
import com.techlab.ecommerce.dtos.ProductoDTO;
import com.techlab.ecommerce.dtos.RolDTO;
import com.techlab.ecommerce.entities.CarritoEntity;
import com.techlab.ecommerce.entities.ProductoEntity;
import com.techlab.ecommerce.entities.RolEntity;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setPreferNestedProperties(false)
                .setPropertyCondition(context -> true);

        // ProductoEntity → ProductoDTO
        modelMapper.typeMap(ProductoEntity.class, ProductoDTO.class)
                .addMappings(mapper ->
                        mapper.map(
                                src -> src.getCategoria().getId(),
                                ProductoDTO::setCategoriaId
                        )
                );

        modelMapper.createTypeMap(RolEntity.class, RolDTO.class)
                .setConverter(context -> {
                    RolEntity source = context.getSource();
                    RolDTO dto = new RolDTO();
                    dto.setId(source.getId());
                    dto.setNombre(source.getNombre().name()); // enum → String
                    return dto;
                });

        // CarritoEntity → CarritoDTO
        modelMapper.typeMap(CarritoEntity.class, CarritoDTO.class)
                .addMappings(mapper ->
                        mapper.map(
                                src -> src.getUsuario().getId(),
                                CarritoDTO::setUsuario_id
                        )
                );



        return modelMapper;
    }
}