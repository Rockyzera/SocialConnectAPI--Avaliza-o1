package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Dados de resposta do produto")
public record ProdutoResponseDTO(
    @Schema(example = "1", description = "Identificador único do produto")
    Long idProduto,

    @Schema(example = "Arroz 5kg", description = "Nome do produto")
    String nome,

    @Schema(example = "ALIMENTO", description = "Categoria do produto")
    CategoriaProduto categoria,

    @Schema(example = "50", description = "Quantidade atual em estoque")
    Integer estoqueAtual,

    @Schema(example = "10", description = "Quantidade mínima de estoque para alerta")
    Integer estoqueMinimo,

    @Schema(example = "unidade", description = "Unidade de medida")
    String unidadeMedida,

    @Schema(example = "2026-09-18", description = "Data de cadastro do produto")
    LocalDate dataCadastro,

    @Schema(example = "true", description = "Indica se o estoque está abaixo do mínimo")
    boolean estoqueBaixo
) {}