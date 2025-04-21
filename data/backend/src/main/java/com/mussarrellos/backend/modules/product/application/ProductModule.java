package com.mussarrellos.backend.modules.product.application;

import com.mussarrellos.backend.buildingblocks.application.BaseModule;
import com.mussarrellos.backend.modules.product.application.contracts.IProductModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Implementação da interface do módulo de produtos.
 * Estende BaseModule para aproveitar a implementação padrão dos métodos.
 */
@Slf4j
@Service
public class ProductModule extends BaseModule<IProductModule> implements IProductModule {

    /**
     * Construtor para o módulo de produtos.
     *
     * @param applicationContext O contexto da aplicação Spring
     */
    public ProductModule(ApplicationContext applicationContext) {
        super(applicationContext, IProductModule.class);
    }
    
    // Métodos específicos do módulo de produtos podem ser adicionados aqui
} 