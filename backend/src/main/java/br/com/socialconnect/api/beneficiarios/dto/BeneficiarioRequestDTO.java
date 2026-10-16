package br.com.socialconnect.api.beneficiarios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados necessários para cadastrar ou substituir um beneficiário")
public record BeneficiarioRequestDTO(

        @Schema(description = "Nome completo do beneficiário", example = "Maria da Silva")
        @NotBlank(message = "{beneficiario.nome.obrigatorio}")
        @Size(max = 150, message = "{beneficiario.nome.tamanho}")
        String nome,

        @Schema(description = "CPF com 11 dígitos ou CNPJ com 14 dígitos, sem pontuação", example = "12345678901")
        @NotBlank(message = "{beneficiario.cpf.obrigatorio}")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "{beneficiario.cpf.formato}"
        )
        String cpf,

        @Schema(description = "Telefone de contato", example = "11999999999")
        @Size(max = 20, message = "{beneficiario.telefone.tamanho}")
        String telefone,

        @Schema(description = "Endereço do beneficiário", example = "Rua das Flores, 100")
        @Size(max = 255, message = "{beneficiario.endereco.tamanho}")
        String endereco,

        @Schema(description = "Descrição da situação de vulnerabilidade", example = "Família em situação de insegurança alimentar")
        @Size(max = 500, message = "{beneficiario.situacao.tamanho}")
        String situacaoVulnerabilidade
) {
}
