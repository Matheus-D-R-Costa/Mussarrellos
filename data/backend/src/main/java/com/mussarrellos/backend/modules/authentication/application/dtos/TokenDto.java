package com.mussarrellos.backend.modules.authentication.application.dtos;

import java.util.UUID;

public record TokenDto(UUID id,
                       String email,
                       Long expiresIn) {
}
