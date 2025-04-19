package com.mussarrellos.backend.modules.user.infra.config;

import com.mussarrellos.backend.buildingblocks.application.outbox.Outbox;
import com.mussarrellos.backend.buildingblocks.application.outbox.OutboxMessageFactory;
import com.mussarrellos.backend.modules.user.domain.repository.ClientRepository;
import com.mussarrellos.backend.modules.user.infra.persistance.adapter.ClientRepositoryAdapter;
import com.mussarrellos.backend.modules.user.infra.persistance.mapper.ClientModelMapper;
import com.mussarrellos.backend.modules.user.infra.persistance.repository.ClientModelRepository;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Configuração da infraestrutura do módulo de usuário.
 * Equivalente ao RegistrationsContext do exemplo em C#.
 * Centraliza a configuração dos repositórios, mapeamentos e outros componentes de infraestrutura.
 */
@Slf4j
@Configuration
@EnableR2dbcRepositories(basePackages = "com.mussarrellos.backend.modules.user.infra.persistance.repository")
public class UserConfig {

    /**
     * Registra a implementação do ClientRepository como um bean no contexto do Spring.
     * Esta é a única implementação de ClientRepository que deve existir no contexto.
     * O ClientRepositoryAdapter não deve ser anotado com @Repository para evitar duplicação.
     */
    @Bean
    public ClientRepository clientRepository(ClientModelRepository repository, Outbox outbox,
                                          OutboxMessageFactory messageFactory, ClientModelMapper mapper) {
        
        return new ClientRepositoryAdapter(repository, outbox, messageFactory, mapper);
    }

    @Bean
    public R2dbcEntityTemplate userModuleR2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }
} 