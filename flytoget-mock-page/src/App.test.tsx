import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('has a card registration iframe title', () => {
  render(<App />);
  const linkElement = screen.getByTitle("card registration iframe")
  expect(linkElement).toBeInTheDocument();
});
