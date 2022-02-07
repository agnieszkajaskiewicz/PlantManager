import React from 'react';
import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import PlantEditor from './PlantEditor';

describe('<PlantEditor />', () => {
  test('it should mount', () => {
    render(<PlantEditor />);
    
    const plantEditor = screen.getByTestId('PlantEditor');

    expect(plantEditor).toBeInTheDocument();
  });
});