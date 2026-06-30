package com.techlab.ecommerce.repositories;

import com.techlab.ecommerce.entities.CarritoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICarritoRepository extends JpaRepository<CarritoEntity, Integer> {
    // los metodos CRUD vienen solos de JpaRepository

    Optional<CarritoEntity> findByUsuarioId(Integer usuarioId);
}