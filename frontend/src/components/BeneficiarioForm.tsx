import { useState, type FormEvent } from 'react'
import { cadastrarBeneficiario } from '../services/beneficiarioService'
import { mensagemDeErro } from '../services/api'
import type { BeneficiarioRequest } from '../types/beneficiario'
import { formatarCpfCnpj, formatarTelefone, somenteDigitos } from '../utils/formatters'

interface BeneficiarioFormProps {
  aoCancelar: () => void
  aoConcluir: (nome: string) => void
}

interface CamposFormulario {
  nome: string
  cpf: string
  telefone: string
  endereco: string
}

const CAMPOS_INICIAIS: CamposFormulario = {
  nome: '',
  cpf: '',
  telefone: '',
  endereco: '',
}

export function BeneficiarioForm({ aoCancelar, aoConcluir }: BeneficiarioFormProps) {
  const [campos, setCampos] = useState(CAMPOS_INICIAIS)
  const [erro, setErro] = useState('')
  const [enviando, setEnviando] = useState(false)

  function atualizarCampo(campo: keyof CamposFormulario, valor: string) {
    setCampos((atuais) => ({ ...atuais, [campo]: valor }))
  }

  async function enviarFormulario(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault()
    setErro('')

    const cpfLimpo = somenteDigitos(campos.cpf)
    if (!campos.nome.trim() || !cpfLimpo || !campos.telefone.trim() || !campos.endereco.trim()) {
      setErro('Preencha todos os campos obrigatórios antes de continuar.')
      return
    }

    if (cpfLimpo.length !== 11 && cpfLimpo.length !== 14) {
      setErro('Informe um CPF com 11 dígitos ou um CNPJ com 14 dígitos.')
      return
    }

    const dados: BeneficiarioRequest = {
      nome: campos.nome.trim(),
      cpf: cpfLimpo,
      telefone: somenteDigitos(campos.telefone),
      endereco: campos.endereco.trim(),
    }

    setEnviando(true)
    try {
      await cadastrarBeneficiario(dados)
      aoConcluir(dados.nome)
    } catch (falha) {
      setErro(mensagemDeErro(falha, 'Não foi possível cadastrar. Confira os dados e tente novamente.'))
    } finally {
      setEnviando(false)
    }
  }

  return (
    <section className="painel painel-formulario" aria-labelledby="titulo-formulario">
      <div className="cabecalho-painel">
        <div>
          <p className="sobretitulo">Novo registro</p>
          <h2 id="titulo-formulario">Cadastrar beneficiário</h2>
          <p className="texto-secundario">Preencha os dados básicos para incluir uma pessoa na lista.</p>
        </div>
      </div>

      {erro && <p className="mensagem mensagem-erro" role="alert">{erro}</p>}

      <form className="formulario" onSubmit={enviarFormulario}>
        <label className="campo campo-largo" htmlFor="nome">
          <span>Nome completo <span className="obrigatorio">*</span></span>
          <input
            autoComplete="name"
            id="nome"
            maxLength={150}
            onChange={(evento) => atualizarCampo('nome', evento.target.value)}
            placeholder="Ex.: Maria da Silva"
            required
            value={campos.nome}
          />
        </label>

        <label className="campo" htmlFor="cpf">
          <span>CPF ou CNPJ <span className="obrigatorio">*</span></span>
          <input
            autoComplete="off"
            id="cpf"
            inputMode="numeric"
            maxLength={18}
            onChange={(evento) => atualizarCampo('cpf', formatarCpfCnpj(evento.target.value))}
            placeholder="Somente números ou com pontuação"
            required
            value={campos.cpf}
          />
          <small>Informe 11 dígitos para CPF ou 14 para CNPJ.</small>
        </label>

        <label className="campo" htmlFor="telefone">
          <span>Telefone <span className="obrigatorio">*</span></span>
          <input
            autoComplete="tel"
            id="telefone"
            maxLength={15}
            onChange={(evento) => atualizarCampo('telefone', formatarTelefone(evento.target.value))}
            placeholder="(11) 99999-9999"
            required
            value={campos.telefone}
          />
        </label>

        <label className="campo campo-largo" htmlFor="endereco">
          <span>Endereço <span className="obrigatorio">*</span></span>
          <input
            autoComplete="street-address"
            id="endereco"
            maxLength={255}
            onChange={(evento) => atualizarCampo('endereco', evento.target.value)}
            placeholder="Rua, número, bairro e cidade"
            required
            value={campos.endereco}
          />
        </label>

        <p className="nota-formulario"><span className="obrigatorio">*</span> Campos obrigatórios</p>

        <div className="acoes-formulario">
          <button className="botao botao-secundario" onClick={aoCancelar} type="button">
            Cancelar
          </button>
          <button className="botao botao-principal" disabled={enviando} type="submit">
            {enviando ? 'Salvando…' : 'Salvar beneficiário'}
          </button>
        </div>
      </form>
    </section>
  )
}
