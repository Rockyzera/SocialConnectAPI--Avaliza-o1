package br.com.socialconnect.api.beneficiarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Representação de um beneficiário retornada pela API")
public record BeneficiarioResponseDTO(
        @Schema(description = "Identificador do beneficiário", example = "1")
        Long idBeneficiario,
        @Schema(description = "Nome completo", example = "Maria da Silva")
        String nome,
        @Schema(description = "CPF ou CNPJ sem pontuação", example = "12345678901")
        String cpf,
        @Schema(description = "Telefone de contato", example = "11999999999")
        String telefone,
        @Schema(description = "Endereço", example = "Rua das Flores, 100")
        String endereco,
        @Schema(description = "Situação de vulnerabilidade", example = "Família em situação de insegurança alimentar")
        String situacaoVulnerabilidade,
        @Schema(description = "Data em que o cadastro foi criado", example = "2026-10-16")
        LocalDate dataCadastro
) {
}
