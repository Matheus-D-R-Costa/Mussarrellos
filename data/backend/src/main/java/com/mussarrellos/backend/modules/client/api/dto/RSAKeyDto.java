package com.mussarrellos.backend.modules.client.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para representar uma chave RSA pública.
 * Contém o módulo e o expoente da chave RSA, além da data de expiração.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RSAKeyDto {
    
    private String modulus;
    private String exponent;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime expiresAt;
    
    private String error;
    
    /**
     * Cria um DTO com uma mensagem de erro.
     *
     * @param errorMessage A mensagem de erro
     * @return O DTO preenchido apenas com a mensagem de erro
     */
    public static RSAKeyDto createError(String errorMessage) {
        return RSAKeyDto.builder()
                .error(errorMessage)
                .build();
    }
} 