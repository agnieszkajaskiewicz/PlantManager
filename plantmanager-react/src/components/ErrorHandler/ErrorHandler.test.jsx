import {render, screen} from '@testing-library/react';
import '@testing-library/jest-dom';
import ErrorHandler from './ErrorHandler';
import { MemoryRouter } from 'react-router-dom';

describe('<ErrorHandler />', () => {
  test('it should mount', () => {
    render(<MemoryRouter>
            <ErrorHandler />
          </MemoryRouter>);
  
    const errorHandler = screen.getByTestId('ErrorHandler');

    expect(errorHandler).toBeInTheDocument();
  });

  test('it should display correct message', () => {
    //given
    const message = "Error_Message";
    //when
    render(<ErrorHandler message={message} />);
    //then
    const errorHandler = screen.getByTestId('ErrorHandler');
    expect(errorHandler.innerHTML).toBe('Unfortunately, an error occurred: ' + message);
  })
});