package com.mussarrellos.backend.modules.cryptography.domain.factory;

import com.mussarrellos.backend.modules.cryptography.domain.exceptions.CryptoOperationException;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Factory para criação de geradores de pares de chaves com diversos algoritmos e tamanhos.
 * Aplica o padrão Factory Method para isolar a criação de objetos complexos.
 */
@Slf4j
public final class KeyPairGeneratorFactory {

    // Valores padrão para geração de chaves RSA
    private static final int DEFAULT_RSA_KEY_SIZE = 2048;
    private static final String RSA_ALGORITHM = "RSA";

    // Algoritmos suportados
    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_DSA = "DSA";
    public static final String ALGORITHM_EC = "EC";

    // Construtor privado para evitar instanciação
    private KeyPairGeneratorFactory() {
        throw new IllegalStateException("Classe utilitária não deve ser instanciada");
    }

    /**
     * Cria um gerador de pares de chaves RSA com o tamanho padrão (2048 bits).
     *
     * @return O gerador de pares de chaves configurado
     * @throws CryptoOperationException Se o algoritmo não for suportado pela JVM
     */
    public static KeyPairGenerator createKeyPairGenerator() {
        return createRSAKeyPairGenerator(DEFAULT_RSA_KEY_SIZE);
    }

    /**
     * Cria um gerador de pares de chaves RSA com o tamanho especificado.
     *
     * @param keySize Tamanho da chave em bits
     * @return O gerador de pares de chaves configurado
     * @throws CryptoOperationException Se o algoritmo não for suportado ou o tamanho for inválido
     */
    public static KeyPairGenerator createRSAKeyPairGenerator(int keySize) {
        validateKeySize(keySize);
        return createKeyPairGenerator(RSA_ALGORITHM, keySize);
    }

    /**
     * Cria um gerador de pares de chaves com o algoritmo e tamanho especificados.
     *
     * @param algorithm Algoritmo para geração das chaves
     * @param keySize Tamanho da chave em bits
     * @return O gerador de pares de chaves configurado
     * @throws CryptoOperationException Se o algoritmo não for suportado ou o tamanho for inválido
     */
    public static KeyPairGenerator createKeyPairGenerator(String algorithm, int keySize) {
        validateKeySize(keySize);
        
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
            
            // Usar SecureRandom para inicializar o gerador
            SecureRandom random = new SecureRandom();
            keyGen.initialize(keySize, random);
            
            log.debug("Criado gerador de pares de chaves {} com tamanho {} bits", algorithm, keySize);
            return keyGen;
        } catch (NoSuchAlgorithmException e) {
            log.error("Algoritmo {} não suportado", algorithm, e);
            throw new CryptoOperationException("Algoritmo não suportado: " + algorithm, e);
        } catch (IllegalArgumentException e) {
            log.error("Parâmetros inválidos para gerador de chaves {}: {}", algorithm, e.getMessage());
            throw new CryptoOperationException("Parâmetros inválidos para gerador de chaves: " + e.getMessage(), e);
        }
    }

    /**
     * Valida se o tamanho da chave é válido.
     *
     * @param keySize Tamanho da chave em bits
     * @throws IllegalArgumentException Se o tamanho for inválido
     */
    private static void validateKeySize(int keySize) {
        if (keySize < 1024) {
            throw new IllegalArgumentException("Tamanho da chave deve ser pelo menos 1024 bits para segurança mínima");
        }
        
        if (keySize % 8 != 0) {
            throw new IllegalArgumentException("Tamanho da chave deve ser múltiplo de 8 bits");
        }
    }
}
