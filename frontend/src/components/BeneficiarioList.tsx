import { useEffect, useState } from 'react'
import { listarBeneficiarios } from '../services/beneficiarioService'
import { mensagemDeErro } from '../services/api'
import type { Beneficiario, PageResponse } from '../types/beneficiario'
import { formatarCpfCnpj, formatarTelefone } from '../utils/formatters'

interface BeneficiarioListProps {
  chaveAtualizacao: number
  mensagemSucesso: string
  aoCadastrar: () => void
}

const TAMANHO_PAGINA = 10

function formatarData(data: string): string {
  const [ano, mes, dia] = data.split('-').map(Number)
  return new Intl.DateTimeFormat('pt-BR').format(new Date(ano, mes - 1, dia))
}

export function BeneficiarioList({
  chaveAtualizacao,
  mensagemSucesso,
  aoCadastrar,
}: BeneficiarioListProps) {
  const [pagina, setPagina] = useState<PageResponse<Beneficiario> | null>(null)
  const [numeroPagina, setNumeroPagina] = useState(0)
  const [tentativas, setTentativas] = useState(0)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')

  useEffect(() => {
    let ativa = true

    async function carregarBeneficiarios() {
      setCarregando(true)
      setErro('')

      try {
        const resultado = await listarBeneficiarios(numeroPagina, TAMANHO_PAGINA)
        if (ativa) setPagina(resultado)
      } catch (falha) {
        if (ativa) {
          setErro(mensagemDeErro(falha, 'Não foi possível carregar os beneficiários.'))
        }
      } finally {
        if (ativa) setCarregando(false)
      }
    }

    void carregarBeneficiarios()

    return () => {
      ativa = false
    }
  }, [numeroPagina, chaveAtualizacao, tentativas])

  function voltarParaPrimeiraPagina() {
    setNumeroPagina(0)
    setTentativas((atual) => atual + 1)
  }

  function iniciarCadastro() {
    setNumeroPagina(0)
    aoCadastrar()
  }

  return (
    <section className="painel" aria-labelledby="titulo-lista">
      <div className="cabecalho-painel">
        <div>
          <p className="sobretitulo">Acompanhamento social</p>
          <h2 id="titulo-lista">Beneficiários cadastrados</h2>
          <p className="texto-secundario">
            Consulte os registros e mantenha os dados da comunidade organizados.
          </p>
        </div>
        <button className="botao botao-principal" onClick={iniciarCadastro} type="button">
          <span aria-hidden="true">＋</span> Novo beneficiário
        </button>
      </div>

      {mensagemSucesso && (
        <p className="mensagem mensagem-sucesso" role="status">
          {mensagemSucesso}
        </p>
      )}

      {erro && (
        <div className="mensagem mensagem-erro" role="alert">
          <p>{erro}</p>
          <button className="botao botao-secundario" onClick={voltarParaPrimeiraPagina} type="button">
            Tentar novamente
          </button>
        </div>
      )}

      {carregando && (
        <p className="estado-vazio" role="status" aria-live="polite">
          Carregando beneficiários…
        </p>
      )}

      {!carregando && !erro && pagina?.empty && (
        <div className="estado-vazio">
          <span className="icone-vazio" aria-hidden="true">○</span>
          <h3>Nenhum beneficiário cadastrado</h3>
          <p>Quando houver registros, eles aparecerão nesta lista.</p>
          <button className="botao botao-secundario" onClick={iniciarCadastro} type="button">
            Cadastrar primeiro beneficiário
          </button>
        </div>
      )}

      {!carregando && !erro && pagina && !pagina.empty && (
        <>
          <div className="tabela-responsiva">
            <table className="tabela-beneficiarios">
              <thead>
                <tr>
                  <th scope="col">Nome</th>
                  <th scope="col">CPF / CNPJ</th>
                  <th scope="col">Telefone</th>
                  <th scope="col">Data do cadastro</th>
                </tr>
              </thead>
              <tbody>
                {pagina.content.map((beneficiario) => (
                  <tr key={beneficiario.idBeneficiario}>
                    <td>
                      <span className="nome-beneficiario">{beneficiario.nome}</span>
                      <span className="identificador">Cadastro #{beneficiario.idBeneficiario}</span>
                    </td>
                    <td>{formatarCpfCnpj(beneficiario.cpf)}</td>
                    <td>{beneficiario.telefone ? formatarTelefone(beneficiario.telefone) : '—'}</td>
                    <td>{formatarData(beneficiario.dataCadastro)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="rodape-tabela">
            <p>
              {pagina.totalElements} {pagina.totalElements === 1 ? 'registro' : 'registros'}
              {' · '}Página {pagina.number + 1} de {pagina.totalPages}
            </p>
            <div className="controles-paginacao" aria-label="Paginação dos beneficiários">
              <button
                className="botao botao-paginacao"
                disabled={pagina.first}
                onClick={() => setNumeroPagina((atual) => Math.max(0, atual - 1))}
                type="button"
              >
                Anterior
              </button>
              <button
                className="botao botao-paginacao"
                disabled={pagina.last}
                onClick={() => setNumeroPagina((atual) => atual + 1)}
                type="button"
              >
                Próxima
              </button>
            </div>
          </div>
        </>
      )}
    </section>
  )
}
