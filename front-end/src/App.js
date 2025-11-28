import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ModalProvider } from './context/ModalContext';
import { ToastProvider } from './context/ToastContext';
import MainLayout from './components/organisms/MainLayout';
import Clientes from './pages/Clientes';
import Cuentas from './pages/Cuentas';
import Movimientos from './pages/Movimientos';
import Reportes from './pages/Reportes';
import './App.css';

function App() {
  return (
    <Router>
      <ToastProvider>
        <ModalProvider>
          <div className="App">
            <Routes>
              <Route path="/" element={<MainLayout><Navigate to="/clientes" replace /></MainLayout>} />
              <Route path="/clientes" element={<MainLayout><Clientes /></MainLayout>} />
              <Route path="/cuentas" element={<MainLayout><Cuentas /></MainLayout>} />
              <Route path="/movimientos" element={<MainLayout><Movimientos /></MainLayout>} />
              <Route path="/reportes" element={<MainLayout><Reportes /></MainLayout>} />
              <Route path="*" element={<Navigate to="/clientes" replace />} />
            </Routes>
          </div>
        </ModalProvider>
      </ToastProvider>
    </Router>
  );
}

export default App;
