import React from 'react';
import './InputText.css';

const InputText = React.forwardRef(({ 
  label, 
  error, 
  placeholder,
  disabled = false,
  ...props 
}, ref) => {
  return (
    <div className="input-text-wrapper">
      {label && <label className="input-text-label">{label}</label>}
      <input
        ref={ref}
        type="text"
        className={`input-text ${error ? 'input-text-error' : ''} ${disabled ? 'input-text-disabled' : ''}`}
        placeholder={placeholder}
        disabled={disabled}
        {...props}
      />
      {error && <span className="input-text-error-message">{error}</span>}
    </div>
  );
});

InputText.displayName = 'InputText';

export default InputText;
