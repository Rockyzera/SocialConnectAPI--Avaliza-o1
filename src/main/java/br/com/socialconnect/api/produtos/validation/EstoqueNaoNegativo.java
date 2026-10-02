package br.com.socialconnect.api.produtos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EstoqueNaoNegativoValidator.class)
@Documented
public @interface EstoqueNaoNegativo {
    String message() default "Estoque atual não pode ser menor que estoque mínimo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}