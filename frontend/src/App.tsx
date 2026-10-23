import { useState } from 'react'
import { BeneficiarioForm } from './components/BeneficiarioForm'
import { BeneficiarioList } from './components/BeneficiarioList'
import './App.css'

function App() {
  const [telaCadastro, setTelaCadastro] = useState(false)
  const [chaveAtualizacao, setChaveAtualizacao] = useState(0)
  const [mensagemSucesso, setMensagemSucesso] = useState('')

  function concluirCadastro(nome: string) {
    setMensagemSucesso(`${nome} foi cadastrado com sucesso.`)
    setChaveAtualizacao((atual) => atual + 1)
    setTelaCadastro(false)
  }

  return (
    <div className="aplicacao">
      <header className="barra-superior">
        <a className="marca" href="/" aria-label="SocialConnect, página inicial">
          <span className="marca-icone" aria-hidden="true">S</span>
          <span>Social<span>Connect</span></span>
        </a>
        <span className="rotulo-sistema">Gestão de beneficiários</span>
      </header>

      <main className="conteudo-principal">
        <section className="apresentacao" aria-labelledby="titulo-principal">
          <div>
            <p className="sobretitulo">Rede de apoio · Gestão social</p>
            <h1 id="titulo-principal">Cuidar começa com <span>conexão.</span></h1>
            <p className="texto-apresentacao">
              Organize os cadastros e acompanhe as pessoas atendidas pela sua iniciativa.
            </p>
          </div>
          <div className="indicador-resumo" aria-label="Plataforma SocialConnect">
            <span className="indicador-ponto" aria-hidden="true" />
            API SocialConnect
            <span className="separador" aria-hidden="true">·</span>
            Ambiente local
          </div>
        </section>

        {telaCadastro ? (
          <BeneficiarioForm
            aoCancelar={() => setTelaCadastro(false)}
            aoConcluir={concluirCadastro}
          />
        ) : (
          <BeneficiarioList
            chaveAtualizacao={chaveAtualizacao}
            mensagemSucesso={mensagemSucesso}
            aoCadastrar={() => {
              setMensagemSucesso('')
              setTelaCadastro(true)
            }}
          />
        )}

        <footer className="rodape-aplicacao">
          <span>SocialConnect · Projeto de extensão</span>
          <span>Protótipo acadêmico · utilize dados fictícios</span>
        </footer>
      </main>
    </div>
  )
}

export default App
