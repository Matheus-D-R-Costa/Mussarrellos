package com.mussarrellos.backend.modules.authentication.application.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record RSAKeyDto(@JsonProperty("modulus")
                        BigInteger modulus,

                        @JsonProperty("algorithm")
                        String algorithm,

                        @JsonProperty("publicExponent")
                        BigInteger publicExponent
) {}