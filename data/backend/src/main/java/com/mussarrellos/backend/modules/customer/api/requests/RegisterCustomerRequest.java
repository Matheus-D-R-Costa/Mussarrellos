package com.mussarrellos.backend.modules.customer.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerRequest(@NotBlank(message = "O email é obrigatório")
                                    @Email(message = "O email deve ser válido")
                                    String email,

                                      @NotBlank(message = "A senha é obrigatória")
                                    String password) {

}