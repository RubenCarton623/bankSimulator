import { render, screen, fireEvent } from '@testing-library/react';
import InputText from '../../../components/atoms/InputText/InputText';

describe('InputText Component', () => {
  test('renders input with placeholder', () => {
    render(<InputText placeholder="Enter text" />);
    expect(screen.getByPlaceholderText('Enter text')).toBeInTheDocument();
  });

  test('handles value changes', () => {
    const handleChange = jest.fn();
    render(<InputText onChange={handleChange} />);
    
    const input = screen.getByRole('textbox');
    fireEvent.change(input, { target: { value: 'test value' } });
    
    expect(handleChange).toHaveBeenCalled();
  });

  test('disables input when disabled prop is true', () => {
    render(<InputText disabled />);
    expect(screen.getByRole('textbox')).toBeDisabled();
  });

  test('applies error class when error is present', () => {
    render(<InputText error="Error message" />);
    const input = screen.getByRole('textbox');
    expect(input).toHaveClass('input-text-error');
  });

  test('renders with different types', () => {
    const { rerender } = render(<InputText type="email" />);
    expect(screen.getByRole('textbox')).toHaveAttribute('type', 'email');
    
    rerender(<InputText type="number" />);
    expect(screen.getByRole('spinbutton')).toHaveAttribute('type', 'number');
  });

  test('forwards ref correctly', () => {
    const ref = { current: null };
    render(<InputText ref={ref} />);
    expect(ref.current).toBeInstanceOf(HTMLInputElement);
  });
});
