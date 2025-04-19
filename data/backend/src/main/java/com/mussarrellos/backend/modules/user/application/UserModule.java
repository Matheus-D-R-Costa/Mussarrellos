package com.mussarrellos.backend.modules.user.application;

import com.mussarrellos.backend.buildingblocks.application.commands.CommandHandler;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.buildingblocks.application.queries.QueryHandler;
import com.mussarrellos.backend.modules.user.application.contracts.IUserModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementação da interface do módulo de usuários.
 * Fornece um ponto de entrada centralizado para todas as operações do módulo.
 * Usa CommandHandler e QueryHandler diretamente quando possível, com reflexão como fallback.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserModule implements IUserModule {

    private final ApplicationContext applicationContext;

    @Override
    public <TResult> Mono<TResult> executeCommand(ICommand<TResult> command) {
        log.debug("Executing command: {}", command.getClass().getSimpleName());
        
        try {
            // Tenta obter o handler diretamente pelo tipo genérico
            CommandHandler<ICommand<TResult>, TResult> handler = findCommandHandler(command);
            return handler.handle(command);
        } catch (Exception e) {
            log.warn("Could not find direct handler for {}, using reflection", command.getClass().getSimpleName());
            
            // Fallback para o método usando reflexão
            try {
                var handlerType = findCommandHandlerTypeByReflection(command);
                var handler = applicationContext.getBean(handlerType);
                
                var method = handlerType.getMethod("handle", command.getClass());
                @SuppressWarnings("unchecked")
                var result = (Mono<TResult>) method.invoke(handler, command);
                return result;
            } catch (Exception ex) {
                log.error("Error executing command: {}", command.getClass().getSimpleName(), e);
                return Mono.error(ex);
            }
        }
    }

    @Override
    public Mono<Void> executeCommand(ICommandWithoutResult command) {
        log.debug("Executing command without result: {}", command.getClass().getSimpleName());
        
        try {
            // Tenta obter o handler diretamente pelo tipo genérico
            CommandHandler<ICommandWithoutResult, Void> handler = findCommandWithoutResultHandler(command);
            return handler.handle(command);
        } catch (Exception e) {
            log.warn("Could not find direct handler for {}, using reflection", command.getClass().getSimpleName());
            
            // Fallback para o método usando reflexão
            try {
                var handlerType = findCommandWithoutResultHandlerTypeByReflection(command);
                var handler = applicationContext.getBean(handlerType);
                
                var method = handlerType.getMethod("handle", command.getClass());
                @SuppressWarnings("unchecked")
                var result = (Mono<Void>) method.invoke(handler, command);
                return result;
            } catch (Exception ex) {
                log.error("Error executing command without result: {}", command.getClass().getSimpleName(), e);
                return Mono.error(ex);
            }
        }
    }

    @Override
    public <TResult> Mono<TResult> executeQuery(IQuery<TResult> query) {
        log.debug("Executing query: {}", query.getClass().getSimpleName());
        
        try {
            // Tenta obter o handler diretamente pelo tipo genérico
            QueryHandler<IQuery<TResult>, TResult> handler = findQueryHandler(query);
            return handler.handle(query);
        } catch (Exception e) {
            log.warn("Could not find direct handler for {}, using reflection", query.getClass().getSimpleName());
            
            // Fallback para o método usando reflexão
            try {
                var handlerType = findQueryHandlerTypeByReflection(query);
                var handler = applicationContext.getBean(handlerType);
                
                var method = handlerType.getMethod("handle", query.getClass());
                @SuppressWarnings("unchecked")
                var result = (Mono<TResult>) method.invoke(handler, query);
                return result;
            } catch (Exception ex) {
                log.error("Error executing query: {}", query.getClass().getSimpleName(), e);
                return Mono.error(ex);
            }
        }
    }

    // Métodos auxiliares para encontrar handlers pelo tipo do comando/query

    @SuppressWarnings("unchecked")
    private <TResult> CommandHandler<ICommand<TResult>, TResult> findCommandHandler(ICommand<TResult> command) {
        Class<?> commandType = command.getClass();
        String handlerName = commandType.getSimpleName() + "Handler";
        
        // Tenta encontrar o bean pelo nome
        return (CommandHandler<ICommand<TResult>, TResult>) applicationContext.getBeansOfType(CommandHandler.class)
            .values()
            .stream()
            .filter(handler -> {
                Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                    handler.getClass(), CommandHandler.class);
                return generics != null && generics.length > 0 && 
                       generics[0].isAssignableFrom(commandType);
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Handler not found for command: " + commandType.getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    private CommandHandler<ICommandWithoutResult, Void> findCommandWithoutResultHandler(ICommandWithoutResult command) {
        Class<?> commandType = command.getClass();
        
        // Tenta encontrar o bean pelo nome
        return (CommandHandler<ICommandWithoutResult, Void>) applicationContext.getBeansOfType(CommandHandler.class)
            .values()
            .stream()
            .filter(handler -> {
                Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                    handler.getClass(), CommandHandler.class);
                return generics != null && generics.length > 0 && 
                       generics[0].isAssignableFrom(commandType);
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Handler not found for command: " + commandType.getSimpleName()));
    }

    @SuppressWarnings("unchecked")
    private <TResult> QueryHandler<IQuery<TResult>, TResult> findQueryHandler(IQuery<TResult> query) {
        Class<?> queryType = query.getClass();
        
        // Tenta encontrar o bean pelo tipo
        return (QueryHandler<IQuery<TResult>, TResult>) applicationContext.getBeansOfType(QueryHandler.class)
            .values()
            .stream()
            .filter(handler -> {
                Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                    handler.getClass(), QueryHandler.class);
                return generics != null && generics.length > 0 && 
                       generics[0].isAssignableFrom(queryType);
            })
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Handler not found for query: " + queryType.getSimpleName()));
    }

    // Métodos usando reflexão como fallback
    
    private <TResult> Class<?> findCommandHandlerTypeByReflection(ICommand<TResult> command) {
        var commandType = command.getClass();
        var expectedHandlerName = commandType.getSimpleName() + "Handler";
        var expectedPackage = commandType.getPackage().getName() + ".handlers";
        var expectedHandlerClassName = expectedPackage + "." + expectedHandlerName;
        
        try {
            return Class.forName(expectedHandlerClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Handler not found for command: " + commandType.getSimpleName(), e);
        }
    }

    private Class<?> findCommandWithoutResultHandlerTypeByReflection(ICommandWithoutResult command) {
        var commandType = command.getClass();
        var expectedHandlerName = commandType.getSimpleName() + "Handler";
        var expectedPackage = commandType.getPackage().getName() + ".handlers";
        var expectedHandlerClassName = expectedPackage + "." + expectedHandlerName;
        
        try {
            return Class.forName(expectedHandlerClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Handler not found for command: " + commandType.getSimpleName(), e);
        }
    }

    private <TResult> Class<?> findQueryHandlerTypeByReflection(IQuery<TResult> query) {
        var queryType = query.getClass();
        var expectedHandlerName = queryType.getSimpleName() + "Handler";
        var expectedPackage = queryType.getPackage().getName() + ".handlers";
        var expectedHandlerClassName = expectedPackage + "." + expectedHandlerName;
        
        try {
            return Class.forName(expectedHandlerClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Handler not found for query: " + queryType.getSimpleName(), e);
        }
    }
} 