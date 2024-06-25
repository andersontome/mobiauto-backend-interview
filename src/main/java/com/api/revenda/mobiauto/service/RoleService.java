package com.api.revenda.mobiauto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.revenda.mobiauto.model.Role;
import com.api.revenda.mobiauto.repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findRoleByName(String nome) {
        return roleRepository.findByNome(nome);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
