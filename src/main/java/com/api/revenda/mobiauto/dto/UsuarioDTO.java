package com.api.revenda.mobiauto.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Name is mandatory")
    private String nome;

    @NotBlank(message = "Password is mandatory")
    private String senha;

    private Set<RoleDTO> roles;
    
    private Long lojaId;

}
