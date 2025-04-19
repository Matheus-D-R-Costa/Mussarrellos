package com.mussarrellos.backend.modules.user.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request para registro de um novo cliente.
 */
@Data
public class RegisterClientRequest {
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email deve ser válido")
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    private String password;
} 