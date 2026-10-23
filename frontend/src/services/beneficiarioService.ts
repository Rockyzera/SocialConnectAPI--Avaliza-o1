import api from './api'
import type {
  Beneficiario,
  BeneficiarioRequest,
  PageResponse,
} from '../types/beneficiario'

export async function listarBeneficiarios(
  pagina: number,
  tamanho: number,
): Promise<PageResponse<Beneficiario>> {
  const resposta = await api.get<PageResponse<Beneficiario>>('/beneficiarios', {
    params: { page: pagina, size: tamanho },
  })

  return resposta.data
}

export async function cadastrarBeneficiario(
  beneficiario: BeneficiarioRequest,
): Promise<Beneficiario> {
  const resposta = await api.post<Beneficiario>('/beneficiarios', beneficiario)
  return resposta.data
}
