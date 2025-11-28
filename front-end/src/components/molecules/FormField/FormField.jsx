import React from 'react';
import Label from '../../atoms/Label/Label';
import InputText from '../../atoms/InputText/InputText';
import Dropdown from '../../atoms/Dropdown/Dropdown';
import './FormField.css';

const FormField = ({ 
  label,
  name,
  type = 'text',
  required = false,
  error,
  register,
  options,
  placeholder = '',
  ...rest
}) => {
  const isDropdown = type === 'select';

  return (
    <div className="form-field">
      <Label htmlFor={name} required={required}>
        {label}
      </Label>
      
      {isDropdown ? (
        <Dropdown
          id={name}
          {...register}
          options={options}
          placeholder={placeholder}
          className={error ? 'input-error' : ''}
          {...rest}
        />
      ) : (
        <InputText
          id={name}
          type={type}
          placeholder={placeholder}
          {...register}
          className={error ? 'input-error' : ''}
          {...rest}
        />
      )}
      
      {error && (
        <span className="form-field-error">{error.message}</span>
      )}
    </div>
  );
};

export default FormField;
