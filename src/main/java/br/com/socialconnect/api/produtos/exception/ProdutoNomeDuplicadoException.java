package br.com.socialconnect.api.produtos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProdutoNomeDuplicadoException extends RuntimeException {
    public ProdutoNomeDuplicadoException(String nome) {
        super("Produto com nome '" + nome + "' já existe");
    }
}