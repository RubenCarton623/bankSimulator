import { API_ENDPOINTS } from '../config/api';

const movimientosService = {
  // Obtener todos los movimientos
  getAll: async () => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS);
      if (!response.ok) {
        throw new Error('Error al obtener los movimientos');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getAll:', error);
      throw error;
    }
  },

  // Obtener movimiento por ID
  getById: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS_BY_ID(id));
      if (!response.ok) {
        throw new Error(`Error al obtener el movimiento con ID ${id}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getById:', error);
      throw error;
    }
  },

  // Obtener movimientos por cuenta ID
  getByCuenta: async (cuentaId) => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS_BY_CUENTA(cuentaId));
      if (!response.ok) {
        throw new Error(`Error al obtener los movimientos de la cuenta ${cuentaId}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getByCuenta:', error);
      throw error;
    }
  },

  // Buscar movimientos
  buscar: async (filtro) => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS_BUSCAR(encodeURIComponent(filtro)));
      if (!response.ok) {
        throw new Error('Error al buscar movimientos');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en buscar:', error);
      throw error;
    }
  },

  // Crear movimiento
  create: async (movimientoData) => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(movimientoData),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al crear el movimiento');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en create:', error);
      throw error;
    }
  },

  // Eliminar movimiento
  delete: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.MOVIMIENTOS_BY_ID(id), {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(`Error al eliminar el movimiento con ID ${id}`);
      }
      return true;
    } catch (error) {
      console.error('Error en delete:', error);
      throw error;
    }
  },

  // Obtener reporte de movimientos
  getReporte: async (clienteId, fechaInicio, fechaFin) => {
    try {
      const response = await fetch(API_ENDPOINTS.REPORTES(clienteId, fechaInicio, fechaFin));
      if (!response.ok) {
        throw new Error('Error al obtener el reporte');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getReporte:', error);
      throw error;
    }
  },

  // Descargar reporte PDF
  downloadReportePDF: async (clienteId, fechaInicio, fechaFin) => {
    try {
      const response = await fetch(API_ENDPOINTS.REPORTES_PDF(clienteId, fechaInicio, fechaFin));
      if (!response.ok) {
        throw new Error('Error al generar el reporte PDF');
      }
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `reporte-movimientos-${clienteId}-${new Date().toISOString().split('T')[0]}.pdf`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
      return true;
    } catch (error) {
      console.error('Error en downloadReportePDF:', error);
      throw error;
    }
  },
};

export default movimientosService;
