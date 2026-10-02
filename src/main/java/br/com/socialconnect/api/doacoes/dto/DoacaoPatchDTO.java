package br.com.socialconnect.api.doacoes.dto;

import java.math.BigDecimal;

public record DoacaoPatchDTO(
    java.time.LocalDate dataDoacao,
    BigDecimal valor,
    String tipo,
    String descricao
) {}