import { renderHook, waitFor } from '@testing-library/react';
import { useCuentas } from '../../hooks/useCuentas';
import cuentasService from '../../services/cuentasService';

jest.mock('../../services/cuentasService');

describe('useCuentas Hook', () => {
  const mockCuentas = [
    { id: 1, numeroCuenta: '12345678', tipoCuenta: 'Ahorros', saldoInicial: 1000 },
    { id: 2, numeroCuenta: '87654321', tipoCuenta: 'Corriente', saldoInicial: 5000 }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('fetches cuentas successfully', async () => {
    cuentasService.getAll.mockResolvedValue(mockCuentas);

    const { result } = renderHook(() => useCuentas());

    expect(result.current.loading).toBe(true);
    expect(result.current.cuentas).toEqual([]);

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.cuentas).toEqual(mockCuentas);
    expect(result.current.error).toBe(null);
  });

  test('handles error when fetching cuentas fails', async () => {
    const errorMessage = 'Error al cargar las cuentas';
    cuentasService.getAll.mockRejectedValue(new Error(errorMessage));

    const { result } = renderHook(() => useCuentas());

    await waitFor(() => {
      expect(result.current.loading).toBe(false);
    });

    expect(result.current.error).toBe(errorMessage);
    expect(result.current.cuentas).toEqual([]);
  });

  test('calls cuentasService.getAll on mount', async () => {
    cuentasService.getAll.mockResolvedValue(mockCuentas);

    renderHook(() => useCuentas());

    await waitFor(() => {
      expect(cuentasService.getAll).toHaveBeenCalledTimes(1);
    });
  });
});
