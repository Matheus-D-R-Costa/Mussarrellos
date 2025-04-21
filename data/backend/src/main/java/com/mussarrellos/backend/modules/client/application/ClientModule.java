package com.mussarrellos.backend.modules.client.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.client.application.contracts.IClientModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * Implementação da ‘interface’ do módulo de clientes.
 * Estende BaseModule para aproveitar a implementação padrão dos métodos.
 */
@Slf4j
public class ClientModule extends BaseModule<IClientModule> implements IClientModule {

    /**
     * Construtor para o módulo de usuários.
     *
     * @param applicationContext O contexto da aplicação Spring
     */
    public ClientModule(ApplicationContext applicationContext) {
        super(applicationContext, IClientModule.class);
    }
    
}