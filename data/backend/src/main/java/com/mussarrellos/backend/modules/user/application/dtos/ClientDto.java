package com.mussarrellos.backend.modules.user.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) que representa um cliente.
 * Usado para transferir dados entre a camada de aplicação e a interface.
 */
public record ClientDto(UUID id,
                        String email,
                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime registrationDate,

                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime emailUpdatedDate,

                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime passwordUpdatedDate) {
}