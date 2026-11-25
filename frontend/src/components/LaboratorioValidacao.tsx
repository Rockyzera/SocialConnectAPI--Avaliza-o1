import { useState } from 'react'
import { joiResolver } from '@hookform/resolvers/joi'
import { yupResolver } from '@hookform/resolvers/yup'
import { zodResolver } from '@hookform/resolvers/zod'
import { Alert, Box, Button, Container, Paper, Stack, TextField, Typography } from '@mui/material'
import { useForm, type DefaultValues, type FieldErrors, type FieldValues, type Path, type Resolver } from 'react-hook-form'
import {
  beneficiarioJoi,
  beneficiarioYup,
  beneficiarioZod,
  type BeneficiarioJoi,
  type BeneficiarioYup,
  type BeneficiarioZod,
  type CamposBeneficiario,
} from '../labs/beneficiarioSchemas'

const VALORES_INICIAIS: CamposBeneficiario = {
  nome: '',
  cpf: '',
  telefone: '',
  endereco: '',
  situacaoVulnerabilidade: '',
}

function mensagemErro<T extends FieldValues>(errors: FieldErrors<T>, campo: keyof CamposBeneficiario): string | undefined {
  const erro = (errors as unknown as Record<string, { message?: string } | undefined>)[campo]
  return erro?.message
}

interface FormularioDemonstracaoProps<T extends CamposBeneficiario & FieldValues> {
  biblioteca: string
  descricao: string
  trechoCodigo: string
  resolver: Resolver<T>
}

function FormularioDemonstracao<T extends CamposBeneficiario & FieldValues>({
  biblioteca,
  descricao,
  trechoCodigo,
  resolver,
}: FormularioDemonstracaoProps<T>) {
  const [validado, setValidado] = useState(false)
  const { register, handleSubmit, reset, formState: { errors, isSubmitting } } = useForm<T>({
    resolver,
    defaultValues: VALORES_INICIAIS as DefaultValues<T>,
  })

  return (
    <Paper component="section" aria-label={`Formulário demonstrativo com ${biblioteca}`} variant="outlined" sx={{ p: 2.5, height: '100%' }}>
      <Stack spacing={2}>
        <Box>
          <Typography component="h2" variant="h5" fontWeight={750}>{biblioteca}</Typography>
          <Typography color="text.secondary" variant="body2">{descricao}</Typography>
        </Box>

        <Box component="pre" sx={{ m: 0, p: 1.5, borderRadius: 1.5, bgcolor: 'grey.100', color: 'text.primary', whiteSpace: 'pre-wrap', overflowWrap: 'anywhere', fontSize: '0.78rem' }}>
          <code>{trechoCodigo}</code>
        </Box>

        <Box
          component="form"
          noValidate
          onChange={() => setValidado(false)}
          onSubmit={handleSubmit(() => setValidado(true))}
        >
          <Stack spacing={1.75}>
            <TextField
              {...register('nome' as Path<T>)}
              label="Nome completo *"
              autoComplete="off"
              error={Boolean(errors.nome)}
              helperText={mensagemErro(errors, 'nome') ?? 'Obrigatório; máximo de 150 caracteres.'}
            />
            <TextField
              {...register('cpf' as Path<T>)}
              label="CPF ou CNPJ *"
              autoComplete="off"
              inputProps={{ inputMode: 'numeric' }}
              error={Boolean(errors.cpf)}
              helperText={mensagemErro(errors, 'cpf') ?? 'Informe 11 ou 14 dígitos; pontuação é aceita.'}
            />
            <TextField
              {...register('telefone' as Path<T>)}
              label="Telefone (opcional)"
              autoComplete="off"
              inputProps={{ inputMode: 'tel' }}
              error={Boolean(errors.telefone)}
              helperText={mensagemErro(errors, 'telefone') ?? 'Vazio ou com DDD e 10/11 dígitos.'}
            />
            <TextField
              {...register('endereco' as Path<T>)}
              label="Endereço (opcional)"
              autoComplete="off"
              error={Boolean(errors.endereco)}
              helperText={mensagemErro(errors, 'endereco') ?? 'Máximo de 255 caracteres.'}
            />
            <TextField
              {...register('situacaoVulnerabilidade' as Path<T>)}
              label="Situação de vulnerabilidade (opcional)"
              autoComplete="off"
              multiline
              minRows={2}
              error={Boolean(errors.situacaoVulnerabilidade)}
              helperText={mensagemErro(errors, 'situacaoVulnerabilidade') ?? 'Máximo de 500 caracteres.'}
            />

            {validado && <Alert severity="success">Dados válidos para este schema. Nada foi enviado nem salvo.</Alert>}

            <Stack direction="row" spacing={1}>
              <Button type="submit" variant="contained" disabled={isSubmitting}>
                Validar
              </Button>
              <Button type="button" variant="outlined" onClick={() => { reset(VALORES_INICIAIS as DefaultValues<T>); setValidado(false) }}>
                Limpar
              </Button>
            </Stack>
          </Stack>
        </Box>
      </Stack>
    </Paper>
  )
}

export function LaboratorioValidacao() {
  return (
    <Container maxWidth="xl" sx={{ py: { xs: 3, md: 5 } }}>
      <Stack spacing={3}>
        <Box>
          <Typography variant="overline" color="primary.main" fontWeight={700}>MINI-LABORATÓRIO</Typography>
          <Typography component="h1" variant="h3" fontWeight={800} sx={{ fontSize: { xs: '2rem', md: '2.6rem' } }}>
            Três schemas, um formulário
          </Typography>
          <Typography color="text.secondary" sx={{ mt: 1, maxWidth: 850 }}>
            Preencha os mesmos campos e valide cada versão. Tente enviar tudo vazio, corrija os erros e compare as mensagens.
            Esta demonstração não chama a API e não grava dados.
          </Typography>
        </Box>

        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: 'repeat(3, minmax(0, 1fr))' }, gap: 2, alignItems: 'stretch' }}>
          <FormularioDemonstracao<BeneficiarioZod>
            biblioteca="Zod + React Hook Form"
            descricao="O tipo do formulário vem do schema com z.infer."
            trechoCodigo="useForm({ resolver: zodResolver(schema) })"
            resolver={zodResolver(beneficiarioZod)}
          />
          <FormularioDemonstracao<BeneficiarioYup>
            biblioteca="Yup + React Hook Form"
            descricao="O tipo de saída pode ser derivado com yup.InferType."
            trechoCodigo="useForm({ resolver: yupResolver(schema) })"
            resolver={yupResolver(beneficiarioYup)}
          />
          <FormularioDemonstracao<BeneficiarioJoi>
            biblioteca="Joi + React Hook Form"
            descricao="O contrato TypeScript é declarado separadamente do schema."
            trechoCodigo="useForm({ resolver: joiResolver(schema) })"
            resolver={joiResolver(beneficiarioJoi)}
          />
        </Box>

        <Button component="a" href="/" variant="text" sx={{ alignSelf: 'flex-start' }}>
          Voltar à aplicação SocialConnect
        </Button>
      </Stack>
    </Container>
  )
}
