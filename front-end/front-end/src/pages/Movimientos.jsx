import React, { useState, useEffect } from 'react';
import { Table, InputText, Button } from '../components/atoms';
import Modal from '../components/atoms/Modal/Modal';
import ConfirmModal from '../components/atoms/ConfirmModal/ConfirmModal';
import MovimientoForm from '../components/forms/MovimientoForm/MovimientoForm';
import { MdEdit, MdDelete, MdClose, MdAdd } from 'react-icons/md';
import movimientosService from '../services/movimientosService';
import useDebounce from '../hooks/useDebounce';
import { formatDate } from '../utils/dateUtils';
import { useModal } from '../context/ModalContext';
import { useModalQuery } from '../hooks/useModalQuery';
import { useToast } from '../context/ToastContext';
import './page.css';

const Movimientos = () => {
  const [movimientos, setMovimientos] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchText, setSearchText] = useState('');
  const debouncedSearchText = useDebounce(searchText, 500);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  
  const { openModal } = useModal();
  const createModal = useModalQuery('createMovimiento');
  const editModal = useModalQuery('editMovimiento');
  const deleteModal = useModalQuery('deleteMovimiento');
  const toast = useToast();

  useEffect(() => {
    loadMovimientos();
  }, []);

  useEffect(() => {
    if (debouncedSearchText) {
      searchMovimientos(debouncedSearchText);
    } else {
      loadMovimientos();
    }
  }, [debouncedSearchText]);

  const loadMovimientos = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await movimientosService.getAll();
      setMovimientos(data);
    } catch (err) {
      setError(err.message);
      console.error('Error al cargar movimientos:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const searchMovimientos = async (filtro) => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await movimientosService.buscar(filtro);
      setMovimientos(data);
    } catch (err) {
      setError(err.message);
      setMovimientos([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreate = () => {
    openModal('createMovimiento', 'new', {});
  };

  const handleEdit = (movimiento) => {
    openModal('editMovimiento', movimiento.id, { 
      movimientoData: movimiento 
    });
  };

  const handleDelete = (movimiento) => {
    openModal('deleteMovimiento', movimiento.id, { 
      movimientoData: movimiento 
    });
  };

  const handleFormSubmit = async (data) => {
    setFormLoading(true);
    try {
      if (editModal.isOpen) {
        await movimientosService.update(editModal.param, data);
        toast.success('Movimiento actualizado exitosamente');
        editModal.close();
      } else if (createModal.isOpen) {
        await movimientosService.create(data);
        toast.success('Movimiento creado exitosamente');
        createModal.close();
      }
      loadMovimientos();
    } catch (err) {
      toast.error(`Error: ${err.message}`);
    } finally {
      setFormLoading(false);
    }
  };

  const confirmDelete = async () => {
    if (!deleteModal.isOpen || !deleteModal.param) return;
    
    setDeleteLoading(true);
    try {
      await movimientosService.delete(deleteModal.param);
      
      // Eliminar de la tabla
      setMovimientos(prev => prev.filter(m => m.id !== parseInt(deleteModal.param)));
      
      toast.success(`Movimiento "${deleteModal.additionalProps.movimientoData?.tipoMovimiento}" eliminado exitosamente`);
      deleteModal.close();
    } catch (err) {
      toast.error(`Error al eliminar movimiento: ${err.message}`);
    } finally {
      setDeleteLoading(false);
    }
  };

  const columns = [
    {
      header: 'Fecha',
      accessor: 'fecha',
      render: (value) => formatDate(value)
    },
    {
      header: 'Tipo de Movimiento',
      accessor: 'tipoMovimiento',
      render: (value) => (
        <span className={`badge badge-${value.toLowerCase()}`}>
          {value}
        </span>
      )
    },
    {
      header: 'Valor',
      accessor: 'valor',
      render: (value) => `$${parseFloat(value).toFixed(2)}`
    },
    {
      header: 'Saldo',
      accessor: 'saldo',
      render: (value) => `$${parseFloat(value).toFixed(2)}`
    },
    {
      header: 'Cuenta',
      accessor: 'cuentaId',
      render: (value, row) => row.cuenta 
        ? `${row.cuenta.id} - ${row.cuenta.numeroCuenta}` 
        : value
    },
    {
      header: 'Acciones',
      accessor: 'id',
      render: (value, row) => (
        <div className="action-buttons">
          <Button 
            variant="outline" 
            size="small"
            onClick={() => handleEdit(row)}
          >
            <MdEdit /> Editar
          </Button>
          <Button 
            variant="ghost" 
            size="small"
            onClick={() => handleDelete(row)}
          >
            <MdDelete /> Eliminar
          </Button>
        </div>
      )
    }
  ];

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1 className="page-title">Movimientos</h1>
          <p className="page-description">Historial de movimientos bancarios</p>
        </div>
        <Button variant="primary" onClick={handleCreate}>
          <MdAdd /> Nuevo Movimiento
        </Button>
      </div>

      <div className="search-container">
        <InputText
          placeholder="Buscar movimiento..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        {searchText && (
          <Button variant="ghost" size="small" onClick={() => setSearchText('')}>
            <MdClose /> Limpiar
          </Button>
        )}
      </div>

      {error && (
        <div className="error-message">
          <p>Error: {error}</p>
          <button onClick={loadMovimientos} className="retry-button">
            Reintentar
          </button>
        </div>
      )}

      <div className="page-content">
        <Table
          columns={columns}
          data={movimientos}
          isLoading={isLoading}
          emptyMessage="No hay movimientos registrados"
        />
      </div>

      {/* Modal de creación */}
      <Modal
        isOpen={createModal.isOpen}
        onClose={createModal.close}
        title="Crear Nuevo Movimiento"
        size="large"
      >
        <MovimientoForm
          onSubmit={handleFormSubmit}
          onCancel={createModal.close}
          isLoading={formLoading}
        />
      </Modal>

      {/* Modal de edición */}
      <Modal
        isOpen={editModal.isOpen}
        onClose={editModal.close}
        title="Editar Movimiento"
        size="large"
      >
        <MovimientoForm
          defaultValues={editModal.additionalProps.movimientoData}
          onSubmit={handleFormSubmit}
          onCancel={editModal.close}
          isLoading={formLoading}
        />
      </Modal>

      {/* Modal de confirmación de eliminación */}
      <ConfirmModal
        isOpen={deleteModal.isOpen}
        onClose={deleteModal.close}
        onConfirm={confirmDelete}
        title="Eliminar Movimiento"
        message={`¿Estás seguro que deseas eliminar el movimiento "${deleteModal.additionalProps.movimientoData?.tipoMovimiento}"?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        isLoading={deleteLoading}
      />
    </div>
  );
};

export default Movimientos;
