import React, { useEffect, useMemo } from 'react';
import { useForm } from 'react-hook-form';
import FormField from '../../molecules/FormField/FormField';
import Button from '../../atoms/Button/Button';
import { useCuentas } from '../../../hooks/useCuentas';
import './MovimientoForm.css';

const MovimientoForm = ({ 
  defaultValues = null, 
  onSubmit, 
  onCancel,
  isLoading = false 
}) => {
  const { cuentas, loading: loadingCuentas } = useCuentas();
  
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm({
    defaultValues: defaultValues || {
      tipoMovimiento: '',
      valor: '',
      cuentaId: ''
    }
  });

  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues);
    }
  }, [defaultValues, reset]);

  const tipoMovimientoOptions = [
    { value: 'Deposito', label: 'Deposito' },
    { value: 'Retiro', label: 'Retiro' },
    { value: 'Transferencia', label: 'Transferencia' }
  ];

  const cuentaOptions = useMemo(() => {
    if (cuentas && cuentas.length > 0) {
      return cuentas.map(cuenta => ({
        value: cuenta.id,
        label: `${cuenta.numeroCuenta} - ${cuenta.tipoCuenta} (${cuenta.estado ? 'Activa' : 'Inactiva'})`
      }));
    }
    return [];
  }, [cuentas]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="movimiento-form">
      <div className="form-grid">
        <FormField
          label="Tipo de Movimiento"
          name="tipoMovimiento"
          type="select"
          required
          error={errors.tipoMovimiento}
          register={register('tipoMovimiento', {
            required: 'El tipo de movimiento es obligatorio'
          })}
          options={tipoMovimientoOptions}
          placeholder="Seleccionar tipo de movimiento"
        />

        <FormField
          label="Valor"
          name="valor"
          type="number"
          required
          error={errors.valor}
          register={register('valor', {
            required: 'El valor es obligatorio',
            min: {
              value: 0.01,
              message: 'El valor debe ser mayor a 0'
            },
            valueAsNumber: true
          })}
          placeholder="Ej: 500.00"
          step="0.01"
        />

        <FormField
          label="Cuenta"
          name="cuentaId"
          type="select"
          required
          error={errors.cuentaId}
          register={register('cuentaId', {
            required: 'La cuenta es obligatoria',
            setValueAs: v => v === '' ? '' : Number(v)
          })}
          options={cuentaOptions}
          placeholder="Seleccionar cuenta"
          disabled={!!defaultValues || loadingCuentas}
        />
      </div>

      <div className="form-actions">
        <Button 
          type="button" 
          variant="ghost" 
          onClick={onCancel}
          disabled={isLoading}
        >
          Cancelar
        </Button>
        <Button 
          type="submit" 
          variant="primary"
          disabled={isLoading}
        >
          {isLoading ? 'Guardando...' : defaultValues ? 'Actualizar' : 'Crear'}
        </Button>
      </div>
    </form>
  );
};

export default MovimientoForm;
