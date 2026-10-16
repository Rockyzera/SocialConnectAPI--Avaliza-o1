package br.com.socialconnect.api.doacoes.dto;

import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.validation.DataNaoFutura;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dados necessários para cadastrar uma doação")
public record DoacaoRequestDTO(

        @Schema(description = "Identificador de referência do doador", example = "1")
        @NotNull(message = "{doacao.doador.obrigatorio}")
        Long idDoador,

        @Schema(description = "Data em que a doação foi realizada", example = "2026-10-16")
        @NotNull(message = "{doacao.data.obrigatoria}")
        @DataNaoFutura(message = "{doacao.data.naoFutura}")
        LocalDate dataDoacao,

        @Schema(description = "Valor da doação, quando aplicável", example = "100.00")
        @DecimalMin(value = "0.01", message = "{doacao.valor.positivo}")
        BigDecimal valor,

        @Schema(description = "Categoria da doação", example = "ALIMENTO")
        @NotNull(message = "{doacao.tipo.obrigatorio}")
        TipoDoacao tipo,

        @Schema(description = "Descrição complementar", example = "Cestas básicas")
        @Size(max = 500, message = "{doacao.descricao.tamanho}")
        String descricao
) {
}
