import { API_ENDPOINTS } from '../config/api';

const clientesService = {
  // Obtener todos los clientes
  getAll: async () => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES);
      if (!response.ok) {
        throw new Error('Error al obtener los clientes');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getAll:', error);
      throw error;
    }
  },

  // Obtener cliente por ID
  getById: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES_BY_ID(id));
      if (!response.ok) {
        throw new Error(`Error al obtener el cliente con ID ${id}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getById:', error);
      throw error;
    }
  },

  // Obtener cliente por identificación
  getByIdentificacion: async (identificacion) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES_BY_IDENTIFICACION(identificacion));
      if (!response.ok) {
        throw new Error(`Error al obtener el cliente con identificación ${identificacion}`);
      }
      return await response.json();
    } catch (error) {
      console.error('Error en getByIdentificacion:', error);
      throw error;
    }
  },

  // Buscar clientes
  buscar: async (filtro) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES_BUSCAR(encodeURIComponent(filtro)));
      if (!response.ok) {
        throw new Error('Error al buscar clientes');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en buscar:', error);
      throw error;
    }
  },

  // Crear cliente
  create: async (clienteData) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(clienteData),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al crear el cliente');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en create:', error);
      throw error;
    }
  },

  // Actualizar cliente
  update: async (id, clienteData) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES_BY_ID(id), {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(clienteData),
      });
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Error al actualizar el cliente');
      }
      return await response.json();
    } catch (error) {
      console.error('Error en update:', error);
      throw error;
    }
  },

  // Eliminar cliente
  delete: async (id) => {
    try {
      const response = await fetch(API_ENDPOINTS.CLIENTES_BY_ID(id), {
        method: 'DELETE',
      });
      if (!response.ok) {
        throw new Error(`Error al eliminar el cliente con ID ${id}`);
      }
      return true;
    } catch (error) {
      console.error('Error en delete:', error);
      throw error;
    }
  },
};

export default clientesService;
