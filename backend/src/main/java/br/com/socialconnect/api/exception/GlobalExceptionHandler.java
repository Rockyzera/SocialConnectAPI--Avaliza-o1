package br.com.socialconnect.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarErrosDeValidacao(
            MethodArgumentNotValidException exception) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos enviados são inválidos."
        );
        problema.setType(URI.create("https://socialconnect.api/erros/validacao"));
        problema.setTitle("Erro de validação");
        problema.setProperty("erros", exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> Map.of(
                        "campo", erro.getField(),
                        "mensagem", erro.getDefaultMessage() == null
                                ? "Valor inválido"
                                : erro.getDefaultMessage()
                ))
                .toList());

        return problema;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail tratarErrosHttp(ResponseStatusException exception) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                exception.getStatusCode(),
                exception.getReason() == null
                        ? "Não foi possível concluir a solicitação."
                        : exception.getReason()
        );
        problema.setType(URI.create("https://socialconnect.api/erros/http"));
        problema.setTitle(HttpStatus.valueOf(exception.getStatusCode().value())
                .getReasonPhrase());

        return problema;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInesperado(Exception exception) {
        LOGGER.error("Erro inesperado ao processar a requisição", exception);

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente."
        );
        problema.setType(URI.create("https://socialconnect.api/erros/interno"));
        problema.setTitle("Erro interno do servidor");

        return problema;
    }
}
