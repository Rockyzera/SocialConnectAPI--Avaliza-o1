package br.com.socialconnect.api.doadores.service;

import br.com.socialconnect.api.doadores.dto.*;
import br.com.socialconnect.api.doadores.model.Doador;
import br.com.socialconnect.api.doadores.repository.DoadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoadorService {

    private final DoadorRepository repository;

    public DoadorService(DoadorRepository repository) {
        this.repository = repository;
    }

    public Page<DoadorResponseDTO> listar(String nome, String cpfCnpj, String email, Pageable pageable) {
        Page<Doador> page;

        if (cpfCnpj != null && !cpfCnpj.isBlank()) {
            page = repository.findByCpfCnpj(cpfCnpj, pageable);
        } else if (email != null && !email.isBlank()) {
            page = repository.findByEmail(email, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    public DoadorResponseDTO buscarPorId(Long idDoador) {
        return repository.findById(idDoador)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Doador não encontrado: " + idDoador));
    }

    public DoadorResponseDTO criar(DoadorRequestDTO dto) {
        if (repository.existsByCpfCnpj(dto.cpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }
        if (repository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }
        Doador entity = Doador.builder()
                .nome(dto.nome())
                .cpfCnpj(dto.cpfCnpj())
                .email(dto.email())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .tipoPessoa(dto.tipoPessoa())
                .dataCadastro(LocalDate.now())
                .build();
        return toResponseDTO(repository.save(entity));
    }

    public DoadorResponseDTO atualizar(Long idDoador, DoadorRequestDTO dto) {
        Doador entity = repository.findById(idDoador)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));

        if (!entity.getCpfCnpj().equals(dto.cpfCnpj()) && repository.existsByCpfCnpj(dto.cpfCnpj())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }
        if (!entity.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        entity.setNome(dto.nome());
        entity.setCpfCnpj(dto.cpfCnpj());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
        entity.setEndereco(dto.endereco());
        entity.setTipoPessoa(dto.tipoPessoa());

        return toResponseDTO(repository.save(entity));
    }

    public DoadorResponseDTO atualizarParcial(Long idDoador, DoadorPatchDTO dto) {
        Doador entity = repository.findById(idDoador)
                .orElseThrow(() -> new RuntimeException("Não encontrado"));

        if (dto.nome() != null) entity.setNome(dto.nome());
        if (dto.telefone() != null) entity.setTelefone(dto.telefone());
        if (dto.endereco() != null) entity.setEndereco(dto.endereco());
        if (dto.email() != null) {
            if (!entity.getEmail().equals(dto.email()) && repository.existsByEmail(dto.email())) {
                throw new RuntimeException("Email já cadastrado");
            }
            entity.setEmail(dto.email());
        }

        return toResponseDTO(repository.save(entity));
    }

    public void deletar(Long idDoador) {
        repository.deleteById(idDoador);
    }

    private DoadorResponseDTO toResponseDTO(Doador e) {
        return new DoadorResponseDTO(
                e.getIdDoador(), e.getNome(), e.getCpfCnpj(), e.getEmail(),
                e.getTelefone(), e.getEndereco(), e.getTipoPessoa(), e.getDataCadastro()
        );
    }
}