import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createTheme, CssBaseline, ThemeProvider } from '@mui/material'
import App from './App.tsx'

const tema = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#176b43', dark: '#105334', contrastText: '#ffffff' },
    secondary: { main: '#465c4f' },
    background: { default: '#f5f8f5', paper: '#ffffff' },
    error: { main: '#b42318' },
  },
  shape: { borderRadius: 10 },
  typography: {
    fontFamily: 'Inter, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif',
    button: { textTransform: 'none', fontWeight: 700 },
  },
  components: {
    MuiButtonBase: {
      styleOverrides: {
        root: {
          '&:focus-visible': { outline: '3px solid #0b5cad', outlineOffset: 3 },
        },
      },
    },
    MuiTextField: { defaultProps: { fullWidth: true, size: 'medium' } },
    MuiPaper: { styleOverrides: { rounded: { borderRadius: 14 } } },
  },
})

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <ThemeProvider theme={tema}>
      <CssBaseline />
      <App />
    </ThemeProvider>
  </StrictMode>,
)
