package com.mussarrellos.backend.buildingblocks.application;

import com.mussarrellos.backend.buildingblocks.application.commands.ICommand;
import com.mussarrellos.backend.buildingblocks.application.commands.ICommandWithoutResult;
import com.mussarrellos.backend.buildingblocks.application.queries.IQuery;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 'Interface' base para todos os módulos da aplicação.
 * Define as operações comuns disponíveis em todos os módulos.
 */
public interface IModule {
    /**
     * Executa um comando que retorna um resultado.
     *
     * @param command O comando a ser executado
     * @param <TResult> O tipo do resultado
     * @return Um Mono contendo o resultado da execução do comando
     */
    <TResult> Mono<TResult> executeCommand(ICommand<TResult> command);

    /**
     * Executa um comando que não retorna resultado.
     *
     * @param command O comando a ser executado
     * @return Um Mono vazio que sinaliza a conclusão da execução
     */
    Mono<Void> executeCommand(ICommandWithoutResult command);

    /**
     * Executa uma consulta.
     *
     * @param query A consulta a ser executada
     * @param <TResult> O tipo do resultado
     * @return Um Mono contendo o resultado da consulta
     */
    <TResult> Mono<TResult> executeQuery(IQuery<TResult> query);
    
    /**
     * Obtém outro módulo pelo nome.
     * Este método permite a comunicação entre módulos sem criar dependências diretas.
     *
     * @param moduleName O nome do módulo a ser obtido (sem prefixo "I" e sufixo "Module")
     * @return Um Optional contendo o módulo, se encontrado
     */
    Optional<IModule> getModuleByName(String moduleName);
    
    /**
     * Obtém outro módulo pelo nome, já convertido para o tipo específico.
     * Este método combina a busca e a conversão de tipo em uma única operação.
     *
     * @param moduleName O nome do módulo a ser obtido
     * @param moduleType A classe da interface do módulo
     * @param <T> O tipo da interface do módulo
     * @return Um Optional contendo o módulo convertido para o tipo especificado, se encontrado
     */
    <T extends IModule> Optional<T> getModuleByName(String moduleName, Class<T> moduleType);
} 