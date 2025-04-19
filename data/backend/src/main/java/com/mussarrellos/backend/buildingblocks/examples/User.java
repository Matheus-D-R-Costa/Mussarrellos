package com.mussarrellos.backend.buildingblocks.examples;

import com.mussarrellos.backend.buildingblocks.Entity;
import com.mussarrellos.backend.modules.user.domain.events.ClientCreatedDomainEvent;

/**
 * Exemplo de entidade que usa um ID tipado.
 * Demonstra como usar TypedIdValueBase em uma entidade real.
 */
public class User extends Entity {
    private final UserId id;
    private String username;
    private String email;
    
    private User(UserId id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
    
    /**
     * Cria um novo usuário.
     * @param username Nome de usuário
     * @param email Email do usuário
     * @return Uma nova instância de User
     */
    public static User create(String username, String email) {
        UserId id = UserId.create();
        User user = new User(id, username, email);
        
        // Registra o evento de criação de usuário
        user.addDomainEvent(new ClientCreatedDomainEvent(id.getValue(), username));
        
        return user;
    }
    
    public UserId getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    /**
     * Altera o email do usuário.
     * @param newEmail Novo email
     */
    public void changeEmail(String newEmail) {
        // Validações poderiam ser adicionadas aqui
        this.email = newEmail;
        
        // Poderia adicionar um evento de domínio aqui
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
} 