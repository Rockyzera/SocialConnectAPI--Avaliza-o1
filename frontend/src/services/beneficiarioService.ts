import api from './api'
import type {
  Beneficiario,
  BeneficiarioRequest,
  PageResponse,
} from '../types/beneficiario'

export async function listarBeneficiarios(
  pagina: number,
  tamanho: number,
  ordenacao: string,
): Promise<PageResponse<Beneficiario>> {
  const resposta = await api.get<PageResponse<Beneficiario>>('/beneficiarios', {
    params: { page: pagina, size: tamanho, sort: ordenacao },
  })

  return resposta.data
}

export async function cadastrarBeneficiario(
  beneficiario: BeneficiarioRequest,
): Promise<Beneficiario> {
  const resposta = await api.post<Beneficiario>('/beneficiarios', beneficiario)
  return resposta.data
}

export async function atualizarBeneficiario(
  id: number,
  beneficiario: BeneficiarioRequest,
): Promise<Beneficiario> {
  const resposta = await api.put<Beneficiario>(`/beneficiarios/${id}`, beneficiario)
  return resposta.data
}

export async function excluirBeneficiario(id: number): Promise<void> {
  await api.delete(`/beneficiarios/${id}`)
}
