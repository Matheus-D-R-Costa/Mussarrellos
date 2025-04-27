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
import com.mussarrellos.backend.modules.customer.domain.repository.ClientRepository;
import com.mussarrellos.backend.modules.customer.infra.persistance.adapter.ClientRepositoryAdapter;
import com.mussarrellos.backend.modules.customer.infra.persistance.mapper.ClientModelMapper;
import com.mussarrellos.backend.modules.customer.infra.persistance.repository.ClientModelRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ClientModuleConfig {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientRepository clientRepository(ClientModelRepository repository, Outbox outbox,
                                             OutboxMessageFactory messageFactory, ClientModelMapper mapper) {

        return new ClientRepositoryAdapter(repository, outbox, messageFactory, mapper);
    }

    @Bean
    ChangeCustomerEmailCommandHandler changeClientEmailCommandHandler(ClientRepository clientRepository) {
        return new ChangeCustomerEmailCommandHandler(clientRepository);
    }

    @Bean
    ChangeCustomerPasswordCommandHandler changeClientPasswordCommandHandler(ClientRepository clientRepository) {
        return new ChangeCustomerPasswordCommandHandler(clientRepository);
    }

    @Bean
    RegisterCustomerCommandHandler registerClientCommandHandler(ClientRepository clientRepository) {
        return new RegisterCustomerCommandHandler(clientRepository);
    }

    @Bean
    GetCustomerByEmailQueryHandler getClientByEmailQueryHandler(ClientRepository clientRepository) {
        return new GetCustomerByEmailQueryHandler(clientRepository);
    }

    @Bean
    GetCustomerByIdQueryHandler getClientByIdQueryHandler(ClientRepository clientRepository) {
        return new GetCustomerByIdQueryHandler(clientRepository);
    }

    @Bean
    public ICustomerModule userModule(ApplicationContext applicationContext) {
        return new CustomerModule(applicationContext);
    }

} 