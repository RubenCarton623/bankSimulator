const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

export const API_ENDPOINTS = {
  // Clientes
  CLIENTES: `${API_BASE_URL}/clientes`,
  CLIENTES_BY_ID: (id) => `${API_BASE_URL}/clientes/${id}`,
  CLIENTES_BY_IDENTIFICACION: (identificacion) => `${API_BASE_URL}/clientes/identificacion/${identificacion}`,
  CLIENTES_BUSCAR: (filtro) => `${API_BASE_URL}/clientes/buscar?filtro=${filtro}`,
  
  // Cuentas
  CUENTAS: `${API_BASE_URL}/cuentas`,
  CUENTAS_BY_ID: (id) => `${API_BASE_URL}/cuentas/${id}`,
  CUENTAS_BY_NUMERO: (numero) => `${API_BASE_URL}/cuentas/numero/${numero}`,
  CUENTAS_BY_CLIENTE: (clienteId) => `${API_BASE_URL}/cuentas/cliente/${clienteId}`,
  CUENTAS_BUSCAR: (filtro) => `${API_BASE_URL}/cuentas/buscar?filtro=${filtro}`,
  
  // Movimientos
  MOVIMIENTOS: `${API_BASE_URL}/movimientos`,
  MOVIMIENTOS_BY_ID: (id) => `${API_BASE_URL}/movimientos/${id}`,
  MOVIMIENTOS_BY_CUENTA: (cuentaId) => `${API_BASE_URL}/movimientos/cuenta/${cuentaId}`,
  MOVIMIENTOS_BUSCAR: (filtro) => `${API_BASE_URL}/movimientos/buscar?filtro=${filtro}`,
  
  // Reportes
  REPORTES: (clienteId, fechaInicio, fechaFin) => `${API_BASE_URL}/movimientos/reportes?clienteId=${clienteId}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`,
  REPORTES_PDF: (clienteId, fechaInicio, fechaFin) => `${API_BASE_URL}/movimientos/reportes/pdf?clienteId=${clienteId}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`,
};

export default API_BASE_URL;
