import { performance } from 'node:perf_hooks'
import { beneficiarioJoi, beneficiarioYup, beneficiarioZod, type BeneficiarioZod } from '../src/labs/beneficiarioSchemas'

const entradas = {
  valido: {
    nome: ' Maria da Silva ',
    cpf: '123.456.789-01',
    telefone: '(11) 99999-9999',
    endereco: ' Rua das Flores, 100 ',
    situacaoVulnerabilidade: ' Insegurança alimentar ',
  },
  invalido: {
    nome: 'Maria da Silva',
    cpf: '12345678901',
    telefone: '11999999999',
    endereco: 'Rua das Flores, 100',
    situacaoVulnerabilidade: 'x'.repeat(501),
  },
} satisfies Record<string, BeneficiarioZod>

type Validator = (entrada: unknown) => boolean

const validadores: Record<string, Validator> = {
  Zod: (entrada) => beneficiarioZod.safeParse(entrada).success,
  Yup: (entrada) => {
    try {
      beneficiarioYup.validateSync(entrada, { abortEarly: false, stripUnknown: true })
      return true
    } catch {
      return false
    }
  },
  Joi: (entrada) => !beneficiarioJoi.validate(entrada, { abortEarly: false, convert: true }).error,
}

for (const [tipo, entrada] of Object.entries(entradas)) {
  const esperado = tipo === 'valido'
  for (const [nome, validar] of Object.entries(validadores)) {
    if (validar(entrada) !== esperado) {
      throw new Error(`${nome} não produziu o resultado esperado para a entrada ${tipo}.`)
    }
  }
}

const iteracoes = Number(process.env.BENCH_ITERATIONS ?? 5_000)
const aquecimento = Math.min(1_000, iteracoes)
const rodadas = 5

function medir(validar: Validator, entrada: unknown): number {
  for (let i = 0; i < aquecimento; i++) validar(entrada)
  const inicio = performance.now()
  for (let i = 0; i < iteracoes; i++) validar(entrada)
  return (performance.now() - inicio) / 1000
}

function mediana(valores: number[]): number {
  return [...valores].sort((a, b) => a - b)[Math.floor(valores.length / 2)]
}

console.log(`Node ${process.version}; ${iteracoes.toLocaleString('pt-BR')} validações por rodada; mediana de ${rodadas} rodadas.`)
console.log('Conversão/normalização habilitada nos três schemas; mede-se validação síncrona e não o consumo da saída.')

for (const [tipo, entrada] of Object.entries(entradas)) {
  console.log(`\nEntrada ${tipo}:`)
  for (const [nome, validar] of Object.entries(validadores)) {
    const tempos = Array.from({ length: rodadas }, () => medir(validar, entrada))
    const segundos = mediana(tempos)
    const operacoesPorSegundo = Math.round(iteracoes / segundos)
    console.log(`  ${nome}: ${operacoesPorSegundo.toLocaleString('pt-BR')} ops/s (mediana ${segundos.toFixed(3)} s)`)
  }
}
