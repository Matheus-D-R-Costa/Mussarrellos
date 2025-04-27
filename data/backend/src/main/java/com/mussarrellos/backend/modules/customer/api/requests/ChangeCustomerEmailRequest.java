package com.mussarrellos.backend.modules.customer.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeCustomerEmailRequest(@NotBlank(message = "O novo email é obrigatório")
                                       @Email(message = "O novo email deve ser válido")
                                       String newEmail) {
    

} 