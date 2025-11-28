# Pruebas Unitarias - Front-End

Este proyecto incluye pruebas unitarias completas para componentes, hooks, servicios y utilidades.

## Estructura de Pruebas

```
src/
├── components/
│   └── atoms/
│       ├── Button/
│       │   └── Button.test.js
│       ├── InputText/
│       │   └── InputText.test.js
│       ├── Dropdown/
│       │   └── Dropdown.test.js
│       └── DateField/
│           └── DateField.test.js
├── hooks/
│   ├── useClientes.test.js
│   ├── useCuentas.test.js
│   └── useDebounce.test.js
├── services/
│   ├── clientesService.test.js
│   └── movimientosService.test.js
├── utils/
│   └── dateUtils.test.js
└── App.test.js
```

## Ejecutar las Pruebas

### Ejecutar todas las pruebas
```bash
npm test
```

### Ejecutar pruebas en modo watch
```bash
npm test -- --watch
```

### Ejecutar pruebas con cobertura
```bash
npm test -- --coverage
```

### Ejecutar pruebas específicas
```bash
# Por nombre de archivo
npm test Button.test.js

# Por patrón
npm test -- --testNamePattern="renders button"
```

## Cobertura de Pruebas

### Componentes Atómicos
- ✅ **Button**: Variantes, eventos click, estados disabled
- ✅ **InputText**: Cambios de valor, tipos, validaciones
- ✅ **Dropdown**: Selección de opciones, placeholders, estados
- ✅ **DateField**: Selección de fechas, validaciones min/max, eventos click

### Hooks Personalizados
- ✅ **useClientes**: Carga de datos, manejo de errores, estados loading
- ✅ **useCuentas**: Carga de datos, manejo de errores, estados loading
- ✅ **useDebounce**: Debounce de valores, cancelación de timeouts

### Servicios
- ✅ **clientesService**: CRUD completo (getAll, getById, create, update, delete)
- ✅ **movimientosService**: Reportes, descarga de PDF, manejo de errores

### Utilidades
- ✅ **dateUtils**: Formateo de fechas, validación de rangos, parseo ISO

## Tecnologías Utilizadas

- **Jest**: Framework de pruebas
- **React Testing Library**: Testing de componentes React
- **@testing-library/user-event**: Simulación de eventos de usuario

## Mejores Prácticas

1. **Nombres descriptivos**: Cada test describe claramente qué está probando
2. **Arrange-Act-Assert**: Estructura clara en cada prueba
3. **Mocks y Stubs**: Para dependencias externas (fetch, servicios)
4. **Cobertura completa**: Casos positivos, negativos y edge cases
5. **Aislamiento**: Cada test es independiente y puede ejecutarse solo

## Ejemplos de Uso

### Probar un componente
```javascript
test('renders button with text', () => {
  render(<Button>Click me</Button>);
  expect(screen.getByText('Click me')).toBeInTheDocument();
});
```

### Probar un hook
```javascript
test('fetches clientes successfully', async () => {
  const { result } = renderHook(() => useClientes());
  await waitFor(() => {
    expect(result.current.clientes).toHaveLength(2);
  });
});
```

### Probar un servicio
```javascript
test('creates cliente successfully', async () => {
  const newCliente = { nombre: 'Juan' };
  const result = await clientesService.create(newCliente);
  expect(result).toEqual(expect.objectContaining(newCliente));
});
```

## Solución de Problemas

### Error: "Cannot find module"
Asegúrate de que todas las dependencias estén instaladas:
```bash
npm install
```

### Tests lentos
Usa el modo watch para ejecutar solo los tests afectados:
```bash
npm test -- --watch
```

### Mock de fetch no funciona
Verifica que `global.fetch` esté siendo mockeado en `beforeEach`:
```javascript
beforeEach(() => {
  global.fetch = jest.fn();
});
```

## Contribuir

Al agregar nuevas funcionalidades:
1. Escribe las pruebas primero (TDD)
2. Asegúrate de que todas las pruebas pasen
3. Mantén la cobertura de código por encima del 80%
4. Documenta casos edge especiales
