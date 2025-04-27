package com.mussarrellos.backend.buildingblocks.application;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.contracts.IModule;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import org.springframework.context.ApplicationContext;
import reactor.core.publisher.Mono;

public class BaseModule<T extends IModule> extends AbstractModule<T> implements IModule {

    public BaseModule(ApplicationContext applicationContext, Class<T> moduleInterface) {
        super(applicationContext, moduleInterface);
    }

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

} 