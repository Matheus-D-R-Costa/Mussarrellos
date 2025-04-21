package com.mussarrellos.backend.modules.client.domain.rules;

import com.mussarrellos.backend.buildingblocks.IBusinessRule;
import com.mussarrellos.backend.modules.client.domain.entity.Client.EmailUniquenessChecker;
import com.mussarrellos.backend.modules.client.domain.rules.PasswordMustNotBeSameRule.PasswordMatchChecker;
import com.mussarrellos.backend.modules.client.domain.specification.PasswordSpecification;

/**
 * Factory para regras de negócio relacionadas a clientes.
 * Centraliza a criação de regras, seguindo o padrão Factory Method.
 * Facilita a manutenção e evita duplicação nas criações de regras.
 */
public final class ClientRulesFactory {
    
    // Construtor privado para evitar instanciação
    private ClientRulesFactory() {}
    
    /**
     * Cria uma regra que verifica se um email é válido.
     * 
     * @param email O email a ser validado
     * @return A regra de negócio
     */
    public static IBusinessRule emailMustBeValid(String email) {
        return new EmailMustBeValidRule(email);
    }
    
    /**
     * Cria uma regra que verifica se um email é único.
     * 
     * @param email O email a ser verificado
     * @param checker O verificador de unicidade
     * @return A regra de negócio
     */
    public static IBusinessRule emailMustBeUnique(String email, EmailUniquenessChecker checker) {
        return new EmailMustBeUniqueRule(email, checker);
    }
    
    /**
     * Cria uma regra que verifica se uma senha é forte o suficiente.
     * 
     * @param password A senha a ser verificada
     * @return A regra de negócio
     */
    public static IBusinessRule passwordMustBeStrong(String password) {
        return new PasswordMustBeStrongRule(password);
    }
    
    /**
     * Cria uma regra que verifica se uma senha é forte o suficiente com requisitos adicionais.
     * 
     * @param password A senha a ser verificada
     * @param requireComplexity Se deve exigir verificações adicionais de complexidade
     * @return A regra de negócio
     */
    public static IBusinessRule passwordMustBeStrong(String password, boolean requireComplexity) {
        return new PasswordMustBeStrongRule(password, requireComplexity);
    }
    
    /**
     * Cria uma regra que verifica se uma senha é forte e com uma especificação personalizada.
     * 
     * @param password A senha a ser verificada
     * @param passwordSpecification A especificação de senha personalizada
     * @return A regra de negócio
     */
    public static IBusinessRule passwordMustBeStrong(String password, PasswordSpecification passwordSpecification) {
        return new PasswordMustBeStrongRule(password, passwordSpecification);
    }
    
    /**
     * Cria uma regra que verifica se uma nova senha é diferente da senha atual.
     * 
     * @param newPassword A nova senha
     * @param passwordMatchChecker O verificador de correspondência de senha
     * @return A regra de negócio
     */
    public static IBusinessRule passwordMustNotBeSame(String newPassword, PasswordMatchChecker passwordMatchChecker) {
        return new PasswordMustNotBeSameRule(newPassword, passwordMatchChecker);
    }
} 