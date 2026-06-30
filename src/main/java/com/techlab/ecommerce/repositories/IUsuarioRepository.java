package com.techlab.ecommerce.repositories;

import com.techlab.ecommerce.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    // busca usuario por email
    Optional<UsuarioEntity> findByEmail(String email);


}

