package br.com.socialconnect.api.beneficiarios.dto;

public record BeneficiarioRequestDTO(
        String nome,
        String cpf,
        String telefone,
        String endereco,
        String situacaoVulnerabilidade
) {
}
