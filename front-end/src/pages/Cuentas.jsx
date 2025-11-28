import React, { useState, useEffect } from 'react';
import { Table, InputText, Button } from '../components/atoms';
import Modal from '../components/atoms/Modal/Modal';
import ConfirmModal from '../components/atoms/ConfirmModal/ConfirmModal';
import CuentaForm from '../components/forms/CuentaForm/CuentaForm';
import { MdEdit, MdDelete, MdClose, MdAdd } from 'react-icons/md';
import cuentasService from '../services/cuentasService';
import useDebounce from '../hooks/useDebounce';
import { useModal } from '../context/ModalContext';
import { useModalQuery } from '../hooks/useModalQuery';
import { useToast } from '../context/ToastContext';
import './page.css';

const Cuentas = () => {
  const [cuentas, setCuentas] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchText, setSearchText] = useState('');
  const debouncedSearchText = useDebounce(searchText, 500);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  
  const { openModal } = useModal();
  const createModal = useModalQuery('createCuenta');
  const editModal = useModalQuery('editCuenta');
  const deleteModal = useModalQuery('deleteCuenta');
  const toast = useToast();

  useEffect(() => {
    loadCuentas();
  }, []);

  useEffect(() => {
    if (debouncedSearchText) {
      searchCuentas(debouncedSearchText);
    } else {
      loadCuentas();
    }
  }, [debouncedSearchText]);

  const loadCuentas = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await cuentasService.getAll();
      setCuentas(data);
    } catch (err) {
      setError(err.message);
      console.error('Error al cargar cuentas:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const searchCuentas = async (filtro) => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await cuentasService.buscar(filtro);
      setCuentas(data);
    } catch (err) {
      setError(err.message);
      setCuentas([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreate = () => {
    openModal('createCuenta', 'new', {});
  };

  const handleEdit = (cuenta) => {
    openModal('editCuenta', cuenta.id, { 
      cuentaData: cuenta 
    });
  };

  const handleDelete = (cuenta) => {
    openModal('deleteCuenta', cuenta.id, { 
      cuentaData: cuenta 
    });
  };

  const handleFormSubmit = async (data) => {
    setFormLoading(true);
    try {
      if (editModal.isOpen) {
        await cuentasService.update(editModal.param, data);
        toast.success('Cuenta actualizada exitosamente');
        editModal.close();
      } else if (createModal.isOpen) {
        await cuentasService.create(data);
        toast.success('Cuenta creada exitosamente');
        createModal.close();
      }
      loadCuentas();
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
      await cuentasService.delete(deleteModal.param);
      
      // Eliminar de la tabla
      setCuentas(prev => prev.filter(c => c.id !== parseInt(deleteModal.param)));
      
      toast.success(`Cuenta "${deleteModal.additionalProps.cuentaData?.numeroCuenta}" eliminada exitosamente`);
      deleteModal.close();
    } catch (err) {
      toast.error(`Error al eliminar cuenta: ${err.message}`);
    } finally {
      setDeleteLoading(false);
    }
  };

  const columns = [
    {
      header: 'Número de Cuenta',
      accessor: 'numeroCuenta'
    },
    {
      header: 'Tipo de Cuenta',
      accessor: 'tipoCuenta',
      render: (value) => (
        <span className={`badge badge-${value.toLowerCase()}`}>
          {value}
        </span>
      )
    },
    {
      header: 'Saldo Inicial',
      accessor: 'saldoInicial',
      render: (value) => `$${parseFloat(value).toFixed(2)}`
    },
    {
      header: 'Estado',
      accessor: 'estado',
      render: (value) => (
        <span className={`badge ${value ? 'badge-active' : 'badge-inactive'}`}>
          {value ? 'Activo' : 'Inactivo'}
        </span>
      )
    },
    {
      header: 'Cliente',
      accessor: 'clienteId',
      render: (value, row) => row.cliente 
        ? `${row.cliente.clienteId} - ${row.cliente.nombre}` 
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
          <h1 className="page-title">Cuentas</h1>
          <p className="page-description">Gestión de cuentas bancarias</p>
        </div>
        <Button variant="primary" onClick={handleCreate}>
          <MdAdd /> Nueva Cuenta
        </Button>
      </div>

      <div className="search-container">
        <InputText
          placeholder="Buscar cuenta..."
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
          <button onClick={loadCuentas} className="retry-button">
            Reintentar
          </button>
        </div>
      )}

      <div className="page-content">
        <Table
          columns={columns}
          data={cuentas}
          isLoading={isLoading}
          emptyMessage="No hay cuentas registradas"
        />
      </div>

      {/* Modal de creación */}
      <Modal
        isOpen={createModal.isOpen}
        onClose={createModal.close}
        title="Crear Nueva Cuenta"
        size="large"
      >
        <CuentaForm
          onSubmit={handleFormSubmit}
          onCancel={createModal.close}
          isLoading={formLoading}
        />
      </Modal>

      {/* Modal de edición */}
      <Modal
        isOpen={editModal.isOpen}
        onClose={editModal.close}
        title="Editar Cuenta"
        size="large"
      >
        <CuentaForm
          defaultValues={editModal.additionalProps.cuentaData}
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
        title="Eliminar Cuenta"
        message={`¿Estás seguro que deseas eliminar la cuenta "${deleteModal.additionalProps.cuentaData?.numeroCuenta}"?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        isLoading={deleteLoading}
      />
    </div>
  );
};

export default Cuentas;
