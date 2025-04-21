package com.mussarrellos.backend.modules.authentication.api.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Request para geração de chaves RSA.
 * Define o tempo de validade durante o qual a chave privada 
 * ficará armazenada no servidor.
 */
@Schema(description = "Requisição para gerar um par de chaves RSA")
public record GenerateRSAKeyRequest(
        @Schema(
                description = "Tempo de validade em minutos para a chave privada no servidor",
                minimum = "1",
                maximum = "30",
                defaultValue = "5",
                example = "10"
        )
        @Min(value = 1, message = "O tempo de validade deve ser de pelo menos 1 minuto")
        @Max(value = 30, message = "O tempo de validade não pode exceder 30 minutos")
        int timeout
) {}
