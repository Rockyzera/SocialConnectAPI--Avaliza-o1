export interface Beneficiario {
  idBeneficiario: number
  nome: string
  cpf: string
  telefone: string | null
  endereco: string | null
  situacaoVulnerabilidade: string | null
  dataCadastro: string
}

export interface BeneficiarioRequest {
  nome: string
  cpf: string
  telefone: string
  endereco: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
  first: boolean
  last: boolean
  empty: boolean
}
