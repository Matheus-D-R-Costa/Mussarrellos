package com.mussarrellos.backend.modules.authentication.api.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GenerateRSAKeyRequest(@Min(value = 1, message = "O tempo de validade deve ser de pelo menos 1 minuto")
                                    @Max(value = 30, message = "O tempo de validade não pode exceder 30 minutos")
                                    int timeout
) { }
