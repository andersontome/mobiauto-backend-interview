package com.api.revenda.mobiauto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.api.revenda.mobiauto.model.Loja;
import com.api.revenda.mobiauto.model.Role;
import com.api.revenda.mobiauto.model.Usuario;
import com.api.revenda.mobiauto.repository.LojaRepository;
import com.api.revenda.mobiauto.repository.RoleRepository;
import com.api.revenda.mobiauto.repository.UsuarioRepository;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LojaRepository lojaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner init() {
        return args -> {
            Role adminRole = roleRepository.findByNome("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setNome("ADMIN");
                        return roleRepository.save(newRole);
                    });

            Role proprietarioRole = roleRepository.findByNome("PROPRIETARIO")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setNome("PROPRIETARIO");
                        return roleRepository.save(newRole);
                    });

            Role gerenteRole = roleRepository.findByNome("GERENTE")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setNome("GERENTE");
                        return roleRepository.save(newRole);
                    });

            if (!usuarioRepository.findByEmail("admin@example.com").isPresent()) {
                Loja loja = new Loja();
                loja.setNome("Loja Teste");

                loja = lojaRepository.save(loja);

                Usuario admin = new Usuario();
                admin.setEmail("admin@example.com");
                admin.setNome("Admin");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setLoja(loja);

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);

                usuarioRepository.save(admin);
            }
        };
    }
}