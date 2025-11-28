import { renderHook, waitFor } from '@testing-library/react';
import { useClientes } from '../../hooks/useClientes';
import clientesService from '../../services/clientesService';

jest.mock('../../services/clientesService');

describe('useClientes Hook', () => {
  const mockClientes = [
    { clienteId: 1, nombre: 'Juan Pérez', identificacion: '1234567890' },
    { clienteId: 2, nombre: 'María García', identificacion: '0987654321' }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('fetches clientes successfully', async () => {
    clientesService.getAll.mockResolvedValue(mockClientes);

    const { result } = renderHook(() => useClientes());

    expect(result.current.loading).toBe(true);
    expect(result.current.clientes).toEqual([]);

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.clientes).toEqual(mockClientes);
    expect(result.current.error).toBe(null);
  });

  test('handles error when fetching clientes fails', async () => {
    const errorMessage = 'Error al cargar los clientes';
    clientesService.getAll.mockRejectedValue(new Error(errorMessage));

    const { result } = renderHook(() => useClientes());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.error).toBe(errorMessage);
    expect(result.current.clientes).toEqual([]);
  });

  test('calls clientesService.getAll on mount', async () => {
    clientesService.getAll.mockResolvedValue(mockClientes);

    renderHook(() => useClientes());

    await waitFor(() => {
      expect(clientesService.getAll).toHaveBeenCalledTimes(1);
    });
  });
});
