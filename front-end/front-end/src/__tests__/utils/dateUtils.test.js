import { formatDate, formatDateTime, isDateInRange, parseISODate } from '../../utils/dateUtils';

describe('dateUtils', () => {
  describe('formatDate', () => {
    test('formats date to dd/mm/yyyy', () => {
      const date = '2025-11-28T01:37:12.730Z';
      const formatted = formatDate(date);
      
      expect(formatted).toMatch(/\d{2}\/\d{2}\/\d{4}/);
    });

    test('returns empty string for null/undefined', () => {
      expect(formatDate(null)).toBe('');
      expect(formatDate(undefined)).toBe('');
      expect(formatDate('')).toBe('');
    });

    test('returns original string for invalid date', () => {
      const invalidDate = 'not-a-date';
      expect(formatDate(invalidDate)).toBe(invalidDate);
    });
  });

  describe('formatDateTime', () => {
    test('formats date to dd/mm/yyyy hh:mm', () => {
      const date = '2025-11-28T01:37:12.730Z';
      const formatted = formatDateTime(date);
      
      expect(formatted).toMatch(/\d{2}\/\d{2}\/\d{4} \d{2}:\d{2}/);
    });

    test('returns empty string for null/undefined', () => {
      expect(formatDateTime(null)).toBe('');
      expect(formatDateTime(undefined)).toBe('');
    });
  });

  describe('isDateInRange', () => {
    test('returns true when date is in range', () => {
      const dateToCheck = new Date('2025-11-15');
      const startDate = new Date('2025-11-01');
      const endDate = new Date('2025-11-30');
      
      expect(isDateInRange(dateToCheck, startDate, endDate)).toBe(true);
    });

    test('returns false when date is before range', () => {
      const dateToCheck = new Date('2025-10-15');
      const startDate = new Date('2025-11-01');
      const endDate = new Date('2025-11-30');
      
      expect(isDateInRange(dateToCheck, startDate, endDate)).toBe(false);
    });

    test('returns false when date is after range', () => {
      const dateToCheck = new Date('2025-12-15');
      const startDate = new Date('2025-11-01');
      const endDate = new Date('2025-11-30');
      
      expect(isDateInRange(dateToCheck, startDate, endDate)).toBe(false);
    });

    test('returns true for dates at range boundaries', () => {
      const startDate = new Date('2025-11-01');
      const endDate = new Date('2025-11-30');
      
      expect(isDateInRange(startDate, startDate, endDate)).toBe(true);
      expect(isDateInRange(endDate, startDate, endDate)).toBe(true);
    });
  });

  describe('parseISODate', () => {
    test('parses ISO date string correctly', () => {
      const isoString = '2025-11-28T01:37:12.730+00:00';
      const parsed = parseISODate(isoString);
      
      expect(parsed).toBeInstanceOf(Date);
      expect(parsed.getFullYear()).toBe(2025);
      expect(parsed.getMonth()).toBe(10); // November (0-indexed)
      // No verificamos el día exacto porque puede variar según la zona horaria
      expect(parsed.getDate()).toBeGreaterThanOrEqual(27);
      expect(parsed.getDate()).toBeLessThanOrEqual(28);
    });

    test('returns null for invalid ISO string', () => {
      const result = parseISODate('invalid-iso-string');
      expect(result).toBeNull();
    });

    test('handles null/undefined input', () => {
      // parseISODate maneja null/undefined creando una fecha epoch
      const resultNull = parseISODate(null);
      const resultUndefined = parseISODate(undefined);
      
      // Estos retornan una fecha válida (epoch) en lugar de null
      expect(resultNull).toBeInstanceOf(Date);
      expect(resultUndefined).toBeInstanceOf(Date);
    });
  });
});
