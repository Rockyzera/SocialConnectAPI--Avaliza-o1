# SocialConnect API

> API RESTful de gestão para instituições sociais (ONGs, bancos de alimentos, CRAS, abrigos). Conecta doadores, voluntários e beneficiários.

**Disciplina:** Tópicos Especiais em Sistemas para Internet III  
**Stack:** Java 21 · Spring Boot 4.1.1 · JPA · H2 (dev) · PostgreSQL (prod)

---

## Como Rodar

### Pré-requisitos

- JDK 21 LTS ([Adoptium](https://adoptium.net/))
- Maven 3.9+ (ou use o wrapper: `./mvnw`)
- IDE: IntelliJ IDEA (recomendado) ou VS Code

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repo>
cd socialconnect-api

# 2. Compile o projeto
mvn clean compile

# 3. Rode a aplicação
mvn spring-boot:run
```

A aplicação sobe em `http://localhost:8080`

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:socialconnectdb`, user: `sa`, senha vazia)

---

## Entrega da Avaliação A1

### Passo a passo:

1. **Crie uma nova branch `avaliacao1`** no seu repositório público (socialconnect) GitHub pessoal
2. **Implemente todos os requisitos** no módulo `produtos`
3. **Rode `mvn clean compile`** e garanta que o projeto compila sem erros
4. **Verifique o Swagger** em `http://localhost:8080/swagger-ui.html`
5. **Commits descritivos:** Use Conventional Commits (ex: `feat(produtos): implementa CRUD`)
6. **Push para o seu repositório pessoal**
7. **Envie o link do repositório** via atividade no Teams

## Endpoints Principais

### Módulo Produtos (`/api/v1/produtos`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/produtos` | Lista produtos (paginado, filtros: `nome`, `categoria`) |
| `GET` | `/api/v1/produtos/{idProduto}` | Busca produto por ID |
| `POST` | `/api/v1/produtos` | Cria novo produto (retorna `201 Created` + `Location`) |
| `PUT` | `/api/v1/produtos/{idProduto}` | Atualização total do produto |
| `DELETE` | `/api/v1/produtos/{idProduto}` | Remove produto |

#### Exemplo de Criação (POST)

```json
{
  "nome": "Arroz 5kg",
  "categoria": "ALIMENTO",
  "estoqueAtual": 50,
  "estoqueMinimo": 10,
  "unidadeMedida": "unidade"
}
```

#### Exemplo de Resposta (ProdutoResponseDTO)

```json
{
  "idProduto": 1,
  "nome": "Arroz 5kg",
  "categoria": "ALIMENTO",
  "estoqueAtual": 3,
  "estoqueMinimo": 10,
  "unidadeMedida": "unidade",
  "dataCadastro": "2026-09-18",
  "estoqueBaixo": true
}
```

#### Códigos de Erro (Problem Details - RFC 7807)

| Status | Código | Quando ocorre |
|--------|--------|---------------|
| 400 | - | Validação Bean Validation falhou |
| 404 | `PRODUTO_NAO_ENCONTRADO` | ID inexistente |
| 409 | `PRODUTO_NOME_DUPLICADO` | Nome já cadastrado |
| 422 | `ESTOQUE_NEGATIVO` | `estoqueAtual < estoqueMinimo` |

---

### Módulo Beneficiários (`/api/v1/beneficiarios`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/beneficiarios` | Lista beneficiários (paginado, filtros: `nome`, `cpf`) |
| `GET` | `/api/v1/beneficiarios/{idBeneficiario}` | Busca por ID |
| `POST` | `/api/v1/beneficiarios` | Cria beneficiário |
| `PUT` | `/api/v1/beneficiarios/{idBeneficiario}` | Atualização total |
| `PATCH` | `/api/v1/beneficiarios/{idBeneficiario}` | Atualização parcial |
| `DELETE` | `/api/v1/beneficiarios/{idBeneficiario}` | Remove |

---

### Módulo Doadores (`/api/v1/doadores`)

| Método | Endpoint |
|--------|----------|
| `GET` | `/api/v1/doadores` |
| `GET` | `/api/v1/doadores/{idDoador}` |
| `POST` | `/api/v1/doadores` |
| `PUT` | `/api/v1/doadores/{idDoador}` |
| `PATCH` | `/api/v1/doadores/{idDoador}` |
| `DELETE` | `/api/v1/doadores/{idDoador}` |

---

### Módulo Doações (`/api/v1/doacoes`)

| Método | Endpoint |
|--------|----------|
| `GET` | `/api/v1/doacoes` |
| `GET` | `/api/v1/doacoes/{idDoacao}` |
| `POST` | `/api/v1/doacoes` |
| `PUT` | `/api/v1/doacoes/{idDoacao}` |
| `PATCH` | `/api/v1/doacoes/{idDoacao}` |
| `DELETE` | `/api/v1/doacoes/{idDoacao}` |

---

## Estrutura do Projeto

```
src/main/java/br/com/socialconnect/api/
├── ApiApplication.java
├── config/
│   └── FlywayConfig.java
├── beneficiarios/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── doadores/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
├── doacoes/
│   ├── controller/
│   ├── dto/
│   ├── model/
│   ├── repository/
│   └── service/
└── produtos/
    ├── controller/
    ├── dto/
    ├── exception/
    ├── model/
    ├── repository/
    ├── service/
    └── validation/
```

---

## Tecnologias e Padrões

- **Spring Boot 4.1.1** (Java 21)
- **Spring Data JPA** + **Hibernate**
- **Flyway** para migrações (padrão `id_` nas PKs: `id_produto`, `id_beneficiario`, etc.)
- **Bean Validation** (Hibernate Validator)
- **SpringDoc OpenAPI 3** (Swagger UI)
- **Problem Details** (RFC 7807) para erros padronizados
- **Conventional Commits** (`feat(produtos): ...`, `fix(service): ...`)



## Testes

```bash
# Rodar todos os testes
mvn test

# Rodar apenas testes de integração (requer Docker para Testcontainers)
mvn test -Dtest=*IntegrationTest
```

---

## Declaração de Uso de IA (A1)

Conforme política da disciplina (arquivo `AI_USAGE.md`), o uso de IA é **permitido e incentivado** com transparência total.

### Ferramentas utilizadas:
- [x] ChatGPT / Claude / Gemini
- [ ] Copilot / Codeium
- [ ] Nenhuma

### Como utilizei:
- Usei IA para gerar a estrutura inicial dos DTOs e Entity.
- Usei IA para configurar as anotações Swagger/OpenAPI.
- Usei IA para revisar a implementação das regras de negócio (estoque negativo, nome único).
- Usei IA para auxiliar coma a criação do README e documentação.

### O que eu entendo 100%:
- A lógica de validação de estoque negativo (422 Unprocessable Entity).
- O padrão de chave primária `id_` nas migrações Flyway.
- A semântica HTTP (201 Created + Location, 204 No Content, 409 Conflict, 422 Unprocessable Entity).
- Problem Details (RFC 7807) para padronização de erros.
- Injeção de dependência via construtor com campos `final`.
- Padrão de pacotes em camadas (controller → service → repository).

### O que precisei estudar mais:
- Configuração do SpringDoc OpenAPI 3 para Swagger UI.
- Criação de validações customizadas Bean Validation (`@EstoqueNaoNegativo`).
- Uso de `ProblemDetail` para respostas de erro padronizadas.

---

## Licença

Projeto acadêmico - Fatec - Disciplina ITE005