import { useEffect, useState } from 'react'
import {
  Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogContentText,
  DialogTitle, IconButton, Pagination, Paper, Skeleton, Stack, Table, TableBody,
  TableCell, TableContainer, TableHead, TableRow, TableSortLabel, Tooltip, Typography,
} from '@mui/material'
import DeleteOutlineIcon from '@mui/icons-material/DeleteOutline'
import EditOutlinedIcon from '@mui/icons-material/EditOutlined'
import GroupOffOutlinedIcon from '@mui/icons-material/GroupOffOutlined'
import { excluirBeneficiario, listarBeneficiarios } from '../services/beneficiarioService'
import { mensagemDeErro } from '../services/api'
import type { Beneficiario, PageResponse } from '../types/beneficiario'
import { formatarCpfCnpj, formatarTelefone } from '../utils/formatters'

interface BeneficiarioListProps {
  chaveAtualizacao: number
  mensagemSucesso: string
  aoCadastrar: () => void
  aoEditar: (beneficiario: Beneficiario) => void
}

type CampoOrdenacao = 'nome' | 'cpf' | 'telefone' | 'dataCadastro'
type DirecaoOrdenacao = 'asc' | 'desc'
const TAMANHO_PAGINA = 10

function formatarData(data: string): string {
  const [ano, mes, dia] = data.split('-').map(Number)
  return new Intl.DateTimeFormat('pt-BR').format(new Date(ano, mes - 1, dia))
}

export function BeneficiarioList({ chaveAtualizacao, mensagemSucesso, aoCadastrar, aoEditar }: BeneficiarioListProps) {
  const [pagina, setPagina] = useState<PageResponse<Beneficiario> | null>(null)
  const [numeroPagina, setNumeroPagina] = useState(0)
  const [campoOrdenacao, setCampoOrdenacao] = useState<CampoOrdenacao>('nome')
  const [direcaoOrdenacao, setDirecaoOrdenacao] = useState<DirecaoOrdenacao>('asc')
  const [tentativas, setTentativas] = useState(0)
  const [carregando, setCarregando] = useState(true)
  const [erro, setErro] = useState('')
  const [erroExclusao, setErroExclusao] = useState('')
  const [mensagemLocal, setMensagemLocal] = useState('')
  const [alvoExclusao, setAlvoExclusao] = useState<Beneficiario | null>(null)
  const [excluindo, setExcluindo] = useState(false)

  useEffect(() => {
    let ativa = true

    async function carregarBeneficiarios() {
      setCarregando(true)
      setErro('')
      try {
        const resultado = await listarBeneficiarios(numeroPagina, TAMANHO_PAGINA, `${campoOrdenacao},${direcaoOrdenacao}`)
        if (ativa) setPagina(resultado)
      } catch (falha) {
        if (ativa) setErro(mensagemDeErro(falha, 'Não foi possível carregar os beneficiários.'))
      } finally {
        if (ativa) setCarregando(false)
      }
    }

    void carregarBeneficiarios()
    return () => { ativa = false }
  }, [numeroPagina, campoOrdenacao, direcaoOrdenacao, chaveAtualizacao, tentativas])

  function ordenarPor(campo: CampoOrdenacao) {
    if (campo === campoOrdenacao) setDirecaoOrdenacao((atual) => atual === 'asc' ? 'desc' : 'asc')
    else { setCampoOrdenacao(campo); setDirecaoOrdenacao('asc') }
    setNumeroPagina(0)
  }

  async function confirmarExclusao() {
    if (!alvoExclusao) return
    setExcluindo(true)
    setErroExclusao('')
    try {
      await excluirBeneficiario(alvoExclusao.idBeneficiario)
      setMensagemLocal(`${alvoExclusao.nome} foi excluído com sucesso.`)
      setAlvoExclusao(null)
      if (pagina?.content.length === 1 && numeroPagina > 0) setNumeroPagina((atual) => atual - 1)
      else setTentativas((atual) => atual + 1)
    } catch (falha) {
      setErroExclusao(mensagemDeErro(falha, 'Não foi possível excluir o beneficiário. Tente novamente.'))
    } finally {
      setExcluindo(false)
    }
  }

  const cabecalhos: { campo: CampoOrdenacao; rotulo: string }[] = [
    { campo: 'nome', rotulo: 'Nome' },
    { campo: 'cpf', rotulo: 'CPF / CNPJ' },
    { campo: 'telefone', rotulo: 'Telefone' },
    { campo: 'dataCadastro', rotulo: 'Data do cadastro' },
  ]

  return (
    <Paper component="section" aria-labelledby="titulo-lista" variant="outlined" sx={{ overflow: 'hidden' }}>
      <Box sx={{ p: { xs: 2, sm: 3 } }}>
        <Typography variant="overline" color="primary.main" fontWeight={700}>ACOMPANHAMENTO SOCIAL</Typography>
        <Typography id="titulo-lista" component="h2" variant="h5" fontWeight={700}>Beneficiários cadastrados</Typography>
        <Typography color="text.secondary" sx={{ mt: 0.5 }}>Consulte os registros e mantenha os dados organizados.</Typography>
      </Box>

      <Stack spacing={1.5} sx={{ px: { xs: 2, sm: 3 } }}>
        {mensagemSucesso && <Alert severity="success" role="status">{mensagemSucesso}</Alert>}
        {mensagemLocal && <Alert severity="success" role="status" onClose={() => setMensagemLocal('')}>{mensagemLocal}</Alert>}
        {erroExclusao && <Alert severity="error" role="alert">{erroExclusao}</Alert>}
        {erro && <Alert severity="error" role="alert" action={<Button color="inherit" onClick={() => setTentativas((atual) => atual + 1)}>Tentar novamente</Button>}>{erro}</Alert>}
      </Stack>

      {!carregando && !erro && pagina?.empty && (
        <Stack alignItems="center" textAlign="center" spacing={1.5} sx={{ px: 2, py: 7 }}>
          <GroupOffOutlinedIcon color="action" sx={{ fontSize: 48 }} aria-hidden="true" />
          <Typography component="h3" variant="h6">Nenhum beneficiário cadastrado</Typography>
          <Typography color="text.secondary">Quando houver registros, eles aparecerão nesta lista.</Typography>
          <Button variant="outlined" onClick={aoCadastrar}>Cadastrar primeiro beneficiário</Button>
        </Stack>
      )}

      {(carregando || (pagina !== null && !pagina.empty && !erro)) && (
        <>
          <TableContainer sx={{ mt: 2, overflowX: 'auto' }}>
            <Table aria-label="Lista de beneficiários" size="medium">
              <TableHead>
                <TableRow>
                  {cabecalhos.map(({ campo, rotulo }) => (
                    <TableCell key={campo} sortDirection={campoOrdenacao === campo ? direcaoOrdenacao : false}>
                      <TableSortLabel
                        active={campoOrdenacao === campo}
                        direction={campoOrdenacao === campo ? direcaoOrdenacao : 'asc'}
                        onClick={() => ordenarPor(campo)}
                      >{rotulo}</TableSortLabel>
                    </TableCell>
                  ))}
                  <TableCell align="right">Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {carregando
                  ? Array.from({ length: 5 }, (_, indice) => (
                    <TableRow key={`skeleton-${indice}`} aria-hidden="true">
                      {Array.from({ length: 5 }, (_, coluna) => <TableCell key={coluna}><Skeleton /></TableCell>)}
                    </TableRow>
                  ))
                  : pagina?.content.map((beneficiario) => (
                    <TableRow hover key={beneficiario.idBeneficiario}>
                      <TableCell>
                        <Typography fontWeight={600}>{beneficiario.nome}</Typography>
                        <Typography variant="caption" color="text.secondary">Cadastro #{beneficiario.idBeneficiario}</Typography>
                      </TableCell>
                      <TableCell>{formatarCpfCnpj(beneficiario.cpf)}</TableCell>
                      <TableCell>{beneficiario.telefone ? formatarTelefone(beneficiario.telefone) : '—'}</TableCell>
                      <TableCell>{formatarData(beneficiario.dataCadastro)}</TableCell>
                      <TableCell align="right" sx={{ whiteSpace: 'nowrap' }}>
                        <Tooltip title={`Editar ${beneficiario.nome}`}>
                          <IconButton aria-label={`Editar beneficiário ${beneficiario.nome}`} onClick={() => aoEditar(beneficiario)} color="primary">
                            <EditOutlinedIcon />
                          </IconButton>
                        </Tooltip>
                        <Tooltip title={`Excluir ${beneficiario.nome}`}>
                          <IconButton aria-label={`Excluir beneficiário ${beneficiario.nome}`} onClick={() => { setErroExclusao(''); setAlvoExclusao(beneficiario) }} color="error">
                            <DeleteOutlineIcon />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
              </TableBody>
            </Table>
          </TableContainer>

          {!carregando && pagina && !pagina.empty && (
            <Stack direction={{ xs: 'column', sm: 'row' }} alignItems="center" justifyContent="space-between" spacing={1.5} sx={{ p: { xs: 2, sm: 3 } }}>
              <Typography variant="body2" color="text.secondary">
                {pagina.totalElements} {pagina.totalElements === 1 ? 'registro' : 'registros'} · Página {pagina.number + 1} de {Math.max(pagina.totalPages, 1)}
              </Typography>
              <Pagination
                aria-label="Paginação dos beneficiários"
                page={numeroPagina + 1}
                count={Math.max(pagina.totalPages, 1)}
                onChange={(_, valor) => setNumeroPagina(valor - 1)}
                color="primary"
                showFirstButton
                showLastButton
              />
            </Stack>
          )}
        </>
      )}

      {carregando && <Typography role="status" aria-live="polite" sx={{ position: 'absolute', width: 1, height: 1, p: 0, m: -1, overflow: 'hidden', clip: 'rect(0, 0, 0, 0)', whiteSpace: 'nowrap', border: 0 }}>Carregando beneficiários</Typography>}

      <Dialog open={alvoExclusao !== null} onClose={() => !excluindo && setAlvoExclusao(null)} aria-labelledby="titulo-confirmacao-exclusao" aria-describedby="texto-confirmacao-exclusao">
        <DialogTitle id="titulo-confirmacao-exclusao">Excluir beneficiário?</DialogTitle>
        <DialogContent>
          <DialogContentText id="texto-confirmacao-exclusao">
            Esta ação removerá {alvoExclusao?.nome ?? 'o cadastro'} permanentemente. Deseja continuar?
          </DialogContentText>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={() => setAlvoExclusao(null)} disabled={excluindo}>Cancelar</Button>
          <Button color="error" variant="contained" onClick={() => void confirmarExclusao()} disabled={excluindo} autoFocus>
            {excluindo ? 'Excluindo…' : 'Excluir'}
          </Button>
        </DialogActions>
      </Dialog>
    </Paper>
  )
}
