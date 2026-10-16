package br.com.socialconnect.api.beneficiarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Campos opcionais para atualizar parcialmente um beneficiário")
public record BeneficiarioPatchDTO(

        @Schema(description = "Nome completo do beneficiário", example = "Maria da Silva")
        @NotBlank(message = "{beneficiario.nome.naoEmBranco}")
        @Size(max = 150, message = "{beneficiario.nome.tamanho}")
        String nome,

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
