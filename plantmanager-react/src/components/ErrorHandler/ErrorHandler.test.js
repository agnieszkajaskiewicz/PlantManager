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

  test('it should display correct message', () => {
    //given
    debugger;
    const message = "Error_Message";
    //when
    render(<ErrorHandler message={message} />);
    //then
    const errorHandler = screen.getByTestId('ErrorHandler');
    expect(errorHandler.innerHTML).toBe('Unfortunately, an error occurred: ' + message);
  })
});