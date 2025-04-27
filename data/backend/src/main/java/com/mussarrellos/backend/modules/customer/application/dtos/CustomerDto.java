package com.mussarrellos.backend.modules.customer.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.time.LocalDateTime;
import java.util.UUID;

public record ClientDto(UUID id,
                        String email,
                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime registrationDate,

                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime emailUpdatedDate,

                        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
                        LocalDateTime passwordUpdatedDate) {
}