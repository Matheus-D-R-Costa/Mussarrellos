package com.mussarrellos.backend.modules.customer.domain.entities;

import com.mussarrellos.backend.buildingblocks.domain.entities.Entity;
import com.mussarrellos.backend.modules.customer.domain.entities.types.CustomerId;
import com.mussarrellos.backend.modules.customer.domain.rules.CustomerRulesFactory;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Customer extends Entity<CustomerId> {

    private final CustomerId id;
    private String email;
    private String hashedPassword;
    private final LocalDateTime registrationDate;
    private LocalDateTime emailUpdatedDate;
    private LocalDateTime passwordUpdatedDate;
    
    private final PasswordEncoder passwordEncoder;

    private Customer(CustomerId id, String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        
        this.checkRule(CustomerRulesFactory.emailMustBeUnique(email, checker));
        this.checkRule(CustomerRulesFactory.emailMustBeValid(email));
        this.checkRule(CustomerRulesFactory.passwordMustBeStrong(password));

        this.id = id;
        this.email = email;
        this.hashedPassword = hashPassword(password);
        this.registrationDate = LocalDateTime.now();
        this.emailUpdatedDate = LocalDateTime.now();
        this.passwordUpdatedDate = LocalDateTime.now();

//        this.addDomainEvent(new ClientCreatedDomainEvent(id.getValue(), email)); TODO: Refatorar todas os DomainEventHandlers para usar a interface do buildingblocks...
    }

    @Override
    public CustomerId getId() {
        return id;
    }

    public static Customer create(String email, String password, EmailUniquenessChecker checker, PasswordEncoder passwordEncoder) {
        CustomerId id = new CustomerId(UUID.randomUUID());
        return new Customer(id, email, password, checker, passwordEncoder);
    }

    public void changeEmail(String newEmail, EmailUniquenessChecker checker) {
        String oldEmail = this.email;
        
        this.checkRule(CustomerRulesFactory.emailMustBeValid(newEmail));
        this.checkRule(CustomerRulesFactory.emailMustBeUnique(newEmail, checker));

        this.email = newEmail;
        this.emailUpdatedDate = LocalDateTime.now();

//        this.addDomainEvent(ClientEmailChangedEvent.create(
//                id.getValue(), oldEmail, newEmail
//        )); TODO: Refatorar todas os DomainEventHandlers para usar a interface do buildingblocks...
//    }
    }

    public void changePassword(String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, hashedPassword)) {
            throw new IllegalArgumentException("A senha atual está incorreta."); // TODO: lançar exception personalizada... (elegante)
        }

        this.checkRule(CustomerRulesFactory.passwordMustBeStrong(newPassword));
        this.checkRule(CustomerRulesFactory.passwordMustNotBeSame(newPassword,
                (password) -> passwordEncoder.matches(password, hashedPassword)));

        this.hashedPassword = hashPassword(newPassword);
        this.passwordUpdatedDate = LocalDateTime.now();
//        this.addDomainEvent(ClientPasswordChangedEvent.create(id.getValue())); TODO: Refatorar todas os DomainEventHandlers para usar a interface do buildingblocks...
//    }
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @FunctionalInterface
    public interface EmailUniquenessChecker {
        boolean isEmailUnique(String email);
    }
}
