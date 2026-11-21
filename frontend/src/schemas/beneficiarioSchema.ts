import { z } from 'zod'
import { somenteDigitos } from '../utils/formatters'

export const beneficiarioSchema = z.object({
  nome: z.string().trim().min(1, 'Informe o nome completo.').max(150, 'O nome deve ter até 150 caracteres.'),
  cpf: z.string().refine((valor) => {
    const quantidade = somenteDigitos(valor).length
    return quantidade === 11 || quantidade === 14
  }, 'Informe 11 dígitos para CPF ou 14 para CNPJ.'),
  telefone: z.string().refine((valor) => {
    const quantidade = somenteDigitos(valor).length
    return quantidade === 0 || quantidade === 10 || quantidade === 11
  }, 'Informe um telefone com DDD e 10 ou 11 dígitos.'),
  endereco: z.string().trim().max(255, 'O endereço deve ter até 255 caracteres.'),
  situacaoVulnerabilidade: z.string().trim().max(500, 'A descrição deve ter até 500 caracteres.'),
})

export type BeneficiarioFormValues = z.infer<typeof beneficiarioSchema>
