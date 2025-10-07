import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import PlantEditor from './PlantEditor';
import { MemoryRouter } from 'react-router-dom';

describe('<PlantEditor />', () => {
  test('it should mount', () => {
    render(<MemoryRouter>
            <PlantEditor />
          </MemoryRouter>);
    
    const plantEditor = screen.getByTestId('PlantEditor');

    expect(plantEditor).toBeInTheDocument();
  });
});