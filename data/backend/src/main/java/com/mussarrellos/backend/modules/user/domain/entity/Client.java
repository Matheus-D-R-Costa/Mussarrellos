package com.mussarrellos.backend.modules.user.domain.entity;

import com.mussarrellos.backend.buildingblocks.Entity;
import com.mussarrellos.backend.modules.user.domain.entity.types.ClientId;
import com.mussarrellos.backend.modules.user.domain.events.ClientCreatedDomainEvent;
import com.mussarrellos.backend.modules.user.domain.rules.EmailMustBeUniqueRule;
import com.mussarrellos.backend.modules.user.domain.rules.EmailMustBeValidRule;
import com.mussarrellos.backend.modules.user.domain.rules.PasswordMustBeStrongRule;
import com.mussarrellos.backend.modules.user.domain.rules.PasswordMustNotBeSameRule;
import lombok.Getter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidade de domínio que representa um cliente.
 * Contém informações básicas sobre um cliente no sistema.
 */
@Getter
public class Client extends Entity {

    private final ClientId id;
    private String email;
    private String hashedPassword;
    private final LocalDateTime registrationDate;
    private LocalDateTime emailUpdatedDate;
    private LocalDateTime passwordUpdatedDate;

    private static final PasswordEncoder ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private Client(ClientId id, String email, String password, EmailUniquenessChecker checker) {
        // Validações usando regras de negócio
        this.checkRule(new EmailMustBeUniqueRule(email, checker));
        this.checkRule(new EmailMustBeValidRule(email));
        this.checkRule(new PasswordMustBeStrongRule(password));

        // Inicialização dos campos
        this.id = id;
        this.email = email;
        this.hashedPassword = hashPassword(password);
        this.registrationDate = LocalDateTime.now();
        this.emailUpdatedDate = LocalDateTime.now();
        this.passwordUpdatedDate = LocalDateTime.now();

        // Registro do evento de domínio
        this.addDomainEvent(new ClientCreatedDomainEvent(id.getValue(), email));
    }

    /**
     * Cria um novo cliente.
     *
     * @param email    Endereço de email do cliente
     * @param password Senha do cliente
     * @param checker  Verificador de unicidade de email
     * @return Uma nova instância de Client
     */
    public static Client create(String email, String password, EmailUniquenessChecker checker) {
        ClientId id = new ClientId(UUID.randomUUID());
        return new Client(id, email, password, checker);
    }

    /**
     * Altera o email do cliente.
     *
     * @param newEmail Novo endereço de email
     * @param checker  Verificador de unicidade de email
     */
    public void changeEmail(String newEmail, EmailUniquenessChecker checker) {
        // Validações usando regras de negócio
        this.checkRule(new EmailMustBeValidRule(newEmail));
        this.checkRule(new EmailMustBeUniqueRule(newEmail, checker));

        this.email = newEmail;
        this.emailUpdatedDate = LocalDateTime.now();

        // Aqui poderíamos adicionar um evento de domínio para a mudança de email
        // this.addDomainEvent(new ClientEmailChangedDomainEvent(id.getValue(), newEmail));
    }

    /**
     * Altera a senha do cliente.
     *
     * @param currentPassword Senha atual
     * @param newPassword     Nova senha
     */
    public void changePassword(String currentPassword, String newPassword) {
        // Verifica se a senha atual está correta
        if (!ENCODER.matches(currentPassword, hashedPassword)) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        // Validações usando regras de negócio
        this.checkRule(new PasswordMustBeStrongRule(newPassword));
        this.checkRule(new PasswordMustNotBeSameRule(newPassword,
                (password) -> ENCODER.matches(password, hashedPassword)));

        this.hashedPassword = hashPassword(newPassword);
        this.passwordUpdatedDate = LocalDateTime.now();

        // Aqui poderíamos adicionar um evento de domínio para a mudança de senha
        // this.addDomainEvent(new ClientPasswordChangedDomainEvent(id.getValue()));
    }

    /**
     * Criptografa a senha usando o encoder configurado.
     *
     * @param password Senha em texto plano
     * @return Senha criptografada
     */
    private String hashPassword(String password) {
        return ENCODER.encode(password);
    }

    /**
     * Interface funcional para verificar a unicidade de um email.
     */
    @FunctionalInterface
    public interface EmailUniquenessChecker {
        boolean isEmailUnique(String email);
    }
}
