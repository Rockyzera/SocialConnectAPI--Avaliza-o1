package br.com.socialconnect.api.produtos.service;

import br.com.socialconnect.api.produtos.dto.ProdutoRequestDTO;
import br.com.socialconnect.api.produtos.dto.ProdutoResponseDTO;
import br.com.socialconnect.api.produtos.exception.EstoqueNegativoException;
import br.com.socialconnect.api.produtos.exception.ProdutoNomeDuplicadoException;
import br.com.socialconnect.api.produtos.model.CategoriaProduto;
import br.com.socialconnect.api.produtos.model.Produto;
import br.com.socialconnect.api.produtos.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Page<ProdutoResponseDTO> listar(String nome, CategoriaProduto categoria, Pageable pageable) {
        Page<Produto> page;

        if (nome != null && !nome.isBlank() && categoria != null) {
            page = repository.findByNomeContainingIgnoreCaseAndCategoria(nome, categoria, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else if (categoria != null) {
            page = repository.findByCategoria(categoria, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toResponseDTO);
    }

    public ProdutoResponseDTO buscarPorId(Long idProduto) {
        return repository.findById(idProduto)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + idProduto));
    }

    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        if (repository.existsByNome(dto.nome())) {
            throw new ProdutoNomeDuplicadoException(dto.nome());
        }

        if (dto.estoqueAtual() < dto.estoqueMinimo()) {
            throw new EstoqueNegativoException("Estoque atual não pode ser menor que estoque mínimo");
        }

        Produto entity = Produto.builder()
                .nome(dto.nome())
                .categoria(dto.categoria())
                .estoqueAtual(dto.estoqueAtual())
                .estoqueMinimo(dto.estoqueMinimo())
                .unidadeMedida(dto.unidadeMedida())
                .dataCadastro(LocalDate.now())
                .build();

        return toResponseDTO(repository.save(entity));
    }

    public ProdutoResponseDTO atualizar(Long idProduto, ProdutoRequestDTO dto) {
        Produto entity = repository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + idProduto));

        if (!entity.getNome().equals(dto.nome()) && repository.existsByNome(dto.nome())) {
            throw new ProdutoNomeDuplicadoException(dto.nome());
        }

        if (dto.estoqueAtual() < dto.estoqueMinimo()) {
            throw new EstoqueNegativoException("Estoque atual não pode ser menor que estoque mínimo");
        }

        entity.setNome(dto.nome());
        entity.setCategoria(dto.categoria());
        entity.setEstoqueAtual(dto.estoqueAtual());
        entity.setEstoqueMinimo(dto.estoqueMinimo());
        entity.setUnidadeMedida(dto.unidadeMedida());

        return toResponseDTO(repository.save(entity));
    }

    public void deletar(Long idProduto) {
        if (!repository.existsById(idProduto)) {
            throw new RuntimeException("Produto não encontrado: " + idProduto);
        }
        repository.deleteById(idProduto);
    }

    private ProdutoResponseDTO toResponseDTO(Produto e) {
        boolean estoqueBaixo = e.getEstoqueAtual() < e.getEstoqueMinimo();
        return new ProdutoResponseDTO(
                e.getIdProduto(),
                e.getNome(),
                e.getCategoria(),
                e.getEstoqueAtual(),
                e.getEstoqueMinimo(),
                e.getUnidadeMedida(),
                e.getDataCadastro(),
                estoqueBaixo
        );
    }
}