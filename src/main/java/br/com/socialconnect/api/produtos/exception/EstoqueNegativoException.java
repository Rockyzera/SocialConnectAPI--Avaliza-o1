package br.com.socialconnect.api.produtos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EstoqueNegativoException extends RuntimeException {
    public EstoqueNegativoException(String message) {
        super(message);
    }
}