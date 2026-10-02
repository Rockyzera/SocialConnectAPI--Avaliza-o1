package br.com.socialconnect.api.doacoes.repository;

import br.com.socialconnect.api.doacoes.model.Doacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    Page<Doacao> findByDataDoacaoBetweenAndTipo(LocalDate dataInicio, LocalDate dataFim, String tipo, Pageable pageable);

    Page<Doacao> findByDataDoacaoBetween(LocalDate dataInicio, LocalDate dataFim, Pageable pageable);

    Page<Doacao> findByTipo(String tipo, Pageable pageable);

    Page<Doacao> findByDoadorIdDoador(Long idDoador, Pageable pageable);
}