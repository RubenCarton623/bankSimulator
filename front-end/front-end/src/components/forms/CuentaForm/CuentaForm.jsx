import React, { useEffect, useMemo } from 'react';
import { useForm } from 'react-hook-form';
import FormField from '../../molecules/FormField/FormField';
import Button from '../../atoms/Button/Button';
import { useClientes } from '../../../hooks/useClientes';
import './CuentaForm.css';

const CuentaForm = ({ 
  defaultValues = null, 
  onSubmit, 
  onCancel,
  isLoading = false 
}) => {
  const { clientes, loading: loadingClientes } = useClientes();
  
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm({
    defaultValues: defaultValues || {
      numeroCuenta: '',
      tipoCuenta: '',
      saldoInicial: '',
      estado: true,
      clienteId: ''
    }
  });

  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues);
    }
  }, [defaultValues, reset]);

  const tipoCuentaOptions = [
    { value: 'Ahorros', label: 'Ahorros' },
    { value: 'Corriente', label: 'Corriente' }
  ];

  const estadoOptions = [
    { value: true, label: 'Activo' },
    { value: false, label: 'Inactivo' }
  ];

  const clienteOptions = useMemo(() => {
    if (clientes && clientes.length > 0) {
      return clientes.map(cliente => ({
        value: cliente.clienteId,
        label: `${cliente.nombre} - ${cliente.identificacion}`
      }));
    }
    return [];
  }, [clientes]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="cuenta-form">
      <div className="form-grid">
        <FormField
          label="Numero de Cuenta"
          name="numeroCuenta"
          required
          error={errors.numeroCuenta}
          register={register('numeroCuenta', {
            required: 'El numero de cuenta es obligatorio',
            pattern: {
              value: /^[0-9]{6,20}$/,
              message: 'El numero de cuenta debe contener solo numeros (6-20 digitos)'
            }
          })}
          placeholder="Ej: 123456789012"
          disabled={!!defaultValues}
        />

        <FormField
          label="Tipo de Cuenta"
          name="tipoCuenta"
          type="select"
          required
          error={errors.tipoCuenta}
          register={register('tipoCuenta', {
            required: 'El tipo de cuenta es obligatorio'
          })}
          options={tipoCuentaOptions}
          placeholder="Seleccionar tipo de cuenta"
        />

        <FormField
          label="Saldo Inicial"
          name="saldoInicial"
          type="number"
          required
          error={errors.saldoInicial}
          register={register('saldoInicial', {
            required: 'El saldo inicial es obligatorio',
            min: {
              value: 0,
              message: 'El saldo inicial debe ser mayor o igual a 0'
            },
            valueAsNumber: true
          })}
          placeholder="Ej: 1000.00"
          step="0.01"
        />

        <FormField
          label="Cliente"
          name="clienteId"
          type="select"
          required
          error={errors.clienteId}
          register={register('clienteId', {
            required: 'El cliente es obligatorio',
            setValueAs: v => v === '' ? '' : Number(v)
          })}
          options={clienteOptions}
          placeholder="Seleccionar cliente"
          disabled={!!defaultValues || loadingClientes}
        />

        <FormField
          label="Estado"
          name="estado"
          type="select"
          required
          error={errors.estado}
          register={register('estado', {
            required: 'El estado es obligatorio',
            setValueAs: v => v === 'true' || v === true
          })}
          options={estadoOptions}
          placeholder="Seleccionar estado"
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

export default CuentaForm;
