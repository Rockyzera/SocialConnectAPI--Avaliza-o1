package br.com.socialconnect.api.beneficiarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BeneficiarioRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "CPF deve ter 11 ou 14 dígitos"
        )
        String cpf,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco,

        @Size(max = 500, message = "Situação deve ter no máximo 500 caracteres")
        String situacaoVulnerabilidade
) {
}
