package br.com.socialconnect.api.doacoes.dto;

import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Doação retornada pela API")
public record DoacaoResponseDTO(
        @Schema(description = "Identificador da doação", example = "1")
        Long idDoacao,
        @Schema(description = "Identificador de referência do doador", example = "1")
        Long idDoador,
        @Schema(description = "Data da doação", example = "2026-10-16")
        LocalDate dataDoacao,
        @Schema(description = "Valor da doação, quando aplicável", example = "100.00")
        BigDecimal valor,
        @Schema(description = "Categoria da doação", example = "ALIMENTO")
        TipoDoacao tipo
) {
}
