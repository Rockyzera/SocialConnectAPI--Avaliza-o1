package br.com.socialconnect.api.doadores.controller;

import br.com.socialconnect.api.doadores.dto.*;
import br.com.socialconnect.api.doadores.service.DoadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/doadores")
public class DoadorController {

    private final DoadorService service;

    public DoadorController(DoadorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<DoadorResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listar(nome, cpfCnpj, email, pageable));
    }

    @GetMapping("/{idDoador}")
    public ResponseEntity<DoadorResponseDTO> buscarPorId(@PathVariable Long idDoador) {
        return ResponseEntity.ok(service.buscarPorId(idDoador));
    }

    @PostMapping
    public ResponseEntity<DoadorResponseDTO> criar(@Valid @RequestBody DoadorRequestDTO dto) {
        DoadorResponseDTO salvo = service.criar(dto);
        URI location = URI.create("/api/v1/doadores/" + salvo.idDoador());
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{idDoador}")
    public ResponseEntity<DoadorResponseDTO> atualizar(
            @PathVariable Long idDoador,
            @Valid @RequestBody DoadorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(idDoador, dto));
    }

    @PatchMapping("/{idDoador}")
    public ResponseEntity<DoadorResponseDTO> atualizarParcial(
            @PathVariable Long idDoador,
            @RequestBody DoadorPatchDTO dto) {
        return ResponseEntity.ok(service.atualizarParcial(idDoador, dto));
    }

    @DeleteMapping("/{idDoador}")
    public ResponseEntity<Void> deletar(@PathVariable Long idDoador) {
        service.deletar(idDoador);
        return ResponseEntity.noContent().build();
    }
}