# Registro de uso de IA

Este registro documenta contribuições relevantes de IA usadas no projeto, conforme o acordo de uso responsável descrito no README.

## 2026-10-16 — Interface React/TypeScript e integração com a API (Aula 09)

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** auxiliar na construção da interface da Aula 09 com React, TypeScript, Vite e Axios, integrando a listagem e o cadastro de beneficiários à API existente.
- **Uso da resposta:** Roger Gabriel definiu o escopo conforme o material da aula, acompanhou e supervisionou as decisões e revisará as alterações. Sob essa orientação, o Codex, como assistente, preparou os componentes de listagem e formulário, os tipos e serviços Axios, a configuração CORS e as instruções de execução; também analisou e corrigiu possíveis problemas durante a implementação.
- **Validação:** a compilação de produção do front-end (`npm run build`), o lint (`npm run lint`) e a compilação do back-end com Maven (`-DskipTests compile`) foram concluídos com sucesso. A validação manual integrada no navegador ainda está pendente.

## 2026-10-23 — Máscaras visuais para CPF/CNPJ e telefone

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** aplicar máscaras visuais progressivas para CPF/CNPJ e telefone no front-end, sem gravar pontuação ou espaços no banco.
- **Uso da resposta:** Roger Gabriel solicitou o comportamento visual e definiu que os dados persistidos deveriam continuar sem formatação. Sob sua orientação e supervisão, o Codex, como assistente, criou funções de formatação compartilhadas entre formulário e listagem e normalizou os valores para apenas dígitos antes do envio à API.
- **Validação:** `npm run build` e `npm run lint` concluíram sem erros. Roger conferiu manualmente as máscaras na interface e confirmou no H2 que CPF e telefone foram persistidos sem pontuação.
## 2026-10-16 — Desafio de filtros opcionais para doações

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** analisar por que os filtros opcionais da listagem de doações não funcionavam individualmente, corrigir a lógica, ampliar os testes e atualizar a documentação.
- **Uso da resposta:** o Codex atuou como assistente na análise e correção da consulta, na atualização do controller e na preparação dos testes e da documentação. Roger Gabriel definiu o escopo, supervisionou o trabalho, revisou as alterações e orientou os ajustes.
- **Validação:** a suíte Maven terminou com `BUILD SUCCESS`, com 11 testes executados sem falhas. As alterações foram revisadas por Roger e integradas pela PR #13.

## 2026-10-16 — Testes do módulo de doações conforme a Aula 07

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** auxiliar na implementação do fluxo de doações e na criação/revisão dos testes unitários e de integração, seguindo a base atual do professor.
- **Uso da resposta:** o Codex atuou como assistente na implementação do módulo, na análise de problemas e na preparação dos testes. Roger definiu a adaptação ao perfil H2 existente e à ausência de `DoadorRepository`, supervisionou as mudanças e revisou o resultado.
- **Validação:** a execução de `mvn test` terminou com `BUILD SUCCESS`, com seis testes executados sem falhas na entrega da Aula 07. A implementação foi integrada pela PR #11.

## 2026-10-16 — Validações e documentação da API conforme a Aula 06

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** auxiliar na aplicação de Bean Validation, no tratamento padronizado de erros, nas mensagens internacionalizadas e na documentação OpenAPI/Swagger; analisar também os erros encontrados durante a execução.
- **Uso da resposta:** o Codex atuou como assistente na análise e correção de problemas e na implementação das validações, do tratamento de erros e da documentação. Roger definiu os requisitos, supervisionou o trabalho, conferiu os endpoints e orientou os ajustes.
- **Validação:** a compilação limpa terminou com sucesso; Swagger UI e `/v3/api-docs` responderam com HTTP 200, e requisições inválidas retornaram HTTP 400 com mensagens conforme `Accept-Language`. As alterações foram integradas pela PR #9.

## 2026-10-16 — Evolução da API de beneficiários conforme a Aula 05

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** auxiliar na separação dos DTOs e nas mudanças necessárias para paginação, filtros, PUT/PATCH e respostas HTTP para beneficiário inexistente ou CPF duplicado, seguindo a Aula 05.
- **Uso da resposta:** o Codex atuou como assistente na criação dos DTOs, na análise de boas práticas e na correção das mudanças no service, controller, repository e README. Roger Gabriel definiu os requisitos, supervisionou o trabalho, orientou os ajustes e revisou as respostas e alterações.
- **Validação:** Roger acompanhou e verificou manualmente o cadastro, a paginação, os filtros por nome e CPF, PUT, PATCH, conflito de CPF (409), busca por ID inexistente (404) e exclusão. A compilação Maven terminou com `BUILD SUCCESS`; os testes automatizados não foram executados.

## 2026-10-15 — API inicial de beneficiários e persistência

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** orientar a implementação inicial da API de beneficiários conforme as Aulas 03 e 04, analisar boas práticas e ajudar a corrigir problemas de execução relacionados a Lombok, Flyway e H2.
- **Uso da resposta:** Roger Gabriel iniciou a estrutura do projeto, definiu o escopo e supervisionou o trabalho. O Codex atuou como assistente na análise e correção de problemas relacionados a Lombok, Flyway e H2 e, conforme solicitado, preparou as primeiras versões da entidade `Beneficiario`, do DTO, repository, service e controller, da configuração de persistência e das instruções de execução. Roger revisou as alterações.
- **Validação:** a compilação limpa foi concluída com JDK 21. Roger conferiu no H2 Console que a migration V1 foi registrada com `success = TRUE` e que a tabela `BENEFICIARIOS` existe. Os testes automatizados ainda não foram executados.

## 2026-10-15 — Organização inicial do repositório

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** analisar os materiais da disciplina e preparar a estrutura inicial do repositório, com issue e documentação, sem inventar informações da equipe.
- **Uso da resposta:** o Codex atuou como assistente na análise dos materiais e na redação da issue #1, do README e do `.gitignore`, seguindo as orientações de Roger, que supervisionou e revisou o conteúdo.
- **Validação:** comparei o conteúdo com `README.md` e `Estrutura_Inicial_Repo.md` fornecidos para a disciplina; os dados de autoria foram confirmados pelo responsável pelo projeto e as informações ainda não decididas ficaram marcadas como pendentes. O `.gitignore` foi revisado para manter fora do Git artefatos de build, IDE e arquivos locais de ambiente.
