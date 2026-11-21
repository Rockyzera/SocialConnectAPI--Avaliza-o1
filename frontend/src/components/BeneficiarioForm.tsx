import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Box, Button, Paper, Stack, TextField, Typography } from '@mui/material'
import SaveOutlinedIcon from '@mui/icons-material/SaveOutlined'
import { atualizarBeneficiario, cadastrarBeneficiario } from '../services/beneficiarioService'
import { mensagemDeErro } from '../services/api'
import type { Beneficiario } from '../types/beneficiario'
import { beneficiarioSchema, type BeneficiarioFormValues } from '../schemas/beneficiarioSchema'
import { formatarCpfCnpj, formatarTelefone, somenteDigitos } from '../utils/formatters'

interface BeneficiarioFormProps {
  beneficiario: Beneficiario | null
  aoCancelar: () => void
  aoConcluir: (nome: string) => void
}

export function BeneficiarioForm({ beneficiario, aoCancelar, aoConcluir }: BeneficiarioFormProps) {
  const editando = beneficiario !== null
  const [erroServidor, setErroServidor] = useState('')
  const { register, control, handleSubmit, formState: { errors, isSubmitting } } = useForm<BeneficiarioFormValues>({
    resolver: zodResolver(beneficiarioSchema),
    defaultValues: {
      nome: beneficiario?.nome ?? '',
      cpf: beneficiario?.cpf ?? '',
      telefone: beneficiario?.telefone ?? '',
      endereco: beneficiario?.endereco ?? '',
      situacaoVulnerabilidade: beneficiario?.situacaoVulnerabilidade ?? '',
    },
  })

  async function enviarFormulario(valores: BeneficiarioFormValues) {
    setErroServidor('')
    const dados = {
      nome: valores.nome.trim(),
      cpf: somenteDigitos(valores.cpf),
      telefone: somenteDigitos(valores.telefone),
      endereco: valores.endereco.trim(),
      situacaoVulnerabilidade: valores.situacaoVulnerabilidade.trim(),
    }

    try {
      if (beneficiario) await atualizarBeneficiario(beneficiario.idBeneficiario, dados)
      else await cadastrarBeneficiario(dados)
      aoConcluir(dados.nome)
    } catch (falha) {
      setErroServidor(mensagemDeErro(falha, `Não foi possível ${editando ? 'atualizar' : 'cadastrar'} o beneficiário. Confira os dados e tente novamente.`))
    }
  }

  return (
    <Paper component="section" aria-labelledby="titulo-formulario" variant="outlined" sx={{ p: { xs: 2, sm: 4 } }}>
      <Stack spacing={3}>
        <Box>
          <Typography variant="overline" color="primary.main" fontWeight={700}>{editando ? 'EDIÇÃO DE CADASTRO' : 'NOVO REGISTRO'}</Typography>
          <Typography id="titulo-formulario" component="h2" variant="h5" fontWeight={700}>
            {editando ? 'Editar beneficiário' : 'Cadastrar beneficiário'}
          </Typography>
          <Typography color="text.secondary" sx={{ mt: 0.5 }}>Os campos com * são obrigatórios.</Typography>
        </Box>

        {erroServidor && <Alert severity="error" role="alert">{erroServidor}</Alert>}

        <Box component="form" noValidate onSubmit={handleSubmit(enviarFormulario)}>
          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, minmax(0, 1fr))' }, gap: 2 }}>
            <TextField
              {...register('nome')}
              autoComplete="name"
              required
              label="Nome completo"
              placeholder="Ex.: Maria da Silva"
              error={Boolean(errors.nome)}
              helperText={errors.nome?.message}
              inputProps={{ maxLength: 150 }}
            />
            <Controller
              name="cpf"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  required
                  label="CPF ou CNPJ"
                  autoComplete="off"
                  placeholder="000.000.000-00"
                  inputProps={{ inputMode: 'numeric', maxLength: 18 }}
                  value={formatarCpfCnpj(field.value)}
                  onChange={(evento) => field.onChange(somenteDigitos(evento.target.value))}
                  error={Boolean(errors.cpf)}
                  helperText={errors.cpf?.message ?? 'A máscara é visual; somente dígitos são enviados.'}
                />
              )}
            />
            <Controller
              name="telefone"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="Telefone"
                  autoComplete="tel"
                  placeholder="(11) 99999-9999"
                  inputProps={{ inputMode: 'tel', maxLength: 15 }}
                  value={formatarTelefone(field.value)}
                  onChange={(evento) => field.onChange(somenteDigitos(evento.target.value))}
                  error={Boolean(errors.telefone)}
                  helperText={errors.telefone?.message}
                />
              )}
            />
            <TextField
              {...register('endereco')}
              autoComplete="street-address"
              label="Endereço"
              placeholder="Rua, número, bairro e cidade"
              error={Boolean(errors.endereco)}
              helperText={errors.endereco?.message}
              inputProps={{ maxLength: 255 }}
            />
            <TextField
              {...register('situacaoVulnerabilidade')}
              label="Situação de vulnerabilidade"
              placeholder="Informação opcional"
              error={Boolean(errors.situacaoVulnerabilidade)}
              helperText={errors.situacaoVulnerabilidade?.message}
              inputProps={{ maxLength: 500 }}
              multiline
              minRows={2}
              sx={{ gridColumn: { sm: '1 / -1' } }}
            />
          </Box>

          <Stack direction={{ xs: 'column-reverse', sm: 'row' }} justifyContent="flex-end" spacing={1.5} sx={{ mt: 3 }}>
            <Button variant="outlined" onClick={aoCancelar} disabled={isSubmitting}>Cancelar</Button>
            <Button type="submit" variant="contained" startIcon={<SaveOutlinedIcon />} disabled={isSubmitting}>
              {isSubmitting ? 'Salvando…' : editando ? 'Salvar alterações' : 'Salvar beneficiário'}
            </Button>
          </Stack>
        </Box>
      </Stack>
    </Paper>
  )
}
