import { z } from 'zod'
import { somenteDigitos } from '../utils/formatters'
import { mensagensBeneficiario } from './beneficiarioMensagens'

export const beneficiarioSchema = z.object({
  nome: z.string({ error: mensagensBeneficiario.nomeTexto }).trim()
    .min(1, mensagensBeneficiario.nomeObrigatorio)
    .max(150, mensagensBeneficiario.nomeTamanho),
  cpf: z.string({ error: mensagensBeneficiario.cpfTexto }).refine((valor) => {
    const quantidade = somenteDigitos(valor).length
    return quantidade === 11 || quantidade === 14
  }, mensagensBeneficiario.cpfFormato),
  telefone: z.string({ error: mensagensBeneficiario.telefoneTexto }).refine((valor) => {
    const quantidade = somenteDigitos(valor).length
    return quantidade === 0 || quantidade === 10 || quantidade === 11
  }, mensagensBeneficiario.telefoneFormato),
  endereco: z.string({ error: mensagensBeneficiario.enderecoTexto }).trim()
    .max(255, mensagensBeneficiario.enderecoTamanho),
  situacaoVulnerabilidade: z.string({ error: mensagensBeneficiario.situacaoTexto }).trim()
    .max(500, mensagensBeneficiario.situacaoTamanho),
})

export type BeneficiarioFormValues = z.infer<typeof beneficiarioSchema>
