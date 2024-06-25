package com.api.revenda.mobiauto.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.revenda.mobiauto.dto.RoleDTO;
import com.api.revenda.mobiauto.dto.UsuarioDTO;
import com.api.revenda.mobiauto.exception.DuplicateEmailException;
import com.api.revenda.mobiauto.model.Role;
import com.api.revenda.mobiauto.model.Usuario;
import com.api.revenda.mobiauto.repository.RoleRepository;
import com.api.revenda.mobiauto.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        Optional<Usuario> existingUser = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateEmailException("Email already exists.");
        }

        Usuario currentUser = getCurrentUser();
        if (!isAdmin(currentUser) && !isAuthorizedToCreateUser(currentUser, usuarioDTO)) {
            throw new AccessDeniedException("User is not authorized to create new users.");
        }

        Set<Role> roles = new HashSet<>();
        for (RoleDTO roleDTO : usuarioDTO.getRoles()) {
            Role existingRole = roleRepository.findByNome(roleDTO.getNome()).orElse(null);
            if (existingRole != null) {
                roles.add(existingRole);
            } else {
                Role defaultRole = roleRepository.findByNome("PROPRIETARIO")
                        .orElseThrow(() -> new RuntimeException("Default role PROPRIETARIO not found"));
                roles.add(defaultRole);
            }
        }

        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario.setId(null);
        usuario.setRoles(roles);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return modelMapper.map(savedUsuario, UsuarioDTO.class);
    }


    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void excluir(Long id) {
        Usuario currentUser = getCurrentUser();
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!isAdmin(currentUser) && !usuario.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("User is not authorized to delete this user.");
        }
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        Usuario currentUser = getCurrentUser();
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent() && !isAdmin(currentUser) && !usuario.get().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("User is not authorized to access this user.");
        }
        return usuario;
    }

    public List<UsuarioDTO> listarTodos() {
        Usuario currentUser = getCurrentUser();

        if (isAdmin(currentUser)) {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return usuarios.stream()
                    .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                    .collect(Collectors.toList());
        } else {
            return usuarioRepository.findById(currentUser.getId())
                    .stream()
                    .map(usuario -> modelMapper.map(usuario, UsuarioDTO.class))
                    .collect(Collectors.toList());
        }
    }

    private Usuario getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdmin(Usuario user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getNome().equals("ADMIN"));
    }

    private boolean isAuthorizedToCreateUser(Usuario currentUser, UsuarioDTO newUser) {
        if (currentUser.getRoles().stream().anyMatch(role -> role.getNome().equals("PROPRIETARIO")) ||
            currentUser.getRoles().stream().anyMatch(role -> role.getNome().equals("GERENTE"))) {
            return newUser.getLojaId().equals(currentUser.getId());
        }
        return false;
    }
}