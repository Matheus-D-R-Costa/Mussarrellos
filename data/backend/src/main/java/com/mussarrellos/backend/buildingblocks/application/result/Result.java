package com.mussarrellos.backend.buildingblocks.application.result;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Representa o resultado de uma operação, podendo ser sucesso ou erro.
 * Este padrão elimina a necessidade de tratamento de exceções para fluxos de negócio
 * e possibilita a combinação de múltiplos resultados.
 *
 * @param <T> O tipo do resultado em caso de sucesso
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {

    @Getter
    private final T value;

    @Getter
    private final List<Error> errors;

    /**
     * Verifica se o resultado foi bem-sucedido (sem erros).
     *
     * @return true se o resultado foi bem-sucedido, false caso contrário
     */
    public boolean isSuccess() {
        return errors == null || errors.isEmpty();
    }

    /**
     * Cria um resultado de sucesso com o valor fornecido.
     *
     * @param value O valor do resultado
     * @param <T> O tipo do valor
     * @return Um Result bem-sucedido contendo o valor
     */
    public static <T> Result<T> success(T value) {
        return new Result<>(value, Collections.emptyList());
    }

    /**
     * Cria um resultado de falha com os erros fornecidos.
     *
     * @param errors A lista de erros
     * @param <T> O tipo do valor (não utilizado em caso de falha)
     * @return Um Result de falha contendo os erros
     */
    public static <T> Result<T> failure(List<Error> errors) {
        return new Result<>(null, errors);
    }

    /**
     * Cria um resultado de falha com o erro fornecido.
     *
     * @param error O erro
     * @param <T> O tipo do valor (não utilizado em caso de falha)
     * @return Um Result de falha contendo o erro
     */
    public static <T> Result<T> failure(Error error) {
        List<Error> errors = new ArrayList<>();
        errors.add(error);
        return failure(errors);
    }

    /**
     * Cria um resultado de falha com a mensagem de erro fornecida.
     *
     * @param errorMessage A mensagem de erro
     * @param <T> O tipo do valor (não utilizado em caso de falha)
     * @return Um Result de falha contendo o erro
     */
    public static <T> Result<T> failure(String errorMessage) {
        return failure(new Error(errorMessage));
    }

    /**
     * Mapeia o valor deste resultado para um novo valor usando a função fornecida.
     * Se este resultado for uma falha, a função não é aplicada e a falha é propagada.
     *
     * @param mapper A função de mapeamento a ser aplicada ao valor
     * @param <U> O tipo do novo valor
     * @return Um novo Result contendo o valor mapeado ou os erros originais
     */
    public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        if (isSuccess()) {
            return Result.success(mapper.apply(value));
        } else {
            return Result.failure(errors);
        }
    }

    /**
     * Executa a ação fornecida se o resultado for bem-sucedido.
     *
     * @param action A ação a ser executada com o valor do resultado
     * @return Este resultado, para encadeamento
     */
    public Result<T> onSuccess(Consumer<T> action) {
        if (isSuccess()) {
            action.accept(value);
        }
        return this;
    }

    /**
     * Executa a ação fornecida se o resultado for uma falha.
     *
     * @param action A ação a ser executada com os erros do resultado
     * @return Este resultado, para encadeamento
     */
    public Result<T> onFailure(Consumer<List<Error>> action) {
        if (!isSuccess()) {
            action.accept(errors);
        }
        return this;
    }

    @FunctionalInterface
    public interface Consumer<T> {
        void accept(T t);
    }
} 