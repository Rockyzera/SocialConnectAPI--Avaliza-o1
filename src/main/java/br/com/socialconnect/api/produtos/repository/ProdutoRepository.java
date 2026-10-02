package br.com.socialconnect.api.produtos.repository;

import br.com.socialconnect.api.produtos.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Produto> findByCategoria(br.com.socialconnect.api.produtos.model.CategoriaProduto categoria, Pageable pageable);

    Page<Produto> findByNomeContainingIgnoreCaseAndCategoria(String nome, br.com.socialconnect.api.produtos.model.CategoriaProduto categoria, Pageable pageable);

    boolean existsByNome(String nome);
}