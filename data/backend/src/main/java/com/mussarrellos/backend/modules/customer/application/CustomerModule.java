package com.mussarrellos.backend.modules.customer.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.customer.application.contracts.ICustomerModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * Implementação da ‘interface’ do módulo de clientes.
 * Estende BaseModule para aproveitar a implementação padrão dos métodos.
 */
@Slf4j
public class CustomerModule extends BaseModule<ICustomerModule> implements ICustomerModule {

    /**
     * Construtor para o módulo de usuários.
     *
     * @param applicationContext O contexto da aplicação Spring
     */
    public CustomerModule(ApplicationContext applicationContext) {
        super(applicationContext, ICustomerModule.class);
    }
    
}