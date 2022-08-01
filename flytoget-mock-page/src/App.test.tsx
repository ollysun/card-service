import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('has a card registration iframe title', () => {
  render(<App />);
  const iFrameTitle = process.env.REACT_APP_IFRAME_TITLE || "card registration iframe"
  const linkElement = screen.getByTitle(iFrameTitle)
  expect(linkElement).toBeInTheDocument();
});
