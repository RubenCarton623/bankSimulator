import { render, screen, fireEvent } from '@testing-library/react';
import Dropdown from '../../../components/atoms/Dropdown/Dropdown';

describe('Dropdown Component', () => {
  const mockOptions = [
    { value: '1', label: 'Option 1' },
    { value: '2', label: 'Option 2' },
    { value: '3', label: 'Option 3' }
  ];

  test('renders dropdown with options', () => {
    render(<Dropdown options={mockOptions} />);
    const select = screen.getByRole('combobox');
    expect(select).toBeInTheDocument();
  });

  test('renders all options', () => {
    render(<Dropdown options={mockOptions} />);
    mockOptions.forEach(option => {
      expect(screen.getByText(option.label)).toBeInTheDocument();
    });
  });

  test('renders placeholder', () => {
    render(<Dropdown options={mockOptions} placeholder="Select option" />);
    expect(screen.getByText('Select option')).toBeInTheDocument();
  });

  test('handles selection changes', () => {
    const handleChange = jest.fn();
    render(<Dropdown options={mockOptions} onChange={handleChange} />);
    
    const select = screen.getByRole('combobox');
    fireEvent.change(select, { target: { value: '2' } });
    
    expect(handleChange).toHaveBeenCalled();
  });

  test('disables dropdown when disabled prop is true', () => {
    render(<Dropdown options={mockOptions} disabled />);
    expect(screen.getByRole('combobox')).toBeDisabled();
  });

  test('applies error class when error is present', () => {
    render(<Dropdown options={mockOptions} error="Error message" />);
    const select = screen.getByRole('combobox');
    expect(select).toHaveClass('dropdown-error');
  });

  test('renders with label', () => {
    render(<Dropdown options={mockOptions} label="Select Item" />);
    expect(screen.getByText('Select Item')).toBeInTheDocument();
  });

  test('forwards ref correctly', () => {
    const ref = { current: null };
    render(<Dropdown options={mockOptions} ref={ref} />);
    expect(ref.current).toBeInstanceOf(HTMLSelectElement);
  });
});
