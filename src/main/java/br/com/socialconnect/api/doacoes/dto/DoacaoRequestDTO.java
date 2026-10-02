package br.com.socialconnect.api.doacoes.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DoacaoRequestDTO(
    @NotNull(message = "ID do doador é obrigatório")
    Long idDoador,

    @NotNull(message = "Data da doação é obrigatória")
    java.time.LocalDate dataDoacao,

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    BigDecimal valor,

    @NotBlank(message = "Tipo é obrigatório")
    String tipo,

    String descricao
) {}