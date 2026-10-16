package br.com.socialconnect.api.beneficiarios.controller;

import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioRequestDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioResponseDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioPatchDTO;
import br.com.socialconnect.api.beneficiarios.service.BeneficiarioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/beneficiarios")
@Tag(name = "Beneficiários", description = "Cadastro e consulta de pessoas beneficiárias")
public class BeneficiarioController {

    private final BeneficiarioService service;

    public BeneficiarioController(BeneficiarioService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar beneficiários", description = "Retorna uma página de beneficiários, com filtros opcionais por nome e CPF e ordenação configurável.")
    @ApiResponse(responseCode = "200", description = "Página de beneficiários retornada")
    public ResponseEntity<Page<BeneficiarioResponseDTO>> listar(
            @Parameter(description = "Filtra por trecho do nome, sem diferenciar maiúsculas e minúsculas", example = "Maria")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Filtra pelo CPF ou CNPJ", example = "12345678901")
            @RequestParam(required = false) String cpf,
            @ParameterObject
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, cpf, pageable));
    }

    @GetMapping("/{idBeneficiario}")
    @Operation(summary = "Buscar beneficiário por ID", description = "Localiza um beneficiário pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beneficiário encontrado"),
            @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public ResponseEntity<BeneficiarioResponseDTO> buscarPorId(
            @Parameter(description = "Identificador do beneficiário", example = "1")
            @PathVariable Long idBeneficiario) {
        return ResponseEntity.ok(service.buscarPorId(idBeneficiario));
    }

    @PostMapping
    @Operation(summary = "Cadastrar beneficiário", description = "Cria um cadastro após validar os dados enviados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Beneficiário cadastrado", content = @Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = BeneficiarioResponseDTO.class),
                    examples = @ExampleObject(value = """
                            {"idBeneficiario":1,"nome":"Maria da Silva","cpf":"12345678901","telefone":"11999999999","endereco":"Rua das Flores, 100","situacaoVulnerabilidade":"Família em situação de insegurança alimentar","dataCadastro":"2026-10-16"}
                            """))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou CNPJ já cadastrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados fictícios de exemplo para cadastro",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {"nome":"Maria da Silva","cpf":"12345678901","telefone":"11999999999","endereco":"Rua das Flores, 100","situacaoVulnerabilidade":"Família em situação de insegurança alimentar"}
                    """)))
    public ResponseEntity<BeneficiarioResponseDTO> salvar(
            @Valid @RequestBody BeneficiarioRequestDTO dto) {
        BeneficiarioResponseDTO salvo = service.salvar(dto);
        URI location = URI.create("/api/v1/beneficiarios/" + salvo.idBeneficiario());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{idBeneficiario}")
    @Operation(summary = "Substituir beneficiário", description = "Atualiza todos os campos editáveis do beneficiário informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beneficiário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado"),
            @ApiResponse(responseCode = "409", description = "CPF ou CNPJ já cadastrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados fictícios para substituir os campos editáveis",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {"nome":"Maria da Silva","cpf":"12345678901","telefone":"11999999999","endereco":"Rua das Flores, 100","situacaoVulnerabilidade":"Família em situação de insegurança alimentar"}
                    """)))
    public ResponseEntity<BeneficiarioResponseDTO> atualizar(
            @Parameter(description = "Identificador do beneficiário", example = "1")
            @PathVariable Long idBeneficiario,
            @Valid @RequestBody BeneficiarioRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idBeneficiario, dto));
    }

    @PatchMapping("/{idBeneficiario}")
    @Operation(summary = "Atualizar parcialmente beneficiário", description = "Atualiza somente os campos enviados. Campos omitidos permanecem inalterados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Beneficiário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Exemplo de atualização parcial. Os campos omitidos permanecem inalterados.",
            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = """
                    {"telefone":"11988887777"}
                    """)))
    public ResponseEntity<BeneficiarioResponseDTO> atualizarParcial(
            @Parameter(description = "Identificador do beneficiário", example = "1")
            @PathVariable Long idBeneficiario,
            @Valid @RequestBody BeneficiarioPatchDTO dto) {
        return ResponseEntity.ok(service.atualizarParcial(idBeneficiario, dto));
    }

    @DeleteMapping("/{idBeneficiario}")
    @Operation(summary = "Excluir beneficiário", description = "Remove o cadastro identificado pelo ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Beneficiário excluído"),
            @ApiResponse(responseCode = "404", description = "Beneficiário não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "Identificador do beneficiário", example = "1")
            @PathVariable Long idBeneficiario) {
        service.deletar(idBeneficiario);
        return ResponseEntity.noContent().build();
    }
}
