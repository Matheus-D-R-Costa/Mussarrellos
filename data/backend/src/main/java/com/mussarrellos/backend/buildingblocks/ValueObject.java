package com.mussarrellos.backend.buildingblocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Classe base para todos os Value Objects do domínio.
 * Um Value Object é imutável e é considerado igual a outro se todos os seus atributos são iguais.
 */
public abstract class ValueObject {
    private List<Field> fields;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        // Compara todos os campos
        for (Field field : getFields()) {
            try {
                field.setAccessible(true);
                Object thisValue = field.get(this);
                Object otherValue = field.get(obj);
                
                if (!Objects.equals(thisValue, otherValue)) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo durante comparação", e);
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        
        for (Field field : getFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(this);
                result = hashValue(result, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo durante cálculo de hashCode", e);
            }
        }
        
        return result;
    }

    /**
     * Verifica se uma regra de negócio é violada.
     * @param rule Regra de negócio a ser verificada.
     * @throws BusinessRuleValidationException Se a regra for violada.
     */
    protected static void checkRule(IBusinessRule rule) {
        if (rule.isBroken()) {
            throw new BusinessRuleValidationException(rule);
        }
    }

    /**
     * Obtém todos os campos não estáticos e não transientes da classe e suas superclasses.
     * Ignora campos anotados com @IgnoreMember.
     * @return Lista de campos a serem considerados para equals e hashCode.
     */
    private List<Field> getFields() {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
            Class<?> currentClass = this.getClass();
            
            while (currentClass != null && currentClass != Object.class) {
                List<Field> classFields = Arrays.stream(currentClass.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(IgnoreMember.class))
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    .filter(f -> !Modifier.isTransient(f.getModifiers()))
                    .collect(Collectors.toList());
                
                this.fields.addAll(classFields);
                currentClass = currentClass.getSuperclass();
            }
        }
        
        return this.fields;
    }

    /**
     * Calcula o valor de hash para um campo.
     * @param seed Valor de hash acumulado.
     * @param value Valor do campo.
     * @return Novo valor de hash.
     */
    private int hashValue(int seed, Object value) {
        int currentHash = value != null ? value.hashCode() : 0;
        return (seed * 23) + currentHash;
    }
} 