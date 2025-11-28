import { useState, useEffect } from 'react';
import clientesService from '../services/clientesService';

export const useClientes = () => {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchClientes = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await clientesService.getAll();
        setClientes(data);
      } catch (err) {
        setError(err.message || 'Error al cargar los clientes');
        console.error('Error fetching clientes:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchClientes();
  }, []);

  return { clientes, loading, error };
};
