import { useState, useEffect } from 'react';
import cuentasService from '../services/cuentasService';

export const useCuentas = () => {
  const [cuentas, setCuentas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCuentas = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await cuentasService.getAll();
        setCuentas(data);
      } catch (err) {
        setError(err.message || 'Error al cargar las cuentas');
        console.error('Error fetching cuentas:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchCuentas();
  }, []);

  return { cuentas, loading, error };
};
