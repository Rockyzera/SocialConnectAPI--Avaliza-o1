package br.com.socialconnect.api.doacoes.service;

import br.com.socialconnect.api.doacoes.dto.*;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import br.com.socialconnect.api.doadores.model.Doador;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoacaoService {

    private final DoacaoRepository repository;
    private final DoadorRepository doadorRepository;

    public DoacaoService(DoacaoRepository repository, DoadorRepository doadorRepository) {
        this.repository = repository;
        this.doadorRepository = doadorRepository;
    }

    public Page<DoacaoResponseDTO> listar(LocalDate dataInicio, LocalDate dataFim, String tipo, Pageable pageable) {
        Page<Doacao> page;

        if (dataInicio != null && dataFim != null && tipo != null && !tipo.isBlank()) {
            page = repository.findByDataDoacaoBetweenAndTipo(dataInicio, dataFim, tipo, pageable);
        } else if (dataInicio != null && dataFim != null) {
            page = repository.findByDataDoacaoBetween(dataInicio, dataFim, pageable);
        } else if (tipo != null && !tipo.isBlank()) {
            page = repository.findByTipo(tipo, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    public DoacaoResponseDTO buscarPorId(Long idDoacao) {
        return repository.findById(idDoacao)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Doação não encontrada: " + idDoacao));
    }

    public DoacaoResponseDTO criar(DoacaoRequestDTO dto) {
        Doador doador = doadorRepository.findById(dto.idDoador())
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + dto.idDoador()));

        if (!isValidTipoDoacao(dto.tipo())) {
            throw new RuntimeException("Tipo de doação inválido: " + dto.tipo());
        }

        Doacao entity = Doacao.builder()
                .doador(doador)
                .dataDoacao(dto.dataDoacao())
                .valor(dto.valor())
                .tipo(dto.tipo())
                .descricao(dto.descricao())
                .build();
        return toResponseDTO(repository.save(entity));
    }

    public DoacaoResponseDTO atualizar(Long idDoacao, DoacaoRequestDTO dto) {
        Doacao entity = repository.findById(idDoacao)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));

        Doador doador = doadorRepository.findById(dto.idDoador())
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + dto.idDoador()));

        if (!isValidTipoDoacao(dto.tipo())) {
            throw new RuntimeException("Tipo de doação inválido: " + dto.tipo());
        }

        entity.setDoador(doador);
        entity.setDataDoacao(dto.dataDoacao());
        entity.setValor(dto.valor());
        entity.setTipo(dto.tipo());
        entity.setDescricao(dto.descricao());

        return toResponseDTO(repository.save(entity));
    }

    public DoacaoResponseDTO atualizarParcial(Long idDoacao, DoacaoPatchDTO dto) {
        Doacao entity = repository.findById(idDoacao)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));

        if (dto.dataDoacao() != null) entity.setDataDoacao(dto.dataDoacao());
        if (dto.valor() != null) entity.setValor(dto.valor());
        if (dto.tipo() != null) {
            if (!isValidTipoDoacao(dto.tipo())) {
                throw new RuntimeException("Tipo de doação inválido: " + dto.tipo());
            }
            entity.setTipo(dto.tipo());
        }
        if (dto.descricao() != null) entity.setDescricao(dto.descricao());

        return toResponseDTO(repository.save(entity));
    }

    public void deletar(Long idDoacao) {
        repository.deleteById(idDoacao);
    }

    private boolean isValidTipoDoacao(String tipo) {
        try {
            TipoDoacao.valueOf(tipo.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private DoacaoResponseDTO toResponseDTO(Doacao e) {
        return new DoacaoResponseDTO(
                e.getIdDoacao(),
                e.getDoador().getIdDoador(),
                e.getDataDoacao(),
                e.getValor(),
                TipoDoacao.valueOf(e.getTipo()),
                e.getDescricao()
        );
    }
}