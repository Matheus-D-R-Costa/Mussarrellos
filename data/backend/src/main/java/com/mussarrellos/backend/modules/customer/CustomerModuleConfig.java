package com.mussarrellos.backend.modules.customer;

import com.mussarrellos.backend.buildingblocks.application.outbox.Outbox;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessageFactory;
import com.mussarrellos.backend.modules.customer.application.CustomerModule;
import com.mussarrellos.backend.modules.customer.application.commands.handlers.ChangeCustomerEmailCommandHandler;
import com.mussarrellos.backend.modules.customer.application.commands.handlers.ChangeCustomerPasswordCommandHandler;
import com.mussarrellos.backend.modules.customer.application.commands.handlers.RegisterCustomerCommandHandler;
import com.mussarrellos.backend.modules.customer.application.contracts.ICustomerModule;
import com.mussarrellos.backend.modules.customer.application.queries.handlers.GetCustomerByEmailQueryHandler;
import com.mussarrellos.backend.modules.customer.application.queries.handlers.GetCustomerByIdQueryHandler;
import com.mussarrellos.backend.modules.customer.domain.repository.CustomerRepository;
import com.mussarrellos.backend.modules.customer.infra.persistance.adapter.CustomerRepositoryAdapter;
import com.mussarrellos.backend.modules.customer.infra.persistance.mapper.CustomerModelMapper;
import com.mussarrellos.backend.modules.customer.infra.persistance.repository.CustomerModelRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CustomerModuleConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomerRepository clientRepository(CustomerModelRepository repository, Outbox outbox,
                                               OutboxMessageFactory messageFactory, CustomerModelMapper mapper) {

        return new CustomerRepositoryAdapter(repository, outbox, messageFactory, mapper);
    }

    @Bean
    ChangeCustomerEmailCommandHandler changeClientEmailCommandHandler(CustomerRepository customerRepository) {
        return new ChangeCustomerEmailCommandHandler(customerRepository);
    }

    @Bean
    ChangeCustomerPasswordCommandHandler changeClientPasswordCommandHandler(CustomerRepository customerRepository) {
        return new ChangeCustomerPasswordCommandHandler(customerRepository);
    }

    @Bean
    RegisterCustomerCommandHandler registerClientCommandHandler(CustomerRepository customerRepository) {
        return new RegisterCustomerCommandHandler(customerRepository);
    }

    @Bean
    GetCustomerByEmailQueryHandler getClientByEmailQueryHandler(CustomerRepository customerRepository) {
        return new GetCustomerByEmailQueryHandler(customerRepository);
    }

    @Bean
    GetCustomerByIdQueryHandler getClientByIdQueryHandler(CustomerRepository customerRepository) {
        return new GetCustomerByIdQueryHandler(customerRepository);
    }

    @Bean
    public ICustomerModule userModule(ApplicationContext applicationContext) {
        return new CustomerModule(applicationContext);
    }

} 