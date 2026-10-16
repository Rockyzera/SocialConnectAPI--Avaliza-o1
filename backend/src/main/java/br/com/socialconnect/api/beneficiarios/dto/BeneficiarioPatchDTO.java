package br.com.socialconnect.api.beneficiarios.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record BeneficiarioPatchDTO(

        @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
        @Pattern(regexp = ".*\\S.*", message = "Nome não pode ficar em branco")
        String nome,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
        String endereco,

        @Size(max = 500, message = "Situação deve ter no máximo 500 caracteres")
        String situacaoVulnerabilidade
) {
}
