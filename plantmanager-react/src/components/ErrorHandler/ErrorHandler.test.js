import React from 'react';
import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import ErrorHandler from './ErrorHandler';

describe('<ErrorHandler />', () => {
  test('it should mount', () => {
    render(<ErrorHandler />);
    
    const errorHandler = screen.getByTestId('ErrorHandler');

    expect(errorHandler).toBeInTheDocument();
  });
});