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

/**
 * Classe base abstrata para módulos da aplicação.
 * Implementa a lógica comum para execução de comandos e consultas,
 * evitando duplicação de código entre os diferentes módulos.
 *
 * @param <T> O tipo da interface do módulo
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractModule<T> {

    private final ApplicationContext applicationContext;
    private final Class<T> moduleInterface;

    /**
     * Executa um comando que retorna um resultado.
     *
     * @param command O comando a ser executado
     * @param <R> O tipo do resultado
     * @return Um Mono contendo o resultado da execução do comando
     */
    protected <R> Mono<R> executeCommand(ICommand<R> command) {
        return executeWithDirectHandler(
                command,
                () -> findCommandHandler(command),
                () -> findHandlerByReflection(command)
        );
    }

    /**
     * Executa um comando que não retorna resultado.
     *
     * @param command O comando a ser executado
     * @return Um Mono vazio que sinaliza a conclusão da execução
     */
    protected Mono<Void> executeCommand(ICommandWithoutResult command) {
        return executeWithDirectHandler(
                command,
                () -> findCommandWithoutResultHandler(command),
                () -> findHandlerByReflection(command)
        );
    }

    /**
     * Executa uma consulta.
     *
     * @param query A consulta a ser executada
     * @param <R> O tipo do resultado
     * @return Um Mono contendo o resultado da consulta
     */
    protected <R> Mono<R> executeQuery(IQuery<R> query) {
        return executeWithDirectHandler(
                query,
                () -> findQueryHandler(query),
                () -> findHandlerByReflection(query)
        );
    }

    /**
     * Método genérico para execução de comandos/queries com tratamento de erro unificado.
     *
     * @param message Comando ou query a ser executado
     * @param directHandlerSupplier Fornecedor do handler direto
     * @param reflectionHandlerSupplier Fornecedor do handler por reflexão (fallback)
     * @param <M> Tipo da mensagem (comando ou query)
     * @param <H> Tipo do handler
     * @param <R> Tipo do resultado
     * @return Mono com o resultado da execução
     */
    private <M, H, R> Mono<R> executeWithDirectHandler(M message, Supplier<H> directHandlerSupplier,
                                                       Supplier<Object> reflectionHandlerSupplier) {
        String messageType = message.getClass().getSimpleName();
        log.debug("[{}] Executing {}", moduleInterface.getSimpleName(), messageType);

        try {
            // Tenta obter o handler diretamente pelo tipo genérico
            H handler = directHandlerSupplier.get();
            return invokeDirectHandler(handler, message);
        } catch (Exception e) {
            log.warn("[{}] Could not find direct handler for {}, using reflection",
                    moduleInterface.getSimpleName(), messageType);

            // Fallback para o método usando reflexão
            return Mono.defer(() -> {
                try {
                    Object handler = reflectionHandlerSupplier.get();
                    return invokeHandlerByReflection(handler, message);
                } catch (Exception ex) {
                    log.error("[{}] Error executing {}: {}",
                            moduleInterface.getSimpleName(), messageType, ex.getMessage());
                    return Mono.error(ex);
                }
            });
        }
    }

    /**
     * Invoca um handler direto (sem reflexão).
     *
     * @param handler O handler a ser invocado
     * @param message A mensagem a ser passada para o handler
     * @param <M> Tipo da mensagem
     * @param <H> Tipo do handler
     * @param <R> Tipo do resultado
     * @return Mono com o resultado da execução
     */
    @SuppressWarnings("unchecked")
    private <M, H, R> Mono<R> invokeDirectHandler(H handler, M message) {
        try {
            if (handler instanceof CommandHandler) {
                return (Mono<R>) ((CommandHandler<M, R>) handler).handle(message);
            } else if (handler instanceof QueryHandler) {
                return (Mono<R>) ((QueryHandler<M, R>) handler).handle(message);
            }
            return Mono.error(new IllegalArgumentException("Unsupported handler type"));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    /**
     * Invoca um handler usando reflexão.
     *
     * @param handler O handler a ser invocado
     * @param message A mensagem a ser passada para o handler
     * @param <M> Tipo da mensagem
     * @param <R> Tipo do resultado
     * @return Mono com o resultado da execução
     */
    @SuppressWarnings("unchecked")
    private <M, R> Mono<R> invokeHandlerByReflection(Object handler, M message) {
        try {
            Method method = handler.getClass().getMethod("handle", message.getClass());
            return (Mono<R>) method.invoke(handler, message);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return Mono.error(new RuntimeException(
                    "Failed to invoke handler for " + message.getClass().getSimpleName(), e));
        }
    }

    // Métodos de busca de handlers

    /**
     * Encontra o handler apropriado para um comando com resultado.
     *
     * @param command O comando para o qual se deseja encontrar o handler
     * @param <R> Tipo do resultado
     * @return O handler encontrado
     */
    @SuppressWarnings("unchecked")
    private <R> CommandHandler<ICommand<R>, R> findCommandHandler(ICommand<R> command) {
        return (CommandHandler<ICommand<R>, R>) findHandlerByType(
                command,
                CommandHandler.class,
                "command"
        );
    }

    /**
     * Encontra o handler apropriado para um comando sem resultado.
     *
     * @param command O comando para o qual se deseja encontrar o handler
     * @return O handler encontrado
     */
    @SuppressWarnings("unchecked")
    private CommandHandler<ICommandWithoutResult, Void> findCommandWithoutResultHandler(ICommandWithoutResult command) {
        return (CommandHandler<ICommandWithoutResult, Void>) findHandlerByType(
                command,
                CommandHandler.class,
                "command"
        );
    }

    /**
     * Encontra o handler apropriado para uma consulta.
     *
     * @param query A consulta para a qual se deseja encontrar o handler
     * @param <R> Tipo do resultado
     * @return O handler encontrado
     */
    @SuppressWarnings("unchecked")
    private <R> QueryHandler<IQuery<R>, R> findQueryHandler(IQuery<R> query) {
        return (QueryHandler<IQuery<R>, R>) findHandlerByType(
                query,
                QueryHandler.class,
                "query"
        );
    }

    /**
     * Método genérico para encontrar handlers com base no tipo da mensagem.
     *
     * @param message A mensagem (comando ou consulta)
     * @param handlerType O tipo do handler
     * @param messageTypeDescription Descrição do tipo de mensagem (para log)
     * @return O handler encontrado
     */
    private Object findHandlerByType(Object message, Class<?> handlerType, String messageTypeDescription) {
        Class<?> messageType = message.getClass();
        
        return Flux.fromIterable(applicationContext.getBeansOfType(handlerType).values())
                .filter(handler -> isHandlerForMessage(handler, handlerType, messageType))
                .next()
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Handler not found for " + messageTypeDescription + ": " + messageType.getSimpleName())))
                .block(); // Infelizmente precisamos bloquear aqui para manter a API compatível
    }

    /**
     * Verifica se um handler pode processar uma determinada mensagem.
     *
     * @param handler O handler a ser verificado
     * @param handlerType O tipo do handler
     * @param messageType O tipo da mensagem
     * @return true se o handler puder processar a mensagem
     */
    private boolean isHandlerForMessage(Object handler, Class<?> handlerType, Class<?> messageType) {
        Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(
                handler.getClass(), handlerType);
        return generics != null && generics.length > 0 &&
                generics[0].isAssignableFrom(messageType);
    }

    /**
     * Encontra um handler através de reflexão com base em convenções de nomenclatura.
     *
     * @param message A mensagem (comando ou consulta)
     * @return O handler encontrado
     */
    private Object findHandlerByReflection(Object message) {
        Class<?> messageType = message.getClass();
        String expectedHandlerName = messageType.getSimpleName() + "Handler";
        String expectedPackage = messageType.getPackage().getName() + ".handlers";
        String expectedHandlerClassName = expectedPackage + "." + expectedHandlerName;
        
        try {
            Class<?> handlerClass = Class.forName(expectedHandlerClassName);
            return applicationContext.getBean(handlerClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Handler not found for message: " + messageType.getSimpleName(), e);
        }
    }

//    /**
//     * Método para obter a interface do módulo que está sendo implementada.
//     * Esta informação é necessária para registro e identificação do módulo.
//     *
//     * @return A classe da interface do módulo
//     */
//    protected Class<T> getModuleInterface() {
//        return moduleInterface;
//    }
} 