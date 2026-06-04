import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import './index.css'
import App from './App.tsx'
import { CharacterProvider } from './context/CharacterContext.tsx'
import {AlertProvider} from "./context/AlertContext.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <CharacterProvider>
        <AlertProvider>
          <BrowserRouter>
            <App />
          </BrowserRouter>
        </AlertProvider>
    </CharacterProvider>
  </StrictMode>,
)
