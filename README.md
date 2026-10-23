# SocialConnect API

> Solução web full stack desenvolvida na disciplina **Tópicos Especiais em Sistemas para Internet III**, Tecnologia em Sistemas para Internet, 2º semestre de 2026, vinculada ao projeto de extensão da disciplina.

## Sobre a solução

- **Problema que resolve:** proposta inicial de organizar o registro e o acompanhamento de doações e beneficiários em projetos sociais. O problema e o escopo serão validados com o parceiro externo na aula 15.
- **Público-alvo:** organizações e projetos sociais que recebem e distribuem doações; pessoas beneficiadas por essas iniciativas.
- **Status:** em construção — produto do semestre letivo

## Equipe

| Nome | GitHub | Responsabilidade |
|---|---|---|
| Roger Gabriel | [@Rockyzera](https://github.com/Rockyzera) | Desenvolvimento e documentação do projeto |

O projeto está sendo conduzido individualmente neste momento. A revisão de código será solicitada a um colega ou ao professor, conforme os acordos da disciplina.

## Acordos de trabalho

- **Canal de comunicação:** issues e pull requests do GitHub para registrar tarefas e decisões; dúvidas da disciplina serão encaminhadas pelo canal indicado pelo professor.
- **Encontros de trabalho:** organização individual, conforme o cronograma da disciplina.
- **Decisões técnicas:** registrar decisões relevantes no repositório; consultar o professor quando houver dúvida sobre os requisitos da atividade.
- **Revisão de código:** todo pull request precisa de pelo menos uma aprovação antes do merge. A pessoa revisora será definida conforme a disponibilidade de colegas ou orientação do professor. O feedback deve tratar do código, não da pessoa.
- **Commits:** pequenos, frequentes e com mensagem que explique o motivo da mudança. Padrão: `tipo: descrição`, por exemplo `feat: adiciona cadastro de doador #12`.
- **Definition of Done inicial:** implementação concluída, validações e testes essenciais passando, pull request revisado e aprovado, documentação atualizada e pipeline sem erros.

## Stack

| Camada | Tecnologia | Versão registrada |
|---|---|---|
| Back-end | Java 21, Spring Boot 4.1.1 e Maven Wrapper | Aula 03–04 |
| Front-end | React 19.3.1, TypeScript 6.0.3, Vite 8.3.4 e Axios 1.20.0 | Aula 09 |
| Banco de dados | H2 2.4.240 em memória (desenvolvimento) + Flyway | Aula 04 |
| DevOps | Git, Docker, Docker Compose e CI | [registrar na aula 13] |

As versões serão registradas no início de cada etapa e só serão alteradas mediante decisão da equipe documentada.

## Como executar

### Back-end

Pré-requisito: JDK 21. Na raiz do repositório, inicie a API com o Maven Wrapper:

```powershell
.\backend\mvnw.cmd -f .\backend\pom.xml spring-boot:run
```

A API fica disponível no prefixo `/api/v1`:

| Recurso | Operações | Parâmetros relevantes |
|---|---|---|
| `/beneficiarios` | Listar, cadastrar, atualizar com PUT/PATCH e excluir | A listagem aceita `nome`, `cpf`, `page`, `size` e `sort`; por padrão, 10 resultados por página, ordenados por nome. |
| `/beneficiarios/{idBeneficiario}` | Consultar por ID | — |
| `/doacoes` | Listar e cadastrar | A listagem aceita os filtros opcionais `dataInicio`, `dataFim` e `tipo`, além de `page`, `size` e `sort`. Sem ordenação explícita, usa data da doação decrescente e 10 itens por página. |

As listagens retornam uma página do Spring Data com `content`, `totalElements`, `totalPages` e os demais metadados de paginação. As datas dos filtros de doação devem usar o formato `AAAA-MM-DD`; os tipos disponíveis são `ALIMENTO`, `ROUPA` e `FINANCEIRA`.

A documentação OpenAPI e a interface Swagger UI ficam disponíveis, com a aplicação em execução, em `http://localhost:8080/swagger-ui.html`.

O banco H2 é temporário e usado para desenvolvimento; os dados são perdidos quando a aplicação é encerrada. Para inspecioná-lo, acesse `http://localhost:8080/h2-console` enquanto a aplicação estiver rodando e use:

| Campo | Valor |
|---|---|
| JDBC URL | `jdbc:h2:mem:socialconnectdb` |
| Usuário | `sa` |
| Senha | (vazia) |

O esquema é criado pela migration `V1__create_beneficiarios_table.sql`, gerenciada pelo Flyway. O Hibernate usa `ddl-auto=validate` para conferir o mapeamento JPA sem criar ou alterar tabelas.

### Testes

Na raiz do repositório, execute os testes automatizados com o Maven Wrapper:

```powershell
.\backend\mvnw.cmd -f .\backend\pom.xml test
```

Os testes de integração usam um banco H2 separado, configurado pelo perfil `test`.

### Front-end

Pré-requisitos: Node.js `^20.19.0` ou `>=22.12.0` e npm. Com o back-end em execução na porta 8080, abra outro terminal na raiz do repositório e execute:

```powershell
cd .\frontend
npm.cmd install
npm.cmd run dev
```

A interface ficará disponível em `http://localhost:5173`. O endereço da API pode ser alterado no arquivo `frontend/.env` usando `frontend/.env.example` como referência. O back-end permite CORS para a origem local do Vite (`http://localhost:5173`).

Para validar a compilação de produção do front-end, execute dentro de `frontend/`:

```powershell
npm.cmd run build
```

## Fluxo de trabalho Git

1. Toda tarefa começa como uma **issue** vinculada ao backlog.
2. Criar uma branch a partir de `main`, usando `feat/nome-curto`, `fix/nome-curto` ou `docs/nome-curto`.
3. Fazer commits pequenos que expliquem a mudança e referenciem a issue.
4. Abrir um **pull request** com descrição do que mudou, como validar e evidências quando aplicável.
5. A integração em `main` acontece depois da CI passar e de pelo menos uma aprovação. Não fazer push direto em `main`.

## Uso responsável de IA generativa

A IA pode ser usada como apoio, com revisão e validação da equipe:

- Declarar contribuições relevantes de IA na descrição do pull request.
- Registrar prompts significativos em `docs/extensao/registro-ia.md`, incluindo data, ferramenta, prompt e como a resposta foi validada.
- Não confiar cegamente nas respostas: revisar código, executar testes e verificar segurança antes do merge.

## Estrutura prevista do repositório

```text
├── README.md
├── .gitignore
├── docs/extensao/       # registro de IA; artefatos da extensão, aulas 15+
├── backend/             # API Spring Boot, aula 03+
├── frontend/            # aplicação React/Vite, aula 09+
└── docker-compose.yml   # ambiente integrado, aula 13+
```

As pastas e componentes serão criados nas etapas indicadas pelo cronograma, sem antecipar entregas de aulas futuras.

## Documentação e referências

- Plano de ensino da disciplina no AVA/Teams.
- [Spring Boot](https://docs.spring.io/spring-boot/), [React](https://react.dev/) e [Material UI](https://mui.com/).
- [OpenAPI Specification](https://spec.openapis.org/oas/latest.html) e [OWASP Top 10](https://owasp.org/www-project-top-ten/).

## Licença

A definir com o professor e o parceiro externo na aula 15, considerando o contexto do projeto de extensão.
