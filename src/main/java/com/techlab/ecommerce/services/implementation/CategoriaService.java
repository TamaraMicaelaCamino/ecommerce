package com.techlab.ecommerce.services.implementation;

import com.techlab.ecommerce.dtos.CategoriaDTO;
import com.techlab.ecommerce.entities.CategoriaEntity;
import com.techlab.ecommerce.exceptions.CategoriaNoEncontradoException;
import com.techlab.ecommerce.repositories.ICategoriaRepository;
import com.techlab.ecommerce.services.ICategoriaService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("categoriaService")
@RequiredArgsConstructor
public class CategoriaService implements ICategoriaService {

    private final ICategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoriaDTO> getAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, CategoriaDTO.class))
                .toList();
    }

    @Override
    public Optional<CategoriaDTO> getById(Integer id) {
        return categoriaRepository.findById(id)
                .map(e -> modelMapper.map(e, CategoriaDTO.class));
    }

    @Override
    public CategoriaDTO create(CategoriaDTO dto) {
        CategoriaEntity entity = modelMapper.map(dto, CategoriaEntity.class);
        return modelMapper.map(categoriaRepository.save(entity), CategoriaDTO.class);
    }
    @Transactional
    @Override
    public CategoriaDTO update(Integer id, CategoriaDTO dto) {
        CategoriaEntity entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradoException(id));

        modelMapper.map(dto, entity);
        entity.setId(id);

        return modelMapper.map(categoriaRepository.save(entity), CategoriaDTO.class);
    }

    @Override
    public CategoriaDTO patch(Integer id, CategoriaDTO dto) {
        CategoriaEntity entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradoException(id));

        if (dto.getNombreCategoria() != null) entity.setNombreCategoria(dto.getNombreCategoria());
        if (dto.getDescripcionCategoria() != null) entity.setDescripcionCategoria(dto.getDescripcionCategoria());

        return modelMapper.map(categoriaRepository.save(entity), CategoriaDTO.class);
    }

    @Override
    @Transactional
    public void remove(Integer id) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNoEncontradoException(id));

        if (!categoria.getProductos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la categoria porque tiene productos asociados");
        }

        categoriaRepository.deleteById(id);
    }
}

