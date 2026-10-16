package br.com.socialconnect.api.doacoes.service;

import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DoacaoService {

    private final DoacaoRepository repository;

    public DoacaoService(DoacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DoacaoResponseDTO criar(DoacaoRequestDTO dto) {
        Doacao doacao = Doacao.builder()
                .idDoador(dto.idDoador())
                .dataDoacao(dto.dataDoacao())
                .valor(dto.valor())
                .tipo(dto.tipo())
                .descricao(dto.descricao())
                .build();

        return toResponseDTO(repository.save(doacao));
    }

    @Transactional(readOnly = true)
    public Page<DoacaoResponseDTO> listar(
            LocalDate dataInicio,
            LocalDate dataFim,
            TipoDoacao tipo,
            Pageable pageable) {
        Page<Doacao> doacoes;

        if (dataInicio != null && dataFim != null && tipo != null) {
            doacoes = repository.findByDataDoacaoBetweenAndTipo(
                    dataInicio, dataFim, tipo, pageable);
        } else {
            doacoes = repository.findAll(pageable);
        }

        return doacoes.map(this::toResponseDTO);
    }

    private DoacaoResponseDTO toResponseDTO(Doacao doacao) {
        return new DoacaoResponseDTO(
                doacao.getIdDoacao(),
                doacao.getIdDoador(),
                doacao.getDataDoacao(),
                doacao.getValor(),
                doacao.getTipo()
        );
    }
}
