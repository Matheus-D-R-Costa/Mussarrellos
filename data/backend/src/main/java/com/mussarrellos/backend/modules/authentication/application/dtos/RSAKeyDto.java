package com.mussarrellos.backend.modules.authentication.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigInteger;

/**
 * DTO que representa uma chave pública RSA retornada para o cliente.
 * Contém os valores necessários para uso da chave em operações criptográficas no frontend.
 */
@Schema(description = "Chave pública RSA retornada para o cliente")
public record RSAKeyDto(
        @Schema(description = "Módulo da chave RSA (n)", example = "19120913871092380912309...")
        @JsonProperty("modulus")
        BigInteger modulus,

        @Schema(description = "Algoritmo utilizado", example = "RSA")
        @JsonProperty("algorithm")
        String algorithm,

        @Schema(description = "Expoente público da chave RSA (e)", example = "65537")
        @JsonProperty("publicExponent")
        BigInteger publicExponent
) {}