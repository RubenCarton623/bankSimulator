import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import FormField from '../../molecules/FormField/FormField';
import Button from '../../atoms/Button/Button';
import './ClienteForm.css';

const ClienteForm = ({ 
  defaultValues = null, 
  onSubmit, 
  onCancel,
  isLoading = false 
}) => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    reset
  } = useForm({
    defaultValues: defaultValues || {
      nombre: '',
      genero: '',
      edad: '',
      identificacion: '',
      direccion: '',
      telefono: '',
      contrasena: '',
      estado: true
    }
  });

  useEffect(() => {
    if (defaultValues) {
      reset(defaultValues);
    }
  }, [defaultValues, reset]);

  const generoOptions = [
    { value: 'M', label: 'Masculino' },
    { value: 'F', label: 'Femenino' },
    { value: 'Otro', label: 'Otro' }
  ];

  const estadoOptions = [
    { value: true, label: 'Activo' },
    { value: false, label: 'Inactivo' }
  ];

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="cliente-form">
      <div className="form-grid">
        <FormField
          label="Nombre completo"
          name="nombre"
          required
          error={errors.nombre}
          register={register('nombre', {
            required: 'El nombre es obligatorio',
            minLength: {
              value: 3,
              message: 'El nombre debe tener al menos 3 caracteres'
            },
            maxLength: {
              value: 255,
              message: 'El nombre no puede exceder 255 caracteres'
            }
          })}
          placeholder="Ej: Juan Perez"
        />

        <FormField
          label="Genero"
          name="genero"
          type="select"
          required
          error={errors.genero}
          register={register('genero', {
            required: 'El genero es obligatorio'
          })}
          options={generoOptions}
          placeholder="Seleccionar genero"
        />

        <FormField
          label="Edad"
          name="edad"
          type="number"
          required
          error={errors.edad}
          register={register('edad', {
            required: 'La edad es obligatoria',
            min: {
              value: 18,
              message: 'La edad minima es 18 anos'
            },
            max: {
              value: 120,
              message: 'La edad maxima es 120 anos'
            },
            valueAsNumber: true
          })}
          placeholder="Ej: 25"
        />

        <FormField
          label="Identificacion"
          name="identificacion"
          required
          error={errors.identificacion}
          register={register('identificacion', {
            required: 'La identificacion es obligatoria',
            pattern: {
              value: /^[0-9]{6,20}$/,
              message: 'La identificacion debe contener solo numeros (6-20 digitos)'
            }
          })}
          placeholder="Ej: 1234567890"
        />

        <FormField
          label="Direccion"
          name="direccion"
          required
          error={errors.direccion}
          register={register('direccion', {
            required: 'La direccion es obligatoria'
          })}
          placeholder="Ej: Calle 123 #45-67"
        />

        <FormField
          label="Telefono"
          name="telefono"
          required
          error={errors.telefono}
          register={register('telefono', {
            required: 'El telefono es obligatorio'
          })}
          placeholder="Ej: 3001234567"
        />

        <FormField
          label="Contrasena"
          name="contrasena"
          type="password"
          required={!defaultValues}
          error={errors.contrasena}
          register={register('contrasena', {
            required: !defaultValues ? 'La contrasena es obligatoria' : false,
            minLength: {
              value: 6,
              message: 'La contrasena debe tener al menos 6 caracteres'
            }
          })}
          placeholder={defaultValues ? 'Dejar en blanco para mantener la actual' : 'Minimo 6 caracteres'}
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

export default ClienteForm;
