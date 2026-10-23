import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

export function mensagemDeErro(erro: unknown, alternativa: string): string {
  if (axios.isAxiosError<{ detail?: string; title?: string }>(erro)) {
    return erro.response?.data?.detail ?? erro.response?.data?.title ?? alternativa
  }

  return alternativa
}

export default api
