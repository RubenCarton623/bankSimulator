import movimientosService from '../../services/movimientosService';
import { API_ENDPOINTS } from '../../config/api';

global.fetch = jest.fn();

describe('movimientosService', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  describe('getReporte', () => {
    test('fetches reporte successfully', async () => {
      const mockReporte = [
        {
          fecha: '2025-11-28T01:37:12.730+00:00',
          cliente: 'Juan Pérez',
          numeroCuenta: 12221321,
          tipo: 'Ahorros',
          saldoInicial: 10000,
          estado: true,
          movimiento: 500,
          saldoDisponible: 10500
        }
      ];

      fetch.mockResolvedValueOnce({
        ok: true,
        json: async () => mockReporte
      });

      const result = await movimientosService.getReporte(1, '2025-11-01T00:00:00', '2025-11-30T23:59:59');

      expect(fetch).toHaveBeenCalledWith(
        API_ENDPOINTS.REPORTES(1, '2025-11-01T00:00:00', '2025-11-30T23:59:59')
      );
      expect(result).toEqual(mockReporte);
    });

    test('throws error when fetch fails', async () => {
      fetch.mockResolvedValueOnce({
        ok: false
      });

      await expect(
        movimientosService.getReporte(1, '2025-11-01', '2025-11-30')
      ).rejects.toThrow('Error al obtener el reporte');
    });
  });

  describe('downloadReportePDF', () => {
    test('downloads PDF successfully', async () => {
      const mockBlob = new Blob(['PDF content'], { type: 'application/pdf' });
      
      // Mock URL methods
      global.URL.createObjectURL = jest.fn(() => 'blob:mock-url');
      global.URL.revokeObjectURL = jest.fn();
      
      // Mock DOM methods
      const mockClick = jest.fn();
      const mockLink = {
        href: '',
        download: '',
        click: mockClick
      };
      
      jest.spyOn(document, 'createElement').mockReturnValue(mockLink);
      jest.spyOn(document.body, 'appendChild').mockImplementation(() => {});
      jest.spyOn(document.body, 'removeChild').mockImplementation(() => {});

      fetch.mockResolvedValueOnce({
        ok: true,
        blob: async () => mockBlob
      });

      const result = await movimientosService.downloadReportePDF(1, '2025-11-01', '2025-11-30');

      expect(fetch).toHaveBeenCalledWith(
        API_ENDPOINTS.REPORTES_PDF(1, '2025-11-01', '2025-11-30')
      );
      expect(mockClick).toHaveBeenCalled();
      expect(result).toBe(true);
      
      // Cleanup
      jest.restoreAllMocks();
    });

    test('throws error when PDF generation fails', async () => {
      fetch.mockResolvedValueOnce({
        ok: false
      });

      await expect(
        movimientosService.downloadReportePDF(1, '2025-11-01', '2025-11-30')
      ).rejects.toThrow('Error al generar el reporte PDF');
    });
  });
});
