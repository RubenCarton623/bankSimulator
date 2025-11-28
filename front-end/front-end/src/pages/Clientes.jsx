import React, { useState, useEffect } from 'react';
import { Table, InputText, Button } from '../components/atoms';
import Modal from '../components/atoms/Modal/Modal';
import ConfirmModal from '../components/atoms/ConfirmModal/ConfirmModal';
import ClienteForm from '../components/forms/ClienteForm/ClienteForm';
import { MdEdit, MdDelete, MdClose, MdAdd } from 'react-icons/md';
import clientesService from '../services/clientesService';
import useDebounce from '../hooks/useDebounce';
import { useModal } from '../context/ModalContext';
import { useModalQuery } from '../hooks/useModalQuery';
import { useToast } from '../context/ToastContext';
import './page.css';

const Clientes = () => {
  const [clientes, setClientes] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchText, setSearchText] = useState('');
  const debouncedSearchText = useDebounce(searchText, 500);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  
  // Modal management
  const { openModal } = useModal();
  const createModal = useModalQuery('createCliente');
  const editModal = useModalQuery('editCliente');
  const deleteModal = useModalQuery('deleteCliente');
  const toast = useToast();

  useEffect(() => {
    loadClientes();
  }, []);

  useEffect(() => {
    if (debouncedSearchText) {
      searchClientes(debouncedSearchText);
    } else {
      loadClientes();
    }
  }, [debouncedSearchText]);

  const loadClientes = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await clientesService.getAll();
      setClientes(data);
    } catch (err) {
      setError(err.message);
      console.error('Error al cargar clientes:', err);
    } finally {
      setIsLoading(false);
    }
  };

  const searchClientes = async (filtro) => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await clientesService.buscar(filtro);
      setClientes(data);
    } catch (err) {
      setError(err.message);
      setClientes([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCreate = () => {
    openModal('createCliente', 'new', {});
  };

  const handleEdit = (cliente) => {
    openModal('editCliente', cliente.clienteId, { 
      clienteData: cliente 
    });
  };

  const handleDelete = (cliente) => {
    openModal('deleteCliente', cliente.clienteId, { 
      clienteData: cliente 
    });
  };

  const handleFormSubmit = async (data) => {
    setFormLoading(true);
    try {
      if (editModal.isOpen) {
        await clientesService.update(editModal.param, data);
        toast.success('Cliente actualizado exitosamente');
        editModal.close();
      } else if (createModal.isOpen) {
        await clientesService.create(data);
        toast.success('Cliente creado exitosamente');
        createModal.close();
      }
      loadClientes();
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
      await clientesService.delete(deleteModal.param);
      
      // Eliminar de la tabla
      setClientes(prev => prev.filter(c => c.clienteId !== parseInt(deleteModal.param)));
      
      toast.success(`Cliente "${deleteModal.additionalProps.clienteData?.nombre}" eliminado exitosamente`);
      deleteModal.close();
    } catch (err) {
      toast.error(`Error al eliminar cliente: ${err.message}`);
    } finally {
      setDeleteLoading(false);
    }
  };

  const columns = [
    {
      header: 'Nombre',
      accessor: 'nombre'
    },
    {
      header: 'Género',
      accessor: 'genero'
    },
    {
      header: 'Edad',
      accessor: 'edad'
    },
    {
      header: 'Identificación',
      accessor: 'identificacion'
    },
    {
      header: 'Dirección',
      accessor: 'direccion'
    },
    {
      header: 'Teléfono',
      accessor: 'telefono'
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
      header: 'Acciones',
      accessor: 'clienteId',
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
          <h1 className="page-title">Clientes</h1>
          <p className="page-description">Gestión de clientes del banco</p>
        </div>
        <Button variant="primary" onClick={handleCreate}>
          <MdAdd /> Nuevo Cliente
        </Button>
      </div>

      <div className="search-container">
        <InputText
          placeholder="Buscar cliente..."
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
          <button onClick={loadClientes} className="retry-button">
            Reintentar
          </button>
        </div>
      )}

      <div className="page-content">
        <Table
          columns={columns}
          data={clientes}
          isLoading={isLoading}
          emptyMessage="No hay clientes registrados"
        />
      </div>

      {/* Modal de creación */}
      <Modal
        isOpen={createModal.isOpen}
        onClose={createModal.close}
        title="Crear Nuevo Cliente"
        size="large"
      >
        <ClienteForm
          onSubmit={handleFormSubmit}
          onCancel={createModal.close}
          isLoading={formLoading}
        />
      </Modal>

      {/* Modal de edición */}
      <Modal
        isOpen={editModal.isOpen}
        onClose={editModal.close}
        title="Editar Cliente"
        size="large"
      >
        <ClienteForm
          defaultValues={editModal.additionalProps.clienteData}
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
        title="Eliminar Cliente"
        message={`¿Estás seguro que deseas eliminar a "${deleteModal.additionalProps.clienteData?.nombre}"?`}
        confirmText="Eliminar"
        cancelText="Cancelar"
        isLoading={deleteLoading}
      />
    </div>
  );
};

export default Clientes;
