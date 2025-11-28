import React from 'react';
import './Label.css';

const Label = ({ 
  htmlFor, 
  children, 
  required = false,
  className = '' 
}) => {
  return (
    <label 
      htmlFor={htmlFor} 
      className={`label ${className}`}
    >
      {children}
      {required && <span className="label-required">*</span>}
    </label>
  );
};

export default Label;
