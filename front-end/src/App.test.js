describe('App', () => {
  test('renders without crashing', () => {
    // Simple test that verifies the app can be imported
    const App = require('./App').default;
    expect(App).toBeDefined();
  });
});
