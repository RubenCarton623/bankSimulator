import React from 'react';
import './InputNumber.css';

const InputNumber = React.forwardRef(({ 
  label, 
  error, 
  placeholder,
  disabled = false,
  min,
  max,
  step = 1,
  ...props 
}, ref) => {
  return (
    <div className="input-number-wrapper">
      {label && <label className="input-number-label">{label}</label>}
      <input
        ref={ref}
        type="number"
        className={`input-number ${error ? 'input-number-error' : ''} ${disabled ? 'input-number-disabled' : ''}`}
        placeholder={placeholder}
        disabled={disabled}
        min={min}
        max={max}
        step={step}
        {...props}
      />
      {error && <span className="input-number-error-message">{error}</span>}
    </div>
  );
});

InputNumber.displayName = 'InputNumber';

export default InputNumber;
