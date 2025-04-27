package com.mussarrellos.backend.modules.cryptography.domain.factory;

import lombok.extern.slf4j.Slf4j;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Slf4j
public final class KeyPairGeneratorFactory {

    private static final int DEFAULT_RSA_KEY_SIZE = 2048;
    private static final String RSA_ALGORITHM = "RSA";

    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_DSA = "DSA";
    public static final String ALGORITHM_EC = "EC";

    private KeyPairGeneratorFactory() {
        throw new IllegalStateException("Classe utilitária não deve ser instanciada");
    }

    public static KeyPairGenerator createKeyPairGenerator() {
        return createRSAKeyPairGenerator(DEFAULT_RSA_KEY_SIZE);
    }

    public static KeyPairGenerator createRSAKeyPairGenerator(int keySize) {
        validateKeySize(keySize);
        return createKeyPairGenerator(RSA_ALGORITHM, keySize);
    }


    public static KeyPairGenerator createKeyPairGenerator(String algorithm, int keySize) {
        validateKeySize(keySize);
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
            
            SecureRandom random = new SecureRandom();
            keyGen.initialize(keySize, random);
            
            log.debug("Criado gerador de pares de chaves {} com tamanho {} bits", algorithm, keySize);
            return keyGen;
        } catch (NoSuchAlgorithmException e) {
            log.error("Algoritmo {} não suportado", algorithm, e);
        } catch (IllegalArgumentException e) {
            log.error("Parâmetros inválidos para gerador de chaves {}: {}", algorithm, e.getMessage());
        }
        return null; // Terminar essa implementação um dia
    }

    private static void validateKeySize(int keySize) {
        if (keySize < 1024) {
            throw new IllegalArgumentException("Tamanho da chave deve ser pelo menos 1024 bits para segurança mínima");
        }
        
        if (keySize % 8 != 0) {
            throw new IllegalArgumentException("Tamanho da chave deve ser múltiplo de 8 bits");
        }
    }
}
