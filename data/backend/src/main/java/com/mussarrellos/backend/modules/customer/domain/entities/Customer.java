package com.mussarrellos.backend.modules.customer.domain.entities;

import com.mussarrellos.backend.buildingblocks.domain.entities.Entity;
import com.mussarrellos.backend.modules.customer.domain.entities.types.ClientId;
import com.mussarrellos.backend.modules.customer.domain.events.ClientCreatedDomainEvent;
import com.mussarrellos.backend.modules.customer.domain.events.ClientEmailChangedEvent;
import com.mussarrellos.backend.modules.customer.domain.events.ClientPasswordChangedEvent;
import com.mussarrellos.backend.modules.customer.domain.rules.ClientRulesFactory;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Client extends Entity<ClientId> {

    private final ClientId id;
    private String email;
    private String hashedPassword;
    private final LocalDateTime registrationDate;
    private LocalDateTime emailUpdatedDate;
    private LocalDateTime passwordUpdatedDate;
    
    private final PasswordEncoder passwordEncoder;

    private Client(ClientId id, String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        
        this.checkRule(ClientRulesFactory.emailMustBeUnique(email, checker));
        this.checkRule(ClientRulesFactory.emailMustBeValid(email));
        this.checkRule(ClientRulesFactory.passwordMustBeStrong(password));

        this.id = id;
        this.email = email;
        this.hashedPassword = hashPassword(password);
        this.registrationDate = LocalDateTime.now();
        this.emailUpdatedDate = LocalDateTime.now();
        this.passwordUpdatedDate = LocalDateTime.now();

        this.addDomainEvent(new ClientCreatedDomainEvent(id.getValue(), email));
    }

    @Override
    public ClientId getId() {
        return id;
    }

    public static Client create(String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        ClientId id = new ClientId(UUID.randomUUID());
        return new Client(id, email, password, checker, passwordEncoder);
    }

    public void changeEmail(String newEmail, EmailUniquenessChecker checker) {
        String oldEmail = this.email;
        
        this.checkRule(ClientRulesFactory.emailMustBeValid(newEmail));
        this.checkRule(ClientRulesFactory.emailMustBeUnique(newEmail, checker));

        this.email = newEmail;
        this.emailUpdatedDate = LocalDateTime.now();

        this.addDomainEvent(ClientEmailChangedEvent.create(
                id.getValue(), oldEmail, newEmail
        ));
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, hashedPassword)) {
            throw new IllegalArgumentException("A senha atual está incorreta."); // TODO: lançar exception personalizada... (elegante)
        }

        this.checkRule(ClientRulesFactory.passwordMustBeStrong(newPassword));
        this.checkRule(ClientRulesFactory.passwordMustNotBeSame(newPassword, 
                (password) -> passwordEncoder.matches(password, hashedPassword)));

        this.hashedPassword = hashPassword(newPassword);
        this.passwordUpdatedDate = LocalDateTime.now();
        this.addDomainEvent(ClientPasswordChangedEvent.create(id.getValue()));
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @FunctionalInterface
    public interface EmailUniquenessChecker {
        boolean isEmailUnique(String email);
    }
}
