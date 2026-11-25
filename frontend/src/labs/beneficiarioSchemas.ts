import Joi from 'joi'
import * as yup from 'yup'
import { beneficiarioSchema } from '../schemas/beneficiarioSchema'
import type { BeneficiarioFormValues } from '../schemas/beneficiarioSchema'
import { mensagensBeneficiario } from '../schemas/beneficiarioMensagens'

export type CamposBeneficiario = BeneficiarioFormValues

// Reutiliza o schema que já valida o formulário da aplicação.
export const beneficiarioZod = beneficiarioSchema
export type BeneficiarioZod = BeneficiarioFormValues

export const beneficiarioYup = yup.object({
  nome: yup.string().typeError(mensagensBeneficiario.nomeTexto).trim()
    .required(mensagensBeneficiario.nomeObrigatorio)
    .max(150, mensagensBeneficiario.nomeTamanho),
  cpf: yup.string().typeError(mensagensBeneficiario.cpfTexto)
    .required(mensagensBeneficiario.cpfFormato).test(
    'cpf-cnpj',
    mensagensBeneficiario.cpfFormato,
    (valor) => {
      const quantidade = (valor ?? '').replace(/\D/g, '').length
      return quantidade === 11 || quantidade === 14
    },
  ),
  telefone: yup.string().typeError(mensagensBeneficiario.telefoneTexto)
    .defined(mensagensBeneficiario.telefoneTexto).test(
    'telefone',
    mensagensBeneficiario.telefoneFormato,
    (valor) => {
      const quantidade = (valor ?? '').replace(/\D/g, '').length
      return quantidade === 0 || quantidade === 10 || quantidade === 11
    },
  ),
  endereco: yup.string().typeError(mensagensBeneficiario.enderecoTexto).trim()
    .max(255, mensagensBeneficiario.enderecoTamanho)
    .defined(mensagensBeneficiario.enderecoTexto),
  situacaoVulnerabilidade: yup.string().typeError(mensagensBeneficiario.situacaoTexto).trim()
    .max(500, mensagensBeneficiario.situacaoTamanho)
    .defined(mensagensBeneficiario.situacaoTexto),
}).noUnknown()

export type BeneficiarioYup = yup.InferType<typeof beneficiarioYup>

export const beneficiarioJoi = Joi.object({
  nome: Joi.string().trim().min(1).max(150).required().messages({
    'any.required': mensagensBeneficiario.nomeObrigatorio,
    'string.empty': mensagensBeneficiario.nomeObrigatorio,
    'string.base': mensagensBeneficiario.nomeTexto,
    'string.max': mensagensBeneficiario.nomeTamanho,
  }),
  cpf: Joi.string().custom((valor: string, helpers) => {
    const quantidade = valor.replace(/\D/g, '').length
    if (quantidade !== 11 && quantidade !== 14) return helpers.error('string.cpfCnpj')
    return valor
  }).required().messages({
    'any.required': mensagensBeneficiario.cpfFormato,
    'string.base': mensagensBeneficiario.cpfTexto,
    'string.empty': mensagensBeneficiario.cpfFormato,
    'string.cpfCnpj': mensagensBeneficiario.cpfFormato,
  }),
  telefone: Joi.string().allow('').custom((valor: string, helpers) => {
    const quantidade = valor.replace(/\D/g, '').length
    if (quantidade !== 0 && quantidade !== 10 && quantidade !== 11) return helpers.error('string.telefone')
    return valor
  }).required().messages({
    'any.required': mensagensBeneficiario.telefoneTexto,
    'string.base': mensagensBeneficiario.telefoneTexto,
    'string.telefone': mensagensBeneficiario.telefoneFormato,
  }),
  endereco: Joi.string().allow('').trim().max(255).required().messages({
    'any.required': mensagensBeneficiario.enderecoTexto,
    'string.base': mensagensBeneficiario.enderecoTexto,
    'string.max': mensagensBeneficiario.enderecoTamanho,
  }),
  situacaoVulnerabilidade: Joi.string().allow('').trim().max(500).required().messages({
    'any.required': mensagensBeneficiario.situacaoTexto,
    'string.base': mensagensBeneficiario.situacaoTexto,
    'string.max': mensagensBeneficiario.situacaoTamanho,
  }),
}).unknown(false).messages({ 'object.unknown': mensagensBeneficiario.campoDesconhecido })

// Joi não infere o contrato TypeScript a partir do schema.
export interface BeneficiarioJoi {
  nome: string
  cpf: string
  telefone: string
  endereco: string
  situacaoVulnerabilidade: string
}
