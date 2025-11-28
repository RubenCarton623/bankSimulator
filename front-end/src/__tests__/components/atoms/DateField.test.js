import { render, screen, fireEvent } from '@testing-library/react';
import DateField from '../../../components/atoms/DateField/DateField';

describe('DateField Component', () => {
  test('renders date input', () => {
    render(<DateField data-testid="date-input" />);
    const input = screen.getByTestId('date-input');
    expect(input).toHaveAttribute('type', 'date');
  });

  test('renders with label', () => {
    render(<DateField label="Select Date" />);
    expect(screen.getByText('Select Date')).toBeInTheDocument();
  });

  test('handles date changes', () => {
    const handleChange = jest.fn();
    render(<DateField onChange={handleChange} data-testid="date-input" />);
    
    const input = screen.getByTestId('date-input');
    fireEvent.change(input, { target: { value: '2025-11-27' } });
    
    expect(handleChange).toHaveBeenCalled();
  });

  test('disables input when disabled prop is true', () => {
    render(<DateField disabled data-testid="date-input" />);
    expect(screen.getByTestId('date-input')).toBeDisabled();
  });

  test('applies min and max constraints', () => {
    render(<DateField min="2025-01-01" max="2025-12-31" data-testid="date-input" />);
    const input = screen.getByTestId('date-input');
    expect(input).toHaveAttribute('min', '2025-01-01');
    expect(input).toHaveAttribute('max', '2025-12-31');
  });

  test('displays error message', () => {
    render(<DateField error="Invalid date" />);
    expect(screen.getByText('Invalid date')).toBeInTheDocument();
  });

  test('opens picker on wrapper click', () => {
    const { container } = render(<DateField data-testid="date-input" />);
    const wrapper = container.firstChild;
    
    const mockShowPicker = jest.fn();
    const input = screen.getByTestId('date-input');
    input.showPicker = mockShowPicker;
    
    fireEvent.click(wrapper);
    expect(mockShowPicker).toHaveBeenCalled();
  });
});
