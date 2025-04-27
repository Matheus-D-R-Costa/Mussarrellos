package com.mussarrellos.backend.buildingblocks.application;

import com.mussarrellos.backend.buildingblocks.application.commands.CommandHandler;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import com.mussarrellos.backend.buildingblocks.application.queries.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.GenericTypeResolver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class AbstractModule<T> {

    private final ApplicationContext applicationContext;
    private final Class<T> moduleInterface;

    protected <R> Mono<R> executeCommand(ICommand<R> command) {
        return executeWithDirectHandler(
                command,
                () -> findCommandHandler(command),
                () -> findHandlerByReflection(command)
        );
    }

    protected Mono<Void> executeCommand(ICommandWithoutResult command) {
        return executeWithDirectHandler(
                command,
                () -> findCommandWithoutResultHandler(command),
                () -> findHandlerByReflection(command)
        );
    }

    protected <R> Mono<R> executeQuery(IQuery<R> query) {
        return executeWithDirectHandler(
                query,
                () -> findQueryHandler(query),
                () -> findHandlerByReflection(query)
        );
    }

    private <M, H, R> Mono<R> executeWithDirectHandler(M message, Supplier<H> directHandlerSupplier,
                                                       Supplier<Object> reflectionHandlerSupplier) {
        try {
            H handler = directHandlerSupplier.get();
            return invokeDirectHandler(handler, message);
        } catch (Exception e) {
            return Mono.defer(() -> {
                try {
                    Object handler = reflectionHandlerSupplier.get();
                    return invokeHandlerByReflection(handler, message);
                } catch (Exception ex) {
                    return Mono.error(ex);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private <M, H, R> Mono<R> invokeDirectHandler(H handler, M message) {
        try {
            if (handler instanceof CommandHandler) {
                return ((CommandHandler<M, R>) handler).handle(message);
            } else if (handler instanceof QueryHandler) {
                return ((QueryHandler<M, R>) handler).handle(message);
            }
            return Mono.error(new IllegalArgumentException("Handler type não suportado"));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <M, R> Mono<R> invokeHandlerByReflection(Object handler, M message) {
        try {
            Method method = handler.getClass().getMethod("handle", message.getClass());
            return (Mono<R>) method.invoke(handler, message);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Mono.error(new RuntimeException(
                    "Falha ao invocar o handler para " + message.getClass().getSimpleName(), e));
        }
    }

    @SuppressWarnings("unchecked")
    private <R> CommandHandler<ICommand<R>, R> findCommandHandler(ICommand<R> command) {
        return (CommandHandler<ICommand<R>, R>) findHandlerByType(
                command,
                CommandHandler.class,
                "command"
        );
    }

    @SuppressWarnings("unchecked")
    private CommandHandler<ICommandWithoutResult, Void> findCommandWithoutResultHandler(ICommandWithoutResult command) {
        return (CommandHandler<ICommandWithoutResult, Void>) findHandlerByType(
                command,
                CommandHandler.class,
                "command"
        );
    }

    @SuppressWarnings("unchecked")
    private <R> QueryHandler<IQuery<R>, R> findQueryHandler(IQuery<R> query) {
        return (QueryHandler<IQuery<R>, R>) findHandlerByType(
                query,
                QueryHandler.class,
                "query"
        );
    }

    private Object findHandlerByType(Object message, Class<?> handlerType, String messageTypeDescription) {
        Class<?> messageType = message.getClass();
        
        return Flux.fromIterable(applicationContext.getBeansOfType(handlerType).values())
                .filter(handler -> isHandlerForMessage(handler, handlerType, messageType))
                .next()
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Handler não encontrado para " + messageTypeDescription + ": " + messageType.getSimpleName())))
                .block();
    }

    private boolean isHandlerForMessage(Object handler, Class<?> handlerType, Class<?> messageType) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                handler.getClass(), handlerType);
        return generics != null && generics.length > 0 &&
                generics[0].isAssignableFrom(messageType);
    }

    private Object findHandlerByReflection(Object message) {
        Class<?> messageType = message.getClass();
        String expectedHandlerName = messageType.getSimpleName() + "Handler";
        String expectedPackage = messageType.getPackage().getName() + ".handlers";
        String expectedHandlerClassName = expectedPackage + "." + expectedHandlerName;
        
        try {
            Class<?> handlerClass = Class.forName(expectedHandlerClassName);
            return applicationContext.getBean(handlerClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Handler não encontrado para: " + messageType.getSimpleName(), e);
        }
    }
} 