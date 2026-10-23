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

## 2026-10-16 — Evolução da API de beneficiários conforme a Aula 05

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** auxiliar na separação dos DTOs e nas mudanças necessárias para paginação, filtros, PUT/PATCH e respostas HTTP para beneficiário inexistente ou CPF duplicado, seguindo a Aula 05.
- **Uso da resposta:** Roger Gabriel definiu os requisitos, orientou as mudanças esperadas e acompanhou o trabalho. O Codex auxiliou na criação dos DTOs e aplicou, sob essa orientação, as alterações correspondentes no service, controller, repository e README.
- **Validação:** Roger acompanhou e verificou manualmente o cadastro, a paginação, os filtros por nome e CPF, PUT, PATCH, conflito de CPF (409), busca por ID inexistente (404) e exclusão. A compilação Maven terminou com `BUILD SUCCESS`; os testes automatizados não foram executados.

## 2026-10-15 — API inicial de beneficiários e persistência

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** orientar a implementação inicial da API de beneficiários conforme as Aulas 03 e 04, analisar boas práticas e ajudar a corrigir problemas de execução relacionados a Lombok, Flyway e H2.
- **Uso da resposta:** Roger Gabriel iniciou a estrutura do projeto, definiu o escopo e revisou as alterações. A pedido dele, o Codex também gerou as primeiras versões da entidade `Beneficiario`, do DTO, repository, service e controller; adicionou a configuração de H2/Flyway e a migration V1; e atualizou as instruções de execução.
- **Validação:** a compilação limpa foi concluída com JDK 21. Roger conferiu no H2 Console que a migration V1 foi registrada com `success = TRUE` e que a tabela `BENEFICIARIOS` existe. Os testes automatizados ainda não foram executados.

## 2026-10-15 — Organização inicial do repositório

- **Ferramenta:** OpenAI Codex.
- **Prompt resumido:** analisar os materiais da disciplina e preparar a estrutura inicial do repositório, com issue e documentação, sem inventar informações da equipe.
- **Uso da resposta:** apoio na redação da issue #1, do README e do `.gitignore`.
- **Validação:** comparei o conteúdo com `README.md` e `Estrutura_Inicial_Repo.md` fornecidos para a disciplina; os dados de autoria foram confirmados pelo responsável pelo projeto e as informações ainda não decididas ficaram marcadas como pendentes. O `.gitignore` foi revisado para manter fora do Git artefatos de build, IDE e arquivos locais de ambiente.
