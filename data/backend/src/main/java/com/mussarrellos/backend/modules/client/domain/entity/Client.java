package com.mussarrellos.backend.modules.client.domain.entity;

import com.mussarrellos.backend.buildingblocks.Entity;
import com.mussarrellos.backend.modules.client.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.client.domain.events.ClientCreatedDomainEvent;
import com.mussarrellos.backend.modules.client.domain.events.ClientEmailChangedEvent;
import com.mussarrellos.backend.modules.client.domain.events.ClientPasswordChangedEvent;
import com.mussarrellos.backend.modules.client.domain.rules.ClientRulesFactory;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade que representa um cliente no sistema.
 * Implementa as regras de negócio relacionadas a clientes e gerencia seus eventos.
 */
@Getter
public class Client extends Entity<ClientId> {

    private final ClientId id;
    private String email;
    private String hashedPassword;
    private final LocalDateTime registrationDate;
    private LocalDateTime emailUpdatedDate;
    private LocalDateTime passwordUpdatedDate;
    
    // Encoder injetado no construtor em vez de estático
    private final PasswordEncoder passwordEncoder;

    private Client(ClientId id, String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        
        // Usando a Factory para criar e verificar regras
        this.checkRule(ClientRulesFactory.emailMustBeUnique(email, checker));
        this.checkRule(ClientRulesFactory.emailMustBeValid(email));
        this.checkRule(ClientRulesFactory.passwordMustBeStrong(password));

        this.id = id;
        this.email = email;
        this.hashedPassword = hashPassword(password);
        this.registrationDate = LocalDateTime.now();
        this.emailUpdatedDate = LocalDateTime.now();
        this.passwordUpdatedDate = LocalDateTime.now();

        // Registro do evento de domínio usando factory method
        this.addDomainEvent(new ClientCreatedDomainEvent(id.getValue(), email));
    }

    /**
     * Retorna o ID da entidade, implementando o método abstrato da classe base.
     */
    @Override
    protected ClientId getId() {
        return id;
    }

    /**
     * Cria um novo cliente.
     *
     * @param email    Endereço de email do cliente
     * @param password Senha do cliente
     * @param checker  Verificador de unicidade de email
     * @param passwordEncoder Codificador de senha
     * @return Uma nova instância de Client
     */
    public static Client create(String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        ClientId id = new ClientId(UUID.randomUUID());
        return new Client(id, email, password, checker, passwordEncoder);
    }

    /**
     * Altera o email do cliente.
     *
     * @param newEmail Novo endereço de email
     * @param checker  Verificador de unicidade de email
     */
    public void changeEmail(String newEmail, EmailUniquenessChecker checker) {
        // Armazena o email antigo para o evento
        String oldEmail = this.email;
        
        // Validações usando regras de negócio com a Factory
        this.checkRule(ClientRulesFactory.emailMustBeValid(newEmail));
        this.checkRule(ClientRulesFactory.emailMustBeUnique(newEmail, checker));

        this.email = newEmail;
        this.emailUpdatedDate = LocalDateTime.now();

        // Adiciona o evento de alteração de email usando factory method
        this.addDomainEvent(ClientEmailChangedEvent.create(
                id.getValue(), oldEmail, newEmail
        ));
    }

    /**
     * Altera a senha do cliente.
     *
     * @param currentPassword Senha atual
     * @param newPassword     Nova senha
     */
    public void changePassword(String currentPassword, String newPassword) {
        // Verifica se a senha atual está correta
        if (!passwordEncoder.matches(currentPassword, hashedPassword)) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        // Validações usando regras de negócio com a Factory
        this.checkRule(ClientRulesFactory.passwordMustBeStrong(newPassword));
        this.checkRule(ClientRulesFactory.passwordMustNotBeSame(newPassword, 
                (password) -> passwordEncoder.matches(password, hashedPassword)));

        this.hashedPassword = hashPassword(newPassword);
        this.passwordUpdatedDate = LocalDateTime.now();

        // Adiciona o evento de alteração de senha usando o factory method
        this.addDomainEvent(ClientPasswordChangedEvent.create(id.getValue()));
    }

    /**
     * Criptografa a senha usando o encoder configurado.
     *
     * @param password Senha em texto plano
     * @return Senha criptografada
     */
    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Interface funcional para verificar a unicidade de um email.
     */
    @FunctionalInterface
    public interface EmailUniquenessChecker {
        boolean isEmailUnique(String email);
    }
}
