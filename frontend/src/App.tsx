import { useState } from 'react'
import { AppBar, Box, Button, Container, Stack, Toolbar, Typography } from '@mui/material'
import AddIcon from '@mui/icons-material/Add'
import VolunteerActivismIcon from '@mui/icons-material/VolunteerActivism'
import { BeneficiarioForm } from './components/BeneficiarioForm'
import { BeneficiarioList } from './components/BeneficiarioList'
import type { Beneficiario } from './types/beneficiario'

function App() {
  const [beneficiarioEmEdicao, setBeneficiarioEmEdicao] = useState<Beneficiario | null>(null)
  const [formularioAberto, setFormularioAberto] = useState(false)
  const [chaveAtualizacao, setChaveAtualizacao] = useState(0)
  const [mensagemSucesso, setMensagemSucesso] = useState('')

  function concluirFormulario(nome: string, editando: boolean) {
    setMensagemSucesso(`${nome} foi ${editando ? 'atualizado' : 'cadastrado'} com sucesso.`)
    setChaveAtualizacao((atual) => atual + 1)
    setFormularioAberto(false)
    setBeneficiarioEmEdicao(null)
  }

  function iniciarCadastro() {
    setMensagemSucesso('')
    setBeneficiarioEmEdicao(null)
    setFormularioAberto(true)
  }

  function iniciarEdicao(beneficiario: Beneficiario) {
    setMensagemSucesso('')
    setBeneficiarioEmEdicao(beneficiario)
    setFormularioAberto(true)
  }

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
      <AppBar position="static" color="inherit" elevation={0} sx={{ borderBottom: 1, borderColor: 'divider' }}>
        <Toolbar component={Container} maxWidth="lg" sx={{ minHeight: 72 }}>
          <VolunteerActivismIcon color="primary" sx={{ mr: 1.5, fontSize: 32 }} aria-hidden="true" />
          <Typography component="a" href="/" variant="h6" color="text.primary" fontWeight={800} sx={{ textDecoration: 'none' }}>
            Social<Typography component="span" variant="inherit" color="primary.main">Connect</Typography>
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ ml: 'auto', display: { xs: 'none', sm: 'block' } }}>
            Gestão de beneficiários
          </Typography>
        </Toolbar>
      </AppBar>

      <Container component="main" maxWidth="lg" sx={{ py: { xs: 3, md: 6 } }}>
        <Stack spacing={3}>
          <Box sx={{ display: 'flex', alignItems: { xs: 'flex-start', sm: 'center' }, justifyContent: 'space-between', gap: 2, flexWrap: 'wrap' }}>
            <Box>
              <Typography variant="overline" color="primary.main" fontWeight={700}>
                REDE DE APOIO · GESTÃO SOCIAL
              </Typography>
              <Typography component="h1" variant="h3" fontWeight={800} sx={{ fontSize: { xs: '2rem', md: '2.75rem' } }}>
                Cuidar começa com conexão.
              </Typography>
              <Typography color="text.secondary" sx={{ mt: 1 }}>
                Organize os cadastros e acompanhe as pessoas atendidas pela sua iniciativa.
              </Typography>
            </Box>
            {!formularioAberto && (
              <Button variant="contained" startIcon={<AddIcon />} onClick={iniciarCadastro}>
                Novo beneficiário
              </Button>
            )}
          </Box>

          {formularioAberto ? (
            <BeneficiarioForm
              beneficiario={beneficiarioEmEdicao}
              aoCancelar={() => { setFormularioAberto(false); setBeneficiarioEmEdicao(null) }}
              aoConcluir={(nome) => concluirFormulario(nome, Boolean(beneficiarioEmEdicao))}
            />
          ) : (
            <BeneficiarioList
              chaveAtualizacao={chaveAtualizacao}
              mensagemSucesso={mensagemSucesso}
              aoCadastrar={iniciarCadastro}
              aoEditar={iniciarEdicao}
            />
          )}

          <Box component="footer" sx={{ pt: 2, display: 'flex', justifyContent: 'space-between', gap: 1, flexWrap: 'wrap', color: 'text.secondary' }}>
            <Typography variant="caption">SocialConnect · Projeto de extensão</Typography>
            <Typography variant="caption">Protótipo acadêmico · utilize dados fictícios</Typography>
          </Box>
        </Stack>
      </Container>
    </Box>
  )
}

export default App
