package com.mussarrellos.backend.modules.client;

import com.mussarrellos.backend.buildingblocks.application.outbox.Outbox;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessageFactory;
import com.mussarrellos.backend.modules.client.application.ClientModule;
import com.mussarrellos.backend.modules.client.application.commands.handlers.ChangeClientEmailCommandHandler;
import com.mussarrellos.backend.modules.client.application.commands.handlers.ChangeClientPasswordCommandHandler;
import com.mussarrellos.backend.modules.client.application.commands.handlers.RegisterClientCommandHandler;
import com.mussarrellos.backend.modules.client.application.contracts.IClientModule;
import com.mussarrellos.backend.modules.client.application.queries.handlers.GetClientByEmailQueryHandler;
import com.mussarrellos.backend.modules.client.application.queries.handlers.GetClientByIdQueryHandler;
import com.mussarrellos.backend.modules.client.domain.repository.ClientRepository;
import com.mussarrellos.backend.modules.client.infra.persistance.adapter.ClientRepositoryAdapter;
import com.mussarrellos.backend.modules.client.infra.persistance.mapper.ClientModelMapper;
import com.mussarrellos.backend.modules.client.infra.persistance.repository.ClientModelRepository;
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
    ChangeClientEmailCommandHandler changeClientEmailCommandHandler(ClientRepository clientRepository) {
        return new ChangeClientEmailCommandHandler(clientRepository);
    }

    @Bean
    ChangeClientPasswordCommandHandler changeClientPasswordCommandHandler(ClientRepository clientRepository) {
        return new ChangeClientPasswordCommandHandler(clientRepository);
    }

    @Bean
    RegisterClientCommandHandler registerClientCommandHandler(ClientRepository clientRepository) {
        return new RegisterClientCommandHandler(clientRepository);
    }

    @Bean
    GetClientByEmailQueryHandler getClientByEmailQueryHandler(ClientRepository clientRepository) {
        return new GetClientByEmailQueryHandler(clientRepository);
    }

    @Bean
    GetClientByIdQueryHandler getClientByIdQueryHandler(ClientRepository clientRepository) {
        return new GetClientByIdQueryHandler(clientRepository);
    }

    @Bean
    public IClientModule userModule(ApplicationContext applicationContext) {
        return new ClientModule(applicationContext);
    }

} 