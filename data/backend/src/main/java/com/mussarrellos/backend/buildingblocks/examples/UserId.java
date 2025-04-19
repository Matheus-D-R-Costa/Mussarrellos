package com.mussarrellos.backend.buildingblocks.examples;

import com.mussarrellos.backend.buildingblocks.TypedIdValueBase;

import java.util.UUID;

/**
 * Exemplo de ID tipado para a entidade User.
 * Proporciona type safety e evita confusão com outros tipos de ID.
 */
public class UserId extends TypedIdValueBase {
    /**
     * Cria um novo ID de usuário com um UUID gerado aleatoriamente.
     */
    public UserId() {
        super(UUID.randomUUID());
    }

    /**
     * Cria um ID de usuário com um UUID específico.
     * @param id O UUID a ser usado como ID
     */
    public UserId(UUID id) {
        super(id);
    }

    /**
     * Cria um ID de usuário a partir de uma string representando um UUID.
     * @param id String representando um UUID válido
     */
    public UserId(String id) {
        super(UUID.fromString(id));
    }
    
    /**
     * Cria um novo ID de usuário com um UUID gerado aleatoriamente.
     * @return Um novo ID de usuário
     */
    public static UserId create() {
        return new UserId(UUID.randomUUID());
    }
} 