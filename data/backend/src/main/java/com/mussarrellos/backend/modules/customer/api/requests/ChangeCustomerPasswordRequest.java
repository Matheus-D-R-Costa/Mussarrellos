package com.mussarrellos.backend.modules.customer.api.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeClientPasswordRequest {
    
    @NotBlank(message = "A senha atual é obrigatória")
    private String currentPassword;
    
    @NotBlank(message = "A nova senha é obrigatória")
    private String newPassword;
} 