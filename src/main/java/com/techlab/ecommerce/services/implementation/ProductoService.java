package com.techlab.ecommerce.services.implementation;

import com.techlab.ecommerce.entities.CategoriaEntity;
import com.techlab.ecommerce.exceptions.CategoriaNoEncontradoException;
import com.techlab.ecommerce.exceptions.ProductoNoEncontradoException;
import com.techlab.ecommerce.services.IProductoService;
import com.techlab.ecommerce.repositories.ICategoriaRepository;
import com.techlab.ecommerce.dtos.ProductoDTO;
import com.techlab.ecommerce.entities.ProductoEntity;
import com.techlab.ecommerce.repositories.IProductoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor  // genera el constructor automaticamente
@Service("productoService")

public class ProductoService implements IProductoService {

    private final IProductoRepository productoRepository;
    private final ModelMapper modelMapper;  //convertir
    private final ICategoriaRepository categoriaRepository;

    @Transactional
    @Override
    public List<ProductoDTO> getAll() {
        return productoRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, ProductoDTO.class))
                .toList();
    }
    @Transactional
    @Override
    public Optional<ProductoDTO> getById(Integer id) {
        return productoRepository.findById(id)    // busca en la BD por id
                .map(e -> modelMapper.map(e, ProductoDTO.class));  // si existe, convierte a DTO
    }
    @Transactional
    @Override
    public ProductoDTO create(ProductoDTO dto) {
        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradoException(dto.getCategoriaId()));

        ProductoEntity entity = modelMapper.map(dto, ProductoEntity.class);
        entity.setCategoria(categoria);
        return modelMapper.map(productoRepository.save(entity), ProductoDTO.class);
    }
    @Transactional
    @Override
    public ProductoDTO update(Integer id, ProductoDTO dto) {
        ProductoEntity entity = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));

        CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new CategoriaNoEncontradoException(dto.getCategoriaId()));

        modelMapper.map(dto, entity);
        entity.setId(id);
        entity.setCategoria(categoria);
        return modelMapper.map(productoRepository.save(entity), ProductoDTO.class);
    }

    @Transactional
    @Override
    public ProductoDTO patch(Integer id, ProductoDTO dto) {
        ProductoEntity entity = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));

        if (dto.getNombre() != null) entity.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) entity.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) entity.setPrecio(dto.getPrecio());
        if (dto.getStock() != null) entity.setStock(dto.getStock());
        if (dto.getImagenUrl() != null) entity.setImagenUrl(dto.getImagenUrl());

        if (dto.getCategoriaId() != null) {
            CategoriaEntity categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new CategoriaNoEncontradoException(dto.getCategoriaId()));
            entity.setCategoria(categoria);
        }

        return modelMapper.map(productoRepository.save(entity), ProductoDTO.class);
    }
    @Transactional
    @Override
    public void remove(Integer id) {
        productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
        productoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductoDTO> getByCategoriaId(Integer categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(entity -> modelMapper.map(entity, ProductoDTO.class))
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public List<ProductoDTO> getByCategoryName(String nombre) {
        return productoRepository.findByCategoriaNombreCategoria(nombre)
                .stream()
                .map(entity -> modelMapper.map(entity, ProductoDTO.class))
                .toList();
    }

    @Transactional
    @Override
    public Optional<ProductoDTO> getByName(String nombre) {
        return productoRepository.findByNombre(nombre)
                .map(entity -> modelMapper.map(entity, ProductoDTO.class));
    }

}

