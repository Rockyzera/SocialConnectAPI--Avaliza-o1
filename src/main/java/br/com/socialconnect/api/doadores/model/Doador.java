package br.com.socialconnect.api.doadores.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "doadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doador")
    private Long idDoador;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "cpf_cnpj", nullable = false, unique = true, length = 18)
    private String cpfCnpj;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 255)
    private String endereco;

    @Column(name = "tipo_pessoa", nullable = false, length = 1)
    private String tipoPessoa;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;
}