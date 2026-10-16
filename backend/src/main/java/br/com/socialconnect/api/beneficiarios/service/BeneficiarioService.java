package br.com.socialconnect.api.beneficiarios.service;

import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioRequestDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioResponseDTO;
import br.com.socialconnect.api.beneficiarios.dto.BeneficiarioPatchDTO;
import br.com.socialconnect.api.beneficiarios.model.Beneficiario;
import br.com.socialconnect.api.beneficiarios.repository.BeneficiarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class BeneficiarioService {

    private final BeneficiarioRepository repository;

    public BeneficiarioService(BeneficiarioRepository repository) {
        this.repository = repository;
    }

    public Page<BeneficiarioResponseDTO> listar(String nome, String cpf, Pageable pageable) {
        Page<Beneficiario> beneficiarios;

        if (cpf != null && !cpf.isBlank()) {
            beneficiarios = repository.findByCpf(cpf, pageable);
        } else if (nome != null && !nome.isBlank()) {
            beneficiarios = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            beneficiarios = repository.findAll(pageable);
        }

        return beneficiarios.map(this::toResponseDTO);
    }

    public BeneficiarioResponseDTO buscarPorId(Long idBeneficiario) {
        return toResponseDTO(obterPorId(idBeneficiario));
    }

    public BeneficiarioResponseDTO salvar(BeneficiarioRequestDTO dto) {
        verificarCpfDisponivel(dto.cpf(), null);
        Beneficiario beneficiario = toEntity(dto);
        beneficiario = repository.save(beneficiario);
        return toResponseDTO(beneficiario);
    }

    public BeneficiarioResponseDTO atualizar(
            Long idBeneficiario,
            BeneficiarioRequestDTO dto) {
        Beneficiario beneficiario = obterPorId(idBeneficiario);
        verificarCpfDisponivel(dto.cpf(), idBeneficiario);

        beneficiario.setNome(dto.nome());
        beneficiario.setCpf(dto.cpf());
        beneficiario.setTelefone(dto.telefone());
        beneficiario.setEndereco(dto.endereco());
        beneficiario.setSituacaoVulnerabilidade(dto.situacaoVulnerabilidade());

        return toResponseDTO(repository.save(beneficiario));
    }

    public BeneficiarioResponseDTO atualizarParcial(
            Long idBeneficiario,
            BeneficiarioPatchDTO dto) {
        Beneficiario beneficiario = obterPorId(idBeneficiario);

        if (dto.nome() != null) {
            beneficiario.setNome(dto.nome());
        }
        if (dto.telefone() != null) {
            beneficiario.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            beneficiario.setEndereco(dto.endereco());
        }
        if (dto.situacaoVulnerabilidade() != null) {
            beneficiario.setSituacaoVulnerabilidade(dto.situacaoVulnerabilidade());
        }

        return toResponseDTO(repository.save(beneficiario));
    }

    public void deletar(Long idBeneficiario) {
        obterPorId(idBeneficiario);
        repository.deleteById(idBeneficiario);
    }

    private Beneficiario obterPorId(Long idBeneficiario) {
        return repository.findById(idBeneficiario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Beneficiário não encontrado com o ID: " + idBeneficiario));
    }

    private void verificarCpfDisponivel(String cpf, Long idBeneficiarioAtual) {
        boolean cpfEmUso = idBeneficiarioAtual == null
                ? repository.existsByCpf(cpf)
                : repository.existsByCpfAndIdBeneficiarioNot(cpf, idBeneficiarioAtual);

        if (cpfEmUso) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe um beneficiário cadastrado com este CPF.");
        }
    }

    private BeneficiarioResponseDTO toResponseDTO(Beneficiario entity) {
        return new BeneficiarioResponseDTO(
                entity.getIdBeneficiario(),
                entity.getNome(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getEndereco(),
                entity.getSituacaoVulnerabilidade(),
                entity.getDataCadastro());
    }

    private Beneficiario toEntity(BeneficiarioRequestDTO dto) {
        return Beneficiario.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .telefone(dto.telefone())
                .endereco(dto.endereco())
                .situacaoVulnerabilidade(dto.situacaoVulnerabilidade())
                .dataCadastro(LocalDate.now())
                .build();
    }
}
