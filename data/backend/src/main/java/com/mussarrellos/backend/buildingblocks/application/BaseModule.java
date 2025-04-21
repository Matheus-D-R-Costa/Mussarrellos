package com.mussarrellos.backend.buildingblocks.application;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Implementação base para todos os módulos da aplicação.
 * Fornece a implementação padrão dos métodos da 'interface' IModule.
 *
 * @param <T> O tipo específico da interface do módulo
 */
@Slf4j
public class BaseModule<T extends IModule> extends AbstractModule<T> implements IModule {

    public BaseModule(ApplicationContext applicationContext, Class<T> moduleInterface) {
        super(applicationContext, moduleInterface);
    }
//
//    @PostConstruct
//    protected void registerModule() {
//        ModuleRegistry.getInstance().registerModule(getModuleInterface(), this);
//        log.debug("Módulo {} registrado no ModuleRegistry", getModuleInterface().getSimpleName());
//    }

    @Override
    public <TResult> Mono<TResult> executeCommand(ICommand<TResult> command) {
        return super.executeCommand(command);
    }

    @Override
    public Mono<Void> executeCommand(ICommandWithoutResult command) {
        return super.executeCommand(command);
    }

    @Override
    public <TResult> Mono<TResult> executeQuery(IQuery<TResult> query) {
        return super.executeQuery(query);
    }
    
//    @Override
//    public Optional<IModule> getModuleByName(String moduleName) {
//        return ModuleRegistry.getInstance().getModule(moduleName);
//    }
//
//    @Override
//    public <M extends IModule> Optional<M> getModuleByName(String moduleName, Class<M> moduleType) {
//        return ModuleRegistry.getInstance().getModule(moduleName, moduleType);
//    }
} 