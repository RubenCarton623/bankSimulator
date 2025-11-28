import clientesService from '../../services/clientesService';
import { API_ENDPOINTS } from '../../config/api';

global.fetch = jest.fn();

describe('clientesService', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  describe('getAll', () => {
    test('fetches all clientes successfully', async () => {
      const mockClientes = [
        { clienteId: 1, nombre: 'Juan Pérez' },
        { clienteId: 2, nombre: 'María García' }
      ];

      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockClientes
      });

      const result = await clientesService.getAll();

      expect(fetch).toHaveBeenCalledWith(API_ENDPOINTS.CLIENTES);
      expect(result).toEqual(mockClientes);
    });

    test('throws error when fetch fails', async () => {
      fetch.mockResolvedValueOnce({
        ok: false
      });

      await expect(clientesService.getAll()).rejects.toThrow('Error al obtener los clientes');
    });
  });

  describe('getById', () => {
    test('fetches cliente by id successfully', async () => {
      const mockCliente = { clienteId: 1, nombre: 'Juan Pérez' };

      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockCliente
      });

      const result = await clientesService.getById(1);

      expect(fetch).toHaveBeenCalledWith(API_ENDPOINTS.CLIENTES_BY_ID(1));
      expect(result).toEqual(mockCliente);
    });

    test('throws error when cliente not found', async () => {
      fetch.mockResolvedValueOnce({
        ok: false
      });

      await expect(clientesService.getById(999)).rejects.toThrow('Error al obtener el cliente con ID 999');
    });
  });

  describe('create', () => {
    test('creates cliente successfully', async () => {
      const newCliente = { nombre: 'Pedro López', genero: 'M', edad: 30 };
      const createdCliente = { clienteId: 3, ...newCliente };

      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => createdCliente
      });

      const result = await clientesService.create(newCliente);

      expect(fetch).toHaveBeenCalledWith(API_ENDPOINTS.CLIENTES, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(newCliente),
      });
      expect(result).toEqual(createdCliente);
    });

    test('throws error when creation fails', async () => {
      fetch.mockResolvedValueOnce({
        ok: false,
        json: async () => ({ message: 'Validation error' })
      });

      await expect(clientesService.create({})).rejects.toThrow();
    });
  });

  describe('update', () => {
    test('updates cliente successfully', async () => {
      const updatedData = { nombre: 'Juan Pérez Actualizado' };
      const updatedCliente = { clienteId: 1, ...updatedData };

      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => updatedCliente
      });

      const result = await clientesService.update(1, updatedData);

      expect(fetch).toHaveBeenCalledWith(API_ENDPOINTS.CLIENTES_BY_ID(1), {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData),
      });
      expect(result).toEqual(updatedCliente);
    });
  });

  describe('delete', () => {
    test('deletes cliente successfully', async () => {
      fetch.mockResolvedValueOnce({
        ok: true
      });

      const result = await clientesService.delete(1);

      expect(fetch).toHaveBeenCalledWith(API_ENDPOINTS.CLIENTES_BY_ID(1), {
        method: 'DELETE',
      });
      expect(result).toBe(true);
    });

    test('throws error when deletion fails', async () => {
      fetch.mockResolvedValueOnce({
        ok: false
      });

      await expect(clientesService.delete(999)).rejects.toThrow('Error al eliminar el cliente con ID 999');
    });
  });
});
