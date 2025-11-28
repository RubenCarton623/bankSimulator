import React from 'react';
import './DateField.css';

const DateField = React.forwardRef(({ 
  label, 
  error, 
  disabled = false,
  min,
  max,
  ...props 
}, ref) => {
  const inputRef = React.useRef(null);

  const handleWrapperClick = () => {
    if (!disabled && inputRef.current) {
      inputRef.current.showPicker?.();
    }
  };

  return (
    <div className="date-field-wrapper" onClick={handleWrapperClick} style={{ cursor: disabled ? 'not-allowed' : 'pointer' }}>
      {label && <label className="date-field-label">{label}</label>}
      <input
        ref={(node) => {
          inputRef.current = node;
          if (typeof ref === 'function') {
            ref(node);
          } else if (ref) {
            ref.current = node;
          }
        }}
        type="date"
        className={`date-field ${error ? 'date-field-error' : ''} ${disabled ? 'date-field-disabled' : ''}`}
        disabled={disabled}
        min={min}
        max={max}
        {...props}
      />
      {error && <span className="date-field-error-message">{error}</span>}
    </div>
  );
});

DateField.displayName = 'DateField';

export default DateField;
