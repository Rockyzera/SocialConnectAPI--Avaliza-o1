package br.com.socialconnect.api.doadores.dto;

import java.time.LocalDate;

public record DoadorResponseDTO(
    Long idDoador,
    String nome,
    String cpfCnpj,
    String email,
    String telefone,
    String endereco,
    String tipoPessoa,
    LocalDate dataCadastro
) {}