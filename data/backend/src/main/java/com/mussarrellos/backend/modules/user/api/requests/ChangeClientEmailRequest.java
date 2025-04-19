package com.mussarrellos.backend.modules.user.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request para alteração de email de um cliente.
 */
public record ChangeClientEmailRequest(@NotBlank(message = "O novo email é obrigatório")
                                       @Email(message = "O novo email deve ser válido")
                                       String newEmail) {
    

} 