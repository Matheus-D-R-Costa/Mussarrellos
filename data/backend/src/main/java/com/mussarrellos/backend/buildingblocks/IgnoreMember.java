package com.mussarrellos.backend.buildingblocks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar campos que devem ser ignorados na comparação de igualdade
 * e no cálculo do hashCode em Value Objects.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface IgnoreMember {
} 