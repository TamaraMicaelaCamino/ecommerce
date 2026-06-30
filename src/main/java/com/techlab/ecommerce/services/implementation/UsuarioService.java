package com.techlab.ecommerce.services.implementation;

import com.techlab.ecommerce.dtos.RolDTO;
import com.techlab.ecommerce.dtos.UsuarioDTO;
import com.techlab.ecommerce.entities.NombreRol;
import com.techlab.ecommerce.entities.RolEntity;
import com.techlab.ecommerce.entities.UsuarioEntity;
import com.techlab.ecommerce.exceptions.UsuarioNoEncontradoException;
import com.techlab.ecommerce.repositories.IRolRepository;
import com.techlab.ecommerce.repositories.IUsuarioRepository;
import com.techlab.ecommerce.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service("usuarioService")
@RequiredArgsConstructor
public class UsuarioService implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public List<UsuarioDTO> getAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, UsuarioDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public Optional<UsuarioDTO> getById(Integer id) {
        return usuarioRepository.findById(id)
                .map(e -> modelMapper.map(e, UsuarioDTO.class));
    }

    @Override
    @Transactional
    public UsuarioDTO create(UsuarioDTO dto) {
        // verifica si el email ya existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        UsuarioEntity entity = modelMapper.map(dto, UsuarioEntity.class);
        entity.setActivo(true);

        // resuelve cada rol desde la BD por nombre
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<RolEntity> rolesGestionados = dto.getRoles().stream()
                    .map(rolDTO -> rolRepository.findByNombre(NombreRol.valueOf(rolDTO.getNombre()))
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolDTO.getNombre())))
                    .collect(Collectors.toSet()); // ← Set en lugar de toList()
            entity.setRoles(rolesGestionados);
        }

        return modelMapper.map(usuarioRepository.save(entity), UsuarioDTO.class);
    }


    @Override
    public void remove(Integer id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    @Override
    public Optional<UsuarioDTO> getByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(entity -> {
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setId(entity.getId());
                    dto.setNombre(entity.getNombre());
                    dto.setEmail(entity.getEmail());
                    dto.setPassword(entity.getPassword());
                    dto.setActivo(entity.getActivo());
                    dto.setFechaCreacion(entity.getFechaCreacion());
                    dto.setRoles(entity.getRoles().stream()
                            .map(rol -> {
                                RolDTO rolDTO = new RolDTO();
                                rolDTO.setId(rol.getId());
                                rolDTO.setNombre(rol.getNombre().name());
                                return rolDTO;
                            })
                            .collect(Collectors.toSet()));
                    return dto;
                });
    }


    @Override
    @Transactional(readOnly = true)
    public boolean login(String email, String password) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException(email));

        return usuario.getPassword().equals(password);
    }

}
