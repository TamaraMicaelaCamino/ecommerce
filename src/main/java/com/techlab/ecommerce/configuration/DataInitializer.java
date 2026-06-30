package com.techlab.ecommerce.configuration;

import com.techlab.ecommerce.entities.NombreRol;
import com.techlab.ecommerce.entities.RolEntity;
import com.techlab.ecommerce.repositories.IRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final IRolRepository rolRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (rolRepository.count() == 0) {
            RolEntity user = new RolEntity();
            user.setNombre(NombreRol.ROLE_USER);
            rolRepository.save(user);

            RolEntity admin = new RolEntity();
            admin.setNombre(NombreRol.ROLE_ADMIN);
            rolRepository.save(admin);

            System.out.println("Roles inicializados correctamente");
        }
    }
}