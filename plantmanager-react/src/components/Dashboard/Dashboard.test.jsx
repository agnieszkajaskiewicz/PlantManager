import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import Dashboard from './Dashboard';
import { MemoryRouter } from 'react-router-dom';

//todo dodać mockowanie strzału po rośliny do backendu

describe('<Dashboard />', () => {
  test('it should mount', () => {
    render(<MemoryRouter>
            <Dashboard />
          </MemoryRouter>);
    
    const dashboard = screen.getByTestId('Dashboard');

    expect(dashboard).toBeInTheDocument();
  });

  test('it should display correct cards with plants from payload', () => {

  });
});