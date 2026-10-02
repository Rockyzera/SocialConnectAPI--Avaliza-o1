package br.com.socialconnect.api.doadores.dto;

import jakarta.validation.constraints.*;

public record DoadorRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    String nome,

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    @Pattern(regexp = "\\d{11}|\\d{14}|\\d{18}", message = "CPF/CNPJ inválido")
    String cpfCnpj,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "Email deve ter no máximo 150 caracteres")
    String email,

    String telefone,
    String endereco,

    @NotNull(message = "Tipo de pessoa é obrigatório (F ou J)")
    @Pattern(regexp = "[FJ]", message = "Tipo deve ser 'F' ou 'J'")
    String tipoPessoa
) {}