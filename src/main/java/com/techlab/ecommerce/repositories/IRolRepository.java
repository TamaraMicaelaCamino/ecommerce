package com.techlab.ecommerce.repositories;

import com.techlab.ecommerce.entities.NombreRol;
import com.techlab.ecommerce.entities.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<RolEntity, Integer> {


    Optional<RolEntity> findByNombre(NombreRol nombre);

}

