package com.mussarrellos.backend.modules.authentication.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticateClientRequest(@NotBlank(message = "O novo email é obrigatório")
                                    @Email(message = "O novo email deve ser válido")
                                    String email,

                                        @NotBlank
                                    String password, // Senha do usuário criptografada no cliente

                                        @NotBlank
                                    String inst,

                                        @NotBlank
                                    String fingerprint,
                                        @NotBlank

                                    @NotBlank
                                    String deviceId) {
}
