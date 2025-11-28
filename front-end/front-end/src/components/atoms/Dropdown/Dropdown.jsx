import React from 'react';
import './Dropdown.css';

const Dropdown = React.forwardRef(({ 
  label, 
  error, 
  disabled = false,
  options = [],
  placeholder = 'Seleccionar...',
  ...props 
}, ref) => {
  return (
    <div className="dropdown-wrapper">
      {label && <label className="dropdown-label">{label}</label>}
      <select
        ref={ref}
        className={`dropdown ${error ? 'dropdown-error' : ''} ${disabled ? 'dropdown-disabled' : ''}`}
        disabled={disabled}
        {...props}
      >
        <option value="" disabled>
          {placeholder}
        </option>
        {options.map((option, index) => (
          <option key={index} value={option.value}>
            {option.label}
          </option>
        ))}
      </select>
      {error && <span className="dropdown-error-message">{error}</span>}
    </div>
  );
});

Dropdown.displayName = 'Dropdown';

export default Dropdown;
