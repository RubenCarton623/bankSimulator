import React, { createContext, useContext, useCallback } from 'react';
import { useNavigate, useLocation, useSearchParams } from 'react-router-dom';

const ModalContext = createContext();

export const useModal = () => {
  const context = useContext(ModalContext);
  if (!context) {
    throw new Error('useModal debe ser usado dentro de ModalProvider');
  }
  return context;
};

export const ModalProvider = ({ children }) => {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  // Store para props adicionales (se mantiene en memoria)
  const additionalPropsRef = React.useRef({});

  /**
   * Abre un modal agregando un query param
   * @param {string} modalKey - Nombre del query param (ej: 'editCliente')
   * @param {string|number} value - Valor del query param (ej: ID del registro)
   * @param {object} additionalProps - Props adicionales que se pasan al modal
   */
  const openModal = useCallback((modalKey, value, additionalProps = {}) => {
    const params = new URLSearchParams(location.search);
    params.set(modalKey, value);
    
    // Guardar props adicionales en memoria
    const storageKey = `${modalKey}_${value}`;
    additionalPropsRef.current[storageKey] = additionalProps;
    
    navigate(`${location.pathname}?${params.toString()}`, { replace: true });
  }, [navigate, location]);

  /**
   * Cierra un modal removiendo el query param
   * @param {string} modalKey - Nombre del query param a remover
   */
  const closeModal = useCallback((modalKey) => {
    const params = new URLSearchParams(location.search);
    const value = params.get(modalKey);
    
    // Limpiar props adicionales
    if (value) {
      const storageKey = `${modalKey}_${value}`;
      delete additionalPropsRef.current[storageKey];
    }
    
    params.delete(modalKey);
    navigate(`${location.pathname}?${params.toString()}`, { replace: true });
  }, [navigate, location]);

  /**
   * Verifica si un modal está abierto
   * @param {string} modalKey - Nombre del query param
   * @returns {boolean}
   */
  const isModalOpen = useCallback((modalKey) => {
    return searchParams.has(modalKey);
  }, [searchParams]);

  /**
   * Obtiene el valor del query param del modal
   * @param {string} modalKey - Nombre del query param
   * @returns {string|null}
   */
  const getModalParam = useCallback((modalKey) => {
    return searchParams.get(modalKey);
  }, [searchParams]);

  /**
   * Obtiene los props adicionales del modal
   * @param {string} modalKey - Nombre del query param
   * @returns {object}
   */
  const getAdditionalProps = useCallback((modalKey) => {
    const value = searchParams.get(modalKey);
    if (!value) return {};
    
    const storageKey = `${modalKey}_${value}`;
    return additionalPropsRef.current[storageKey] || {};
  }, [searchParams]);

  const value = {
    openModal,
    closeModal,
    isModalOpen,
    getModalParam,
    getAdditionalProps,
  };

  return (
    <ModalContext.Provider value={value}>
      {children}
    </ModalContext.Provider>
  );
};
