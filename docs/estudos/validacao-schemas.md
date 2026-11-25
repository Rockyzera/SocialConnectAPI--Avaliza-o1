# Validadores de schema: Zod, Yup e Joi

## Conceito

Um schema descreve o formato e as regras esperadas para um dado. O validador compara uma entrada com essas regras, pode normalizar valores (por exemplo, remover espaços) e retorna o dado válido ou erros. No SocialConnect, a validação do front-end melhora o retorno no formulário; a validação Java/Spring continua protegendo a API, pois clientes podem ignorar a interface.

## Comparação

| Critério | Zod | Yup | Joi |
|---|---|---|---|
| Foco | TypeScript-first | JavaScript e TypeScript | JavaScript, com tipos TypeScript disponíveis |
| Inferência de tipo | `z.infer<typeof schema>` deriva o tipo do schema | `yup.InferType<typeof schema>` deriva o tipo de saída | O contrato da aplicação costuma ser declarado separadamente |
| Validação | `parse` lança erro; `safeParse` retorna união sucesso/erro | `validate` retorna Promise e rejeita com `ValidationError`; existe `validateSync` | `validate` retorna `{ value, error }`; existe `validateAsync` |
| Normalização | Transformações explícitas como `.trim()` | Transformações encadeadas como `.trim()` | Conversão habilitada por padrão e configurável com `convert` |
| React Hook Form | `zodResolver` | `yupResolver` | `joiResolver` |
| Ponto forte | Tipos e regras próximos em projetos TypeScript | API direta para regras de formulários | Opções amplas para regras e conversões em Node.js |

Os três oferecem objetos, regras encadeadas e erros por campo. Diferenças de conversão, tratamento de chaves extras e emissão de erros precisam ser consideradas ao migrar. O laboratório compara um conjunto equivalente de campos e regras, sem afirmar que todos os comportamentos padrão das bibliotecas são idênticos.

## Mini-laboratório `BeneficiarioSchema`

As três implementações estão em [`frontend/src/labs/beneficiarioSchemas.ts`](../../frontend/src/labs/beneficiarioSchemas.ts); a versão Zod reutiliza o schema já usado no cadastro da SocialConnect. Elas seguem as regras do formulário: nome após trim, não vazio e com até 150 caracteres; CPF/CNPJ com 11 ou 14 dígitos após remover pontuação; telefone vazio ou com 10/11 dígitos; endereço com até 255 caracteres; descrição de vulnerabilidade com até 500. O teste de CPF/CNPJ verifica quantidade de dígitos, não o dígito verificador.

Há três formulários interativos em [`frontend/src/components/LaboratorioValidacao.tsx`](../../frontend/src/components/LaboratorioValidacao.tsx), disponíveis em `/laboratorio-validacao`. A página tem um campo de cada vez por biblioteca, mostra erros por campo e uma confirmação quando os dados passam. É demonstrativa: não envia nem salva dados. A aplicação carrega essa página sob demanda; Joi fica no chunk do laboratório.

As implementações se chamam `beneficiarioZod`, `beneficiarioYup` e `beneficiarioJoi` para conviver no mesmo módulo. Exemplos do tipo de chamada:

```ts
const zodResult = beneficiarioZod.safeParse(entrada)
const yupResult = beneficiarioYup.validateSync(entrada, { abortEarly: false })
const joiResult = beneficiarioJoi.validate(entrada, { abortEarly: false, convert: true })
```

As mensagens exibidas pelos três formulários foram personalizadas em português para campos obrigatórios, formatos e limites. Sem essa configuração, algumas regras mostram mensagens padrão em inglês. As frases equivalentes compartilham o catálogo em `frontend/src/schemas/beneficiarioMensagens.ts`.

Para abrir os formulários, inicie o front-end na pasta `frontend/` e visite `http://localhost:5173/laboratorio-validacao`. Para rodar o benchmark no terminal, também na pasta `frontend/`:

```powershell
npm run lab:schemas
```

O programa aquece os validadores, mede entradas válidas e inválidas (uma regra inválida por vez), repete cinco rodadas e apresenta a mediana em operações por segundo. O lote padrão é de 5 mil validações por biblioteca e cenário; ajuste pela variável `BENCH_ITERATIONS`, por exemplo:

```powershell
$env:BENCH_ITERATIONS = "10000"
npm run lab:schemas
```

## Como interpretar performance

Os números medem o conjunto e as versões instaladas nesta máquina, com conversão/normalização ativada. Eles variam conforme Node, hardware, schema, lote, entrada e opções. A entrada inválida também pode custar mais porque as bibliotecas constroem erros; por isso os dois casos são apresentados separadamente. O benchmark não mede renderização React, bundle do navegador nem latência do servidor.

Uma comparação justa registra junto dos números: versão do Node, versões do Zod/Yup/Joi, quantidade de iterações e opções usadas. A mediana evita que uma rodada isolada determine a conclusão. Para um formulário pequeno, desempenho raramente deve decidir sozinho; inferência de tipos, mensagens úteis e manutenção tendem a ter mais impacto no dia a dia.

### Primeira medição neste ambiente

Executado com Node `v24.14.0`, Zod `4.6.7`, Yup `1.7.1` e Joi `18.3.2`, usando 5 mil validações por rodada e mediana de cinco rodadas:

| Entrada | Zod | Yup | Joi |
|---|---:|---:|---:|
| Válida | 945.448 ops/s | 75.849 ops/s | 80.912 ops/s |
| Inválida (descrição acima de 500 caracteres) | 446.863 ops/s | 7.861 ops/s | 69.463 ops/s |

É um retrato desta execução, não um ranking universal: o hardware não foi registrado, e o caminho de erro de cada biblioteca tem custos próprios. Use o comando do laboratório no ambiente da apresentação para reproduzir e substituir esses números.

## Integração com React Hook Form

React Hook Form recebe um `resolver` para delegar a validação ao schema. A estrutura é a mesma para os três:

```tsx
// Zod
useForm({ resolver: zodResolver(beneficiarioZod) })

// Yup
useForm({ resolver: yupResolver(beneficiarioYup) })

// Joi
useForm({ resolver: joiResolver(beneficiarioJoi) })
```

No formulário real, os valores e erros continuam ligados aos campos por `register`, `Controller` e `formState.errors`. A resolução Joi requer adaptar o tipo do formulário, pois Joi não fornece a mesma inferência direta do schema que Zod e Yup. Os resolvers fazem parte do pacote [`@hookform/resolvers`](https://github.com/react-hook-form/resolvers).

## Decisão para a SocialConnect

Zod é uma escolha adequada para este projeto porque o front-end já usa TypeScript, o schema atual já deriva o tipo de formulário e a tela já usa React Hook Form com `zodResolver`. Isso evita manter manualmente um tipo equivalente às regras e mantém a validação localizável em `frontend/src/schemas/beneficiarioSchema.ts`. Yup também oferece inferência e Joi também pode ser conectado ao formulário, mas a troca não traria vantagem suficiente para justificar reescrever uma integração já estabelecida.

Essa decisão é específica do front-end TypeScript. O back-end Spring valida seus próprios DTOs com Bean Validation; o schema Zod não é compartilhado automaticamente com Java. As regras equivalentes devem continuar alinhadas nas duas camadas.

## Referências

- [Zod: parsing e inferência de tipos](https://zod.dev/basics)
- [Yup: API e `InferType`](https://github.com/jquense/yup)
- [Joi: API de validação e conversão](https://joi.dev/api/)
- [React Hook Form: resolvers](https://github.com/react-hook-form/resolvers)
