package com.techlab.ecommerce.repositories;

import com.techlab.ecommerce.entities.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    // los metodos CRUD vienen solos de JpaRepository

}

