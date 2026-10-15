# Registro de uso de IA

Este registro documenta contribuições relevantes de IA usadas no projeto, conforme o acordo de uso responsável descrito no README.

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
