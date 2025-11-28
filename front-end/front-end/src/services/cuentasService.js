import { API_ENDPOINTS } from '../config/api';

const cuentasService = {
  // Obtener todas las cuentas
  getAll: async () => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS);
      if (!response.ok) {
        throw new Error('Error al obtener las cuentas');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getAll:', error);
      throw error;
    }
  },

  // Obtener cuenta por ID
  getById: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BY_ID(id));
      if (!response.ok) {
        throw new Error(`Error al obtener la cuenta con ID ${id}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getById:', error);
      throw error;
    }
  },

  // Obtener cuenta por número
  getByNumero: async (numero) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BY_NUMERO(numero));
      if (!response.ok) {
        throw new Error(`Error al obtener la cuenta con número ${numero}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getByNumero:', error);
      throw error;
    }
  },

  // Obtener cuentas por cliente ID
  getByCliente: async (clienteId) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BY_CLIENTE(clienteId));
      if (!response.ok) {
        throw new Error(`Error al obtener las cuentas del cliente ${clienteId}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getByCliente:', error);
      throw error;
    }
  },

  // Buscar cuentas
  buscar: async (filtro) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BUSCAR(encodeURIComponent(filtro)));
      if (!response.ok) {
        throw new Error('Error al buscar cuentas');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en buscar:', error);
      throw error;
    }
  },

  // Crear cuenta
  create: async (cuentaData) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(cuentaData),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al crear la cuenta');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en create:', error);
      throw error;
    }
  },

  // Actualizar cuenta
  update: async (id, cuentaData) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BY_ID(id), {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(cuentaData),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al actualizar la cuenta');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en update:', error);
      throw error;
    }
  },

  // Eliminar cuenta
  delete: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.CUENTAS_BY_ID(id), {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(`Error al eliminar la cuenta con ID ${id}`);
      }
      return true;
    } catch (error) {
      console.error('Error en delete:', error);
      throw error;
    }
  },
};

export default cuentasService;
