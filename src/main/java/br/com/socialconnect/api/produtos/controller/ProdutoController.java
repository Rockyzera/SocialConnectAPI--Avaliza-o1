package br.com.socialconnect.api.produtos.controller;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.exception.EstoqueNegativoException;
import br.com.socialconnect.api.produtos.exception.ProdutoNomeDuplicadoException;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de produtos do estoque")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @Operation(summary = "Lista produtos com paginação e filtros",
               description = "Retorna lista paginada de produtos. Filtros opcionais: nome (parcial) e categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                     content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(
            @Parameter(description = "Filtro por nome parcial (case-insensitive)")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Filtro por categoria")
            @RequestParam(required = false) CategoriaProduto categoria,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, categoria, pageable));
    }

    @Operation(summary = "Busca produto por ID",
               description = "Retorna um produto específico pelo seu identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado",
                     content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long idProduto) {
        return ResponseEntity.ok(service.buscarPorId(idProduto));
    }

    @Operation(summary = "Cria novo produto",
               description = "Cadastra um novo produto no estoque. Retorna 201 com cabeçalho Location.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                     content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Nome de produto já existe",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Estoque atual menor que estoque mínimo",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
        ProdutoResponseDTO salvo = service.criar(dto);
        URI location = URI.create("/api/v1/produtos/" + salvo.idProduto());
        return ResponseEntity.created(location).body(salvo);
    }

    @Operation(summary = "Atualiza produto totalmente",
               description = "Atualização completa do produto (PUT)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                     content = @Content(schema = @Schema(implementation = ProdutoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Nome de produto já existe",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "422", description = "Estoque atual menor que estoque mínimo",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{idProduto}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long idProduto,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idProduto, dto));
    }

    @Operation(summary = "Remove produto",
               description = "Remove um produto do estoque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                     content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{idProduto}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do produto", example = "1")
            @PathVariable Long idProduto) {
        service.deletar(idProduto);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProdutoNomeDuplicadoException.class)
    public ResponseEntity<ProblemDetail> handleProdutoNomeDuplicado(ProdutoNomeDuplicadoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflito: Nome duplicado");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", "PRODUTO_NOME_DUPLICADO");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(EstoqueNegativoException.class)
    public ResponseEntity<ProblemDetail> handleEstoqueNegativo(EstoqueNegativoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        problem.setTitle("Entidade não processável: Estoque inválido");
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", "ESTOQUE_NEGATIVO");
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("não encontrado")) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
            problem.setTitle("Não encontrado");
            problem.setProperty("timestamp", Instant.now());
            problem.setProperty("errorCode", "PRODUTO_NAO_ENCONTRADO");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno");
        problem.setTitle("Erro interno do servidor");
        problem.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}