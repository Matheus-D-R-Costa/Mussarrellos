package com.mussarrellos.backend.buildingblocks.domain.valueobjects;

import com.mussarrellos.backend.buildingblocks.domain.rules.BusinessRuleValidationException;
import com.mussarrellos.backend.buildingblocks.domain.rules.IBusinessRule;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ValueObject {
    private List<Field> fields;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        for (Field field : getFields()) {
            try {
                field.setAccessible(true);
                Object thisValue = field.get(this);
                Object otherValue = field.get(obj);
                if (!Objects.equals(thisValue, otherValue)) return false;
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Erro ao acessar campo durante comparação", e);
            }
        }
        
        return true;
    }

    @Override
    public int hashCode() {
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

    protected static void checkRule(IBusinessRule rule) {
        if (rule.isBroken()) throw new BusinessRuleValidationException(rule);
    }

    private List<Field> getFields() {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
            Class<?> currentClass = this.getClass();
            while (currentClass != null && currentClass != Object.class) {
                List<Field> classFields = Arrays.stream(currentClass.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(IgnoreMember.class))
                    .filter(f -> !Modifier.isStatic(f.getModifiers()))
                    .filter(f -> !Modifier.isTransient(f.getModifiers()))
                    .toList();
                this.fields.addAll(classFields);
                currentClass = currentClass.getSuperclass();
            }
        }
        
        return this.fields;
    }

    private int hashValue(int seed, Object value) {
        int currentHash = value != null ? value.hashCode() : 0;
        return (seed * 23) + currentHash;
    }
} 