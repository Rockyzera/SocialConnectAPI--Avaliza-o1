export function somenteDigitos(valor: string): string {
  return valor.replace(/\D/g, '')
}

export function formatarCpfCnpj(valor: string): string {
  const digitos = somenteDigitos(valor).slice(0, 14)

  if (digitos.length <= 11) {
    return digitos
      .replace(/^(\d{3})(\d)/, '$1.$2')
      .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
      .replace(/^(\d{3})\.(\d{3})\.(\d{3})(\d{1,2})$/, '$1.$2.$3-$4')
  }

  return digitos
    .replace(/^(\d{2})(\d)/, '$1.$2')
    .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})(\d)/, '$1.$2.$3/$4')
    .replace(/^(\d{2})\.(\d{3})\.(\d{3})\/(\d{4})(\d{1,2})$/, '$1.$2.$3/$4-$5')
}

export function formatarTelefone(valor: string): string {
  const digitos = somenteDigitos(valor).slice(0, 11)

  if (digitos.length === 0) return ''
  if (digitos.length <= 2) return `(${digitos}`

  const ddd = digitos.slice(0, 2)
  const numero = digitos.slice(2)
  if (digitos.length <= 6) return `(${ddd}) ${numero}`

  const tamanhoPrefixo = numero.length > 8 ? 5 : 4
  const prefixo = numero.slice(0, tamanhoPrefixo)
  const sufixo = numero.slice(tamanhoPrefixo)
  return `(${ddd}) ${prefixo}${sufixo ? `-${sufixo}` : ''}`
}
