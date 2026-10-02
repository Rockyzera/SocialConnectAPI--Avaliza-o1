package br.com.socialconnect.api.produtos.validation;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EstoqueNaoNegativoValidator implements ConstraintValidator<EstoqueNaoNegativo, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof ProdutoRequestDTO dto) {
            if (dto.estoqueAtual() != null && dto.estoqueMinimo() != null) {
                return dto.estoqueAtual() >= dto.estoqueMinimo();
            }
        }
        return true;
    }
}