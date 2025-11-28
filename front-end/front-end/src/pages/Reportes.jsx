import React, { useState, useMemo } from 'react';
import { Button, Dropdown } from '../components/atoms';
import DateField from '../components/atoms/DateField/DateField';
import { Table } from '../components/atoms';
import movimientosService from '../services/movimientosService';
import { useClientes } from '../hooks/useClientes';
import { useToast } from '../context/ToastContext';
import { MdSearch, MdDownload, MdDataObject } from 'react-icons/md';
import './page.css';
import { formatDate } from '../utils/dateUtils';

const Reportes = () => {
  const [clienteId, setClienteId] = useState('');
  const [fechaInicio, setFechaInicio] = useState('');
  const [fechaFin, setFechaFin] = useState('');
  const [reporteData, setReporteData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const { clientes, loading: loadingClientes } = useClientes();
  const toast = useToast();

  const handleFechaInicioChange = (e) => {
    const newFechaInicio = e.target.value;
    setFechaInicio(newFechaInicio);
    
    // Si ya hay una fecha fin y la nueva fecha inicio es mayor, mostrar error
    if (fechaFin && newFechaInicio && new Date(newFechaInicio) > new Date(fechaFin)) {
      toast.error('La fecha de inicio no puede ser mayor a la fecha fin');
    }
  };

  const handleFechaFinChange = (e) => {
    const newFechaFin = e.target.value;
    setFechaFin(newFechaFin);
    
    // Si ya hay una fecha inicio y la nueva fecha fin es menor, mostrar error
    if (fechaInicio && newFechaFin && new Date(newFechaFin) < new Date(fechaInicio)) {
      toast.error('La fecha fin no puede ser menor a la fecha de inicio');
    }
  };

  const clienteOptions = useMemo(() => {
    if (clientes && clientes.length > 0) {
      return clientes.map(cliente => ({
        value: cliente.clienteId,
        label: `${cliente.nombre} - ${cliente.identificacion}`
      }));
    }
    return [];
  }, [clientes]);

  const handleBuscar = async (e) => {
    e.preventDefault();
    
    if (!clienteId || !fechaInicio || !fechaFin) {
      toast.error('Todos los campos son obligatorios');
      return;
    }

    // Validar que fechaFin sea mayor o igual a fechaInicio
    if (new Date(fechaFin) < new Date(fechaInicio)) {
      toast.error('La fecha fin debe ser mayor o igual a la fecha inicio');
      return;
    }

    try {
      setIsLoading(true);
      // Formatear fechas para el endpoint
      const fechaInicioFormatted = new Date(fechaInicio).toISOString();
      const fechaFinFormatted = new Date(fechaFin).toISOString();
      
      const data = await movimientosService.getReporte(
        clienteId,
        fechaInicioFormatted,
        fechaFinFormatted
      );
      
      setReporteData(data);
      
      if (data.length === 0) {
        toast.info('No se encontraron movimientos para los criterios seleccionados');
      } else {
        toast.success(`Se encontraron ${data.length} movimiento(s)`);
      }
    } catch (error) {
      toast.error('Error al obtener el reporte: ' + error.message);
      console.error('Error:', error);
      setReporteData([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDownloadJSON = () => {
    if (reporteData.length === 0) {
      toast.warning('No hay datos para exportar');
      return;
    }

    try {
      const dataStr = JSON.stringify(reporteData, null, 2);
      const dataBlob = new Blob([dataStr], { type: 'application/json' });
      const url = window.URL.createObjectURL(dataBlob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `reporte-movimientos-${clienteId}-${new Date().toISOString().split('T')[0]}.json`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      toast.success('Reporte JSON descargado exitosamente');
    } catch (error) {
      toast.error('Error al descargar el JSON: ' + error.message);
    }
  };

  const handleDownloadPDF = async () => {
    if (!clienteId || !fechaInicio || !fechaFin) {
      toast.error('Debe realizar una búsqueda antes de generar el PDF');
      return;
    }

    try {
      setIsLoading(true);
      const fechaInicioFormatted = new Date(fechaInicio).toISOString();
      const fechaFinFormatted = new Date(fechaFin).toISOString();
      
      await movimientosService.downloadReportePDF(
        clienteId,
        fechaInicioFormatted,
        fechaFinFormatted
      );
      
      toast.success('Reporte PDF descargado exitosamente');
    } catch (error) {
      toast.error('Error al generar el PDF: ' + error.message);
      console.error('Error:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const columns = [
    {
      header: 'Fecha',
      accessor: 'fecha', 
      render: (value) => formatDate(value)
    },
    {
      header: 'Cliente',
      accessor: 'cliente'
    },
    {
      header: 'Número de Cuenta',
      accessor: 'numeroCuenta'
    },
    {
      header: 'Tipo',
      accessor: 'tipo'
    },
    {
      header: 'Saldo Inicial',
      accessor: 'saldoInicial',
      cell: (row) => `$${row.saldoInicial.toLocaleString('es-ES', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    },
    {
      header: 'Movimiento',
      accessor: 'movimiento',
      cell: (row) => {
        const isPositive = row.movimiento >= 0;
        return (
          <span style={{ color: isPositive ? 'green' : 'red', fontWeight: '600' }}>
            {isPositive ? '+' : ''}${row.movimiento.toLocaleString('es-ES', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
          </span>
        );
      }
    },
    {
      header: 'Saldo Disponible',
      accessor: 'saldoDisponible',
      cell: (row) => `$${row.saldoDisponible.toLocaleString('es-ES', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    },
    {
      header: 'Estado',
      accessor: 'estado',
      render: (value) => (
        <span className={`status-badge ${value ? 'status-active' : 'status-inactive'}`}>
          {value ? 'Activo' : 'Inactivo'}
        </span>
      )
    }
  ];

  return (
    <div className="page-container">
      <div className="page-header">
        <h1 className="page-title">Reportes de Movimientos</h1>
      </div>

      <div className="filters-section">
        <form onSubmit={handleBuscar} className="filters-form">
          <div className="filters-grid">
            <div className="filter-field">
              <label className="filter-label">Cliente</label>
              <Dropdown
                value={clienteId}
                onChange={(e) => setClienteId(e.target.value)}
                options={clienteOptions}
                placeholder="Seleccionar cliente"
                disabled={loadingClientes || isLoading}
              />
            </div>

            <div className="filter-field">
              <DateField
                label="Fecha Inicio"
                value={fechaInicio}
                onChange={handleFechaInicioChange}
                max={fechaFin || undefined}
                disabled={isLoading}
              />
            </div>

            <div className="filter-field">
              <DateField
                label="Fecha Fin"
                value={fechaFin}
                onChange={handleFechaFinChange}
                min={fechaInicio || undefined}
                disabled={isLoading}
              />
            </div>
          </div>

          <div className="filters-actions">
            <Button
              type="submit"
              variant="primary"
              disabled={isLoading || loadingClientes}
            >
              <MdSearch size={20} />
              {isLoading ? 'Buscando...' : 'Buscar'}
            </Button>

            <Button
              type="button"
              variant="secondary"
              onClick={handleDownloadJSON}
              disabled={isLoading || reporteData.length === 0}
            >
              <MdDataObject size={20} />
              Descargar JSON
            </Button>

            <Button
              type="button"
              variant="secondary"
              onClick={handleDownloadPDF}
              disabled={isLoading || !clienteId || !fechaInicio || !fechaFin}
            >
              <MdDownload size={20} />
              Descargar PDF
            </Button>
          </div>
        </form>
      </div>

      <div className="table-container">
        {isLoading ? (
          <div className="loading-state">Cargando reporte...</div>
        ) : reporteData.length > 0 ? (
          <Table
            columns={columns}
            data={reporteData}
          />
        ) : (
          <div className="empty-state">
            <p>No hay datos para mostrar. Seleccione los filtros y presione "Buscar"</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default Reportes;
