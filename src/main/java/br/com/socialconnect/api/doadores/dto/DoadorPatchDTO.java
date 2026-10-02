package br.com.socialconnect.api.doadores.dto;

public record DoadorPatchDTO(
    String nome,
    String telefone,
    String endereco,
    String email
) {}