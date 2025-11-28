import { useEffect } from 'react';
import { useModal } from '../context/ModalContext';

/**
 * Hook personalizado para manejar modales con query params
 * @param {string} modalKey - Nombre del query param (ej: 'editCliente')
 * @returns {object} - { isOpen, param, additionalProps, close }
 */
export const useModalQuery = (modalKey) => {
  const { isModalOpen, getModalParam, getAdditionalProps, closeModal } = useModal();

  const isOpen = isModalOpen(modalKey);
  const param = getModalParam(modalKey);
  const additionalProps = getAdditionalProps(modalKey);

  // Console log para debugging cuando el modal se abre
  useEffect(() => {
    if (isOpen && param) {
      console.log(`[Modal ${modalKey}] Query Param:`, param);
      console.log(`[Modal ${modalKey}] Additional Props:`, additionalProps);
    }
  }, [isOpen, param, additionalProps, modalKey]);

  return {
    isOpen,
    param,
    additionalProps,
    close: () => closeModal(modalKey),
  };
};
