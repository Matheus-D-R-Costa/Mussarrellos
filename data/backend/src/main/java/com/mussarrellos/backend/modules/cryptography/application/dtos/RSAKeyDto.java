package com.mussarrellos.backend.modules.cryptography.application.dtos;

import java.math.BigInteger;

public record RSAKeyDto(BigInteger modules,
                        String inst,
                        BigInteger exponent) {

}
