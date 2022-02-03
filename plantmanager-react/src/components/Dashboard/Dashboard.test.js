import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Dashboard from './Dashboard';

//todo dodać mockowanie strzału po rośliny do backendu

describe('<Dashboard />', () => {
  test('it should mount', () => {
    render(<Dashboard />);
    
    const dashboard = screen.getByTestId('Dashboard');

    expect(dashboard).toBeInTheDocument();
  });

  test('it should display correct cards with plants from payload', () => {

  });
});