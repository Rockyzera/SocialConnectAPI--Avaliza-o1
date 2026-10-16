package br.com.socialconnect.api.doacoes.service;

import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoacaoServiceTest {

    @Mock
    private DoacaoRepository repository;

    @InjectMocks
    private DoacaoService service;

    @Test
    @DisplayName("Deve criar doação quando os dados são válidos")
    void deveCriarDoacaoQuandoDadosValidos() {
        // ARRANGE
        LocalDate dataDoacao = LocalDate.now().minusDays(1);
        DoacaoRequestDTO dto = new DoacaoRequestDTO(
                1L, dataDoacao, new BigDecimal("100.00"), TipoDoacao.ALIMENTO, "Cestas básicas");
        Doacao doacaoSalva = Doacao.builder()
                .idDoacao(1L)
                .idDoador(1L)
                .dataDoacao(dataDoacao)
                .valor(new BigDecimal("100.00"))
                .tipo(TipoDoacao.ALIMENTO)
                .descricao("Cestas básicas")
                .build();
        when(repository.save(any(Doacao.class))).thenReturn(doacaoSalva);

        // ACT
        DoacaoResponseDTO resultado = service.criar(dto);

        // ASSERT
        assertNotNull(resultado.idDoacao());
        assertEquals(1L, resultado.idDoador());
        assertEquals(dataDoacao, resultado.dataDoacao());
        assertEquals(new BigDecimal("100.00"), resultado.valor());
        assertEquals(TipoDoacao.ALIMENTO, resultado.tipo());
        verify(repository).save(any(Doacao.class));
    }

    @Test
    @DisplayName("Deve listar doações em páginas")
    void deveListarDoacoesComPaginacao() {
        // ARRANGE
        LocalDate dataDoacao = LocalDate.now().minusDays(1);
        Doacao doacao = Doacao.builder()
                .idDoacao(1L)
                .idDoador(1L)
                .dataDoacao(dataDoacao)
                .tipo(TipoDoacao.ROUPA)
                .build();
        PageRequest pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(doacao), pageable, 1));

        // ACT
        var resultado = service.listar(null, null, null, pageable);

        // ASSERT
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1L, resultado.getContent().getFirst().idDoacao());
        assertEquals(TipoDoacao.ROUPA, resultado.getContent().getFirst().tipo());
        verify(repository).findAll(pageable);
    }
}
