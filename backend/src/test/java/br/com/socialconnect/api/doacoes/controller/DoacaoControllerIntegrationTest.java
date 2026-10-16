package br.com.socialconnect.api.doacoes.controller;

import br.com.socialconnect.api.doacoes.dto.DoacaoRequestDTO;
import br.com.socialconnect.api.doacoes.dto.DoacaoResponseDTO;
import br.com.socialconnect.api.doacoes.model.Doacao;
import br.com.socialconnect.api.doacoes.model.TipoDoacao;
import br.com.socialconnect.api.doacoes.repository.DoacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
class DoacaoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DoacaoRepository repository;

    @BeforeEach
    void limparBancoDeTeste() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Deve cadastrar doação válida e responder 201 Created")
    void deveCriarDoacaoQuandoDadosValidos() {
        // ARRANGE
        DoacaoRequestDTO request = new DoacaoRequestDTO(
                1L,
                LocalDate.now().minusDays(1),
                new BigDecimal("100.00"),
                TipoDoacao.ALIMENTO,
                "Cestas básicas"
        );

        // ACT
        ResponseEntity<DoacaoResponseDTO> resposta = restTemplate.postForEntity(
                "/api/v1/doacoes", request, DoacaoResponseDTO.class);

        // ASSERT
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resposta.getBody()).isNotNull();
        assertThat(resposta.getBody().idDoacao()).isNotNull();
        assertThat(resposta.getHeaders().getLocation()).isNotNull();
    }

    @Test
    @DisplayName("Deve responder 400 quando a data da doação estiver no futuro")
    void deveRetornar400QuandoDataFutura() {
        // ARRANGE
        DoacaoRequestDTO request = new DoacaoRequestDTO(
                1L,
                LocalDate.now().plusDays(10),
                new BigDecimal("100.00"),
                TipoDoacao.ALIMENTO,
                "Doação inválida para teste"
        );

        // ACT
        ResponseEntity<String> resposta = restTemplate.postForEntity(
                "/api/v1/doacoes", request, String.class);

        // ASSERT
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resposta.getBody()).contains("futuro");
    }

    @Test
    @DisplayName("Deve listar doações cadastradas com paginação")
    void deveListarDoacoesCadastradas() {
        // ARRANGE
        repository.save(Doacao.builder()
                .idDoador(1L)
                .dataDoacao(LocalDate.now().minusDays(1))
                .tipo(TipoDoacao.ROUPA)
                .descricao("Roupas de inverno")
                .build());

        // ACT
        ResponseEntity<String> resposta = restTemplate.getForEntity(
                "/api/v1/doacoes?page=0&size=10", String.class);

        // ASSERT
        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).contains("\"tipo\":\"ROUPA\"", "\"totalElements\":1");
    }
}
