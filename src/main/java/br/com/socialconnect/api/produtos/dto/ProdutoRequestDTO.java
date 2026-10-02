package br.com.socialconnect.api.produtos.dto;

import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.validation.EstoqueNaoNegativo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@EstoqueNaoNegativo
@Schema(description = "Dados para criação/atualização de produto")
public record ProdutoRequestDTO(
    @NotBlank(message = "{produto.nome.obrigatorio}")
    @Size(max = 150, message = "{produto.nome.tamanho}")
    @Schema(example = "Arroz 5kg", description = "Nome do produto")
    String nome,

    @NotNull(message = "{produto.categoria.obrigatoria}")
    @Schema(example = "ALIMENTO", description = "Categoria do produto")
    CategoriaProduto categoria,

    @Min(value = 0, message = "{produto.estoque.minimo}")
    @Schema(example = "50", description = "Quantidade atual em estoque")
    Integer estoqueAtual,

    @Min(value = 0, message = "{produto.estoque.minimo}")
    @Schema(example = "10", description = "Quantidade mínima de estoque para alerta")
    Integer estoqueMinimo,

    @NotBlank(message = "{produto.unidade.obrigatoria}")
    @Size(max = 20, message = "{produto.unidade.tamanho}")
    @Schema(example = "unidade", description = "Unidade de medida (ex: kg, unidade, litro)")
    String unidadeMedida
) {}